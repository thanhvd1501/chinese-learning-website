// components/PdfFlipbookSpread.tsx
'use client';

import dynamic from 'next/dynamic';
import React, {
  useEffect,
  useMemo,
  useState,
  forwardRef,
  useRef,
  useCallback,
} from 'react';
import { Document, Page, pdfjs } from 'react-pdf';

pdfjs.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.mjs';
const HTMLFlipBook = dynamic(() => import('react-pageflip'), { ssr: false });

/** ============== Sheet: KHÔNG re-render để nét lại ============== */
type SheetProps = { number: number; numPages: number; width: number };

const Sheet = forwardRef<HTMLDivElement, SheetProps>(
  ({ number, numPages, width }, ref) => {
    return (
      <div
        ref={ref as any}
        className="page relative w-full h-full bg-white overflow-hidden rounded-lg"
      >
        {/* Trang hiện tại (canvas cố định theo width cơ sở) */}
        <div className="face front">
          <Page
            pageNumber={number}
            width={width}
            renderTextLayer={false}
            renderAnnotationLayer={false}
            loading={null}   // không chèn placeholder trắng
            noData={null}
            error={null}
          />
        </div>

        {/* Lớp "xuyên giấy" (trang sau mờ mờ) — có thể giữ hoặc bỏ */}
        {number < numPages && (
          <div className="face bleed pointer-events-none">
            <Page
              pageNumber={number + 1}
              width={width}
              renderTextLayer={false}
              renderAnnotationLayer={false}
              loading={null}
              noData={null}
              error={null}
            />
          </div>
        )}

        {/* Bóng mép trái/phải cho cảm giác có trang bên dưới */}
        <div className="absolute left-0 top-0 h-full w-4 bg-gradient-to-r from-black/5 to-transparent opacity-60" />
        <div className="absolute right-0 top-0 h-full w-4 bg-gradient-to-l from-black/5 to-transparent opacity-60" />
      </div>
    );
  }
);
Sheet.displayName = 'Sheet';

