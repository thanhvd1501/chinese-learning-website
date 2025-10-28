'use client';
import dynamic from 'next/dynamic';
import { useState, useMemo } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';

pdfjs.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.mjs';

const HTMLFlipBook = dynamic(() => import('react-pageflip'), { ssr: false });

function Sheet({ children, number }: React.PropsWithChildren<{ number: number }>, ref: any) {
  return (
    <div ref={ref} className="w-full h-full rounded-xl bg-white shadow-xl overflow-hidden">
      <div className="absolute top-2 right-3 text-xs text-neutral-400 select-none">{number}</div>
      {children}
    </div>
  );
}
// Định nghĩa PageWrap dùng forwardRef trực tiếp, không dynamic
import React from 'react';
const PageWrap = React.forwardRef(Sheet);

type Props = { file: string };


export default function PdfFlipbook({ file }: Props) {
  const [numPages, setNumPages] = useState<number>(0);
  // Responsive: rộng hơn trên desktop, nhỏ lại trên mobile
  const pageWidth = typeof window !== 'undefined' && window.innerWidth < 600 ? 180 : 400;
  const width = pageWidth * 2;
  const height = Math.round(pageWidth * 1.414);
  // Spread: luôn chẵn số trang, nếu lẻ thì thêm 1 trang trắng
  // Spread chuẩn sách: nếu số trang chẵn, thêm trắng đầu để [trắng, 1], [2,3], ...
  const spreadPages = useMemo(() => {
    let arr = Array.from({ length: numPages }, (_, i) => i + 1);
    arr = [-1, ...arr]; // Luôn thêm trắng đầu
    if (arr.length % 2 !== 0) arr.push(-1); // Nếu thiếu thì thêm trắng cuối
    const spreads = [];
    for (let i = 0; i < arr.length; i += 2) {
      spreads.push([arr[i], arr[i + 1]]);
    }
    return spreads;
  }, [numPages]);

  return (
    <div className="min-h-[80vh] flex flex-col items-center justify-center bg-neutral-100 px-2">
      <Document
        file={file}
        loading={<div className="p-8 text-neutral-500">Đang tải PDF…</div>}
        onLoadSuccess={({ numPages }) => setNumPages(numPages)}
        onLoadError={(e) => console.error(e)}
      >
        {numPages > 0 && (
          <div className="flex flex-col items-center">
            <HTMLFlipBook
              width={width}
              height={height}
              size="fixed"
              showCover={false}
              flippingTime={700}
              maxShadowOpacity={0.25}
              mobileScrollSupport={true}
              startPage={0}
              minWidth={320}
              maxWidth={1200}
              minHeight={200}
              maxHeight={1600}
              autoSize={true}
              swipeDistance={50}
              useMouseEvents
              drawShadow={true}
              usePortrait={true}
              startZIndex={0}
              clickEventForward={true}
              disableFlipByClick={false}
              onFlip={() => {}}
              onChangeOrientation={() => {}}
              onChangeState={() => {}}
              showPageCorners={true}
              className="rounded-2xl border border-gray-200 shadow-2xl"
              style={{ boxShadow: '0 25px 80px rgba(0,0,0,.18)', minWidth: 320, maxWidth: '98vw', background: '#fff' }}
            >
              {spreadPages.map((pair, idx) => (
                <PageWrap key={idx} number={undefined}>
                  <div style={{ display: 'flex', width: width, height: height }}>
                    {pair.map((p, i) => (
                      <div key={i} style={{ position: 'relative', width: pageWidth, height: height, background: '#fff' }}>
                        {p !== -1 && (
                          <Page
                            pageNumber={p}
                            width={pageWidth}
                            height={height}
                            renderTextLayer={false}
                            renderAnnotationLayer={false}
                          />
                        )}
                      </div>
                    ))}
                  </div>
                </PageWrap>
              ))}
            </HTMLFlipBook>
            <div className="mt-4 text-sm text-gray-500 text-center select-none">
              Lật trang bằng cách kéo chuột hoặc bấm vào mép trang<br/>
              (Kéo sang trái/phải hoặc chạm trên mobile)
            </div>
          </div>
        )}
      </Document>
      <style jsx global>{`
        .page { position: relative; }
        .page::after{
          content:''; position:absolute; right:0; bottom:0; width:90px; height:90px;
          background: radial-gradient(90px 90px at 100% 100%,
                      rgba(0,0,0,.18) 0%, rgba(0,0,0,.08) 40%, transparent 70%);
          clip-path: polygon(100% 0, 0 100%, 100% 100%);
          opacity:.16; pointer-events:none; transition:opacity .2s ease-out;
        }
        .page:hover::after{ opacity:.28; }
      `}</style>
    </div>
  );
}