/** ============== Main ============== */
export default function PdfFlipbookSpread({ file = '/book.pdf' }: { file: string }) {
  const [numPages, setNumPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);

  // Zoom chỉ bằng CSS (không re-render PDF để nét lại)
  const [zoom, setZoom] = useState(1);

  // Pan state (kéo để xem vùng khác)
  const [panX, setPanX] = useState(0);
  const [panY, setPanY] = useState(0);
  const [isPanning, setIsPanning] = useState(false);
  const [spaceHeld, setSpaceHeld] = useState(false);
  const dragStartRef = useRef<{ x: number; y: number; panX: number; panY: number } | null>(null);

  // Viewport ref để lấy kích thước khung xem
  const viewportRef = useRef<HTMLDivElement | null>(null);

  // Flipbook ref
  const flipbookRef = useRef<any>(null);

  const [isFullscreen, setIsFullscreen] = useState(false);
  const [showToolbar, setShowToolbar] = useState(false);

  // ==== Zoom steps & handlers ====
  const zoomSteps = [0.5, 0.75, 1, 1.25, 1.5, 2, 3];
  const nextStepUp = (val: number) => {
    const i = zoomSteps.findIndex((z) => z > val);
    return i === -1 ? zoomSteps[zoomSteps.length - 1] : zoomSteps[i];
  };
  const nextStepDown = (val: number) => {
    const i = zoomSteps.findIndex((z) => z >= val);
    return i <= 0 ? zoomSteps[0] : zoomSteps[i - 1];
  };

  const handleZoomIn = () =>
    setZoom((p) => {
      const nz = nextStepUp(p);
      recenterAfterZoom(p, nz);
      return nz;
    });

  const handleZoomOut = () =>
    setZoom((p) => {
      const nz = nextStepDown(p);
      recenterAfterZoom(p, nz);
      return nz;
    });

  const handleZoomReset = () => {
    setZoom(1);
    setPanX(0);
    setPanY(0);
  };

  // ==== Base page size (A4 ratio ~ 1:1.414). ====
  const basePageWidth = 600;
  const basePageHeight = Math.round(basePageWidth * 1.414);

  // ===> Quan trọng: Flipbook hiển thị dạng SPREAD (2 trang)
  // Nên tổng nội dung cần pan = 2 * width của một trang
  const contentWidth = basePageWidth * 2;
  const contentHeight = basePageHeight;

  const pages = useMemo(() => Array.from({ length: numPages }, (_, i) => i + 1), [numPages]);

  // ==== Hiện toolbar theo chuột ====
  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      setShowToolbar(e.clientY >= window.innerHeight - 100);
    };
    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  // ==== Fullscreen ====
  const handleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
      setIsFullscreen(true);
    } else {
      document.exitFullscreen();
      setIsFullscreen(false);
    }
  };

  // ==== Lật trang ====
  const handleFlip = (e: any) => {
    if (typeof e?.data === 'number') setCurrentPage(e.data + 1);
  };

  // ==== Clamp pan để không trượt quá biên ====
  const clampPan = useCallback(
    (x: number, y: number) => {
      const vp = viewportRef.current;
      if (!vp) return { x, y };

      const vpW = vp.clientWidth;
      const vpH = vp.clientHeight;

      const scaledW = contentWidth * zoom;  // spread 2 trang
      const scaledH = contentHeight * zoom;

      // Nếu nội dung nhỏ hơn viewport, giữ center (pan = 0)
      const minX = Math.min(0, vpW - scaledW);
      const maxX = 0;
      const minY = Math.min(0, vpH - scaledH);
      const maxY = 0;

      const cx = Math.max(minX, Math.min(maxX, x));
      const cy = Math.max(minY, Math.min(maxY, y));
      return { x: cx, y: cy };
    },
    [zoom, contentWidth, contentHeight]
  );

  // ==== Giữ vùng nhìn khi đổi zoom (zoom quanh tâm viewport) ====
  const recenterAfterZoom = (oldZ: number, newZ: number) => {
    const vp = viewportRef.current;
    if (!vp) return;

    const vpW = vp.clientWidth,
      vpH = vp.clientHeight;

    // Tâm viewport quy về toạ độ nội dung trước zoom
    const centerX = -panX + vpW / 2;
    const centerY = -panY + vpH / 2;

    const scale = newZ / oldZ;
    const newCenterX = centerX * scale;
    const newCenterY = centerY * scale;

    let nx = -(newCenterX - vpW / 2);
    let ny = -(newCenterY - vpH / 2);

    const clamped = clampPan(nx, ny);
    setPanX(clamped.x);
    setPanY(clamped.y);
  };

  // ==== Pointer pan (chuột/touch) ====
  useEffect(() => {
    const vp = viewportRef.current;
    if (!vp) return;

    const onPointerDown = (e: PointerEvent) => {
      // Kéo khi zoom > 1 hoặc giữ Space (giống các app design)
      const canPan = zoom > 1 || spaceHeld;
      // Chỉ kéo bằng chuột trái / touch / pen
      const isPrimary = e.isPrimary && (e.button === 0 || e.pointerType !== 'mouse');
      if (!canPan || !isPrimary) return;

      vp.setPointerCapture(e.pointerId);
      setIsPanning(true);
      dragStartRef.current = {
        x: e.clientX,
        y: e.clientY,
        panX,
        panY,
      };
    };

    const onPointerMove = (e: PointerEvent) => {
      if (!isPanning || !dragStartRef.current) return;
      const dx = e.clientX - dragStartRef.current.x;
      const dy = e.clientY - dragStartRef.current.y;

      const nxt = clampPan(dragStartRef.current.panX + dx, dragStartRef.current.panY + dy);
      setPanX(nxt.x);
      setPanY(nxt.y);
    };

    const endPan = (e: PointerEvent) => {
      if (!isPanning) return;
      vp.releasePointerCapture?.(e.pointerId);
      setIsPanning(false);
      dragStartRef.current = null;
    };

    vp.addEventListener('pointerdown', onPointerDown);
    window.addEventListener('pointermove', onPointerMove);
    window.addEventListener('pointerup', endPan);
    window.addEventListener('pointercancel', endPan);

    return () => {
      vp.removeEventListener('pointerdown', onPointerDown);
      window.removeEventListener('pointermove', onPointerMove);
      window.removeEventListener('pointerup', endPan);
      window.removeEventListener('pointercancel', endPan);
    };
  }, [zoom, panX, panY, clampPan, isPanning, spaceHeld]);

  // ==== Space để bật/tắt pan dễ dàng ====
  useEffect(() => {
    const onKeyDown = (e: KeyboardEvent) => {
      if (e.code === 'Space') {
        e.preventDefault();
        setSpaceHeld(true);
      }
    };
    const onKeyUp = (e: KeyboardEvent) => {
      if (e.code === 'Space') setSpaceHeld(false);
    };
    window.addEventListener('keydown', onKeyDown);
    window.addEventListener('keyup', onKeyUp);
    return () => {
      window.removeEventListener('keydown', onKeyDown);
      window.removeEventListener('keyup', onKeyUp);
    };
  }, []);

  // ==== (Tuỳ chọn) Wheel để pan: cuộn dọc/giữ Shift để cuộn ngang ====
  useEffect(() => {
    const vp = viewportRef.current;
    if (!vp) return;

    const onWheel = (e: WheelEvent) => {
      if (zoom <= 1) return; // khi không phóng, để scroll mặc định trang
      e.preventDefault();

      const dx = e.shiftKey ? e.deltaY : e.deltaX;
      const dy = e.shiftKey ? 0 : e.deltaY;

      const nxt = clampPan(panX - dx, panY - dy); // trừ để cảm giác kéo nội dung
      setPanX(nxt.x);
      setPanY(nxt.y);
    };

    vp.addEventListener('wheel', onWheel, { passive: false });
    return () => vp.removeEventListener('wheel', onWheel);
  }, [zoom, panX, panY, clampPan]);

  return (
    <div className="min-h-[80vh] bg-neutral-100 relative pb-8">
      {/* Viewport: khung xem cố định, ẩn tràn; pan = translate, zoom = scale */}
      <div
        ref={viewportRef}
        className="relative mx-auto rounded-xl"
        style={{
          width: 'min(94vw, 1200px)',
          height: 'min(86vh, 1200px)',
          overflow: 'hidden',
          boxShadow: '0 25px 80px rgba(0,0,0,.12)',
          background: 'transparent',
          cursor: zoom > 1 || spaceHeld ? (isPanning ? 'grabbing' : 'grab') : 'default',
          touchAction: 'none', // cho phép pan mượt trên touch
        }}
      >
        {/* Content: translate(x,y) scale(zoom) */}
        <div
          style={{
            position: 'absolute',
            left: 0,
            top: 0,
            transform: `translate(${panX}px, ${panY}px) scale(${zoom})`,
            transformOrigin: 'top left',
            transition: isPanning ? 'none' : 'transform 120ms ease-out',
            willChange: 'transform',
            width: contentWidth,   // giúp clamp trực quan khi inspect
            height: contentHeight,
          }}
        >
          <Document
            file={file}
            onLoadSuccess={({ numPages }) => setNumPages(numPages)}
            loading={<div className="p-8">Đang tải PDF…</div>}
          >
            {!!numPages && (
              <HTMLFlipBook
                ref={flipbookRef}
                width={basePageWidth}       // width của MỖI TRANG
                height={basePageHeight}
                size="fixed"
                usePortrait={false}
                showCover
                flippingTime={750}
                drawShadow
                maxShadowOpacity={0.35}
                mobileScrollSupport
                minWidth={400}
                maxWidth={1450}
                minHeight={300}
                maxHeight={1950}
                autoSize={false}            // quan trọng: giữ kích thước nội dung cố định
                swipeDistance={50}
                useMouseEvents
                startZIndex={0}
                clickEventForward
                disableFlipByClick={false}
                onFlip={handleFlip}
                showPageCorners
                startPage={0}
                className="rounded-xl"
                style={{ boxShadow: '0 25px 80px rgba(0,0,0,.18)' }}
              >
                {pages.map((p) => (
                  <Sheet key={p} number={p} numPages={numPages} width={basePageWidth} />
                ))}
              </HTMLFlipBook>
            )}
          </Document>
        </div>

        {/* Hint Space-to-pan */}
        {spaceHeld && (
          <div className="absolute left-3 bottom-3 px-2 py-1 text-xs rounded bg-black/60 text-white select-none">
            Space + kéo để pan
          </div>
        )}
      </div>

      {/* Toolbar nổi */}
      <div
        className="absolute z-50 bg-white/50 backdrop-blur-sm rounded-t-lg shadow-lg border border-gray-200 px-3 py-2 flex items-center gap-3 justify-center"
        style={{
          left: '50%',
          transform: 'translateX(-50%)',
          bottom: '-52px',
          width: 'min(94vw,720px)',
        }}
      >
        <div className="text-sm text-gray-600">{currentPage} / {numPages}</div>

        <div className="flex items-center gap-1">
          <button onClick={handleZoomOut} className="p-2 hover:bg-gray-100 rounded">−</button>
          <span className="text-sm text-gray-600 min-w-[3rem] text-center">{Math.round(zoom * 100)}%</span>
          <button onClick={handleZoomIn} className="p-2 hover:bg-gray-100 rounded">＋</button>
          <button onClick={handleZoomReset} className="px-2 py-1 text-xs bg-gray-100 hover:bg-gray-200 rounded">Reset</button>
        </div>

        <button
          onClick={handleFullscreen}
          className="p-2 hover:bg-gray-100 rounded transition-colors"
          title={isFullscreen ? 'Thoát fullscreen' : 'Xem fullscreen'}
        >
          ⤢
        </button>
      </div>

      {/* CSS */}
      <style jsx global>{`
        .page { position: relative; transform-style: preserve-3d; }
        .face { position: absolute; inset: 0; }
        .face.front { backface-visibility: hidden; }
        .face.bleed {
          opacity: .10;
          transform: scaleX(-1);
          filter: blur(.4px) contrast(.95);
          mix-blend-mode: multiply;
        }
        .react-pdf__Page__canvas {
          image-rendering: -webkit-optimize-contrast;
          image-rendering: crisp-edges;
          image-rendering: pixelated;   /* khi zoom lớn */
          transform: translateZ(0);
          backface-visibility: hidden;
          will-change: transform;
        }
      `}</style>
    </div>
  );
}
