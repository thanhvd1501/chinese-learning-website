import { useEffect, useRef, useState } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import PdfFlipbookSpread from './PdfFlipbookSpread';

pdfjs.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.mjs';


const loadStPageFlip = () => import('stpageflip');

export default function StFlipbook({ file }: { file: string }) {
  const containerRef = useRef<HTMLDivElement>(null);
  const [numPages, setNumPages] = useState(0);
  const [pdf, setPdf] = useState<any>(null);
  const [PageFlip, setPageFlip] = useState<any>(null);
  const pageImages = useRef<string[]>([]);
  const flipInstance = useRef<any>(null);

  // Render all PDF pages to images
  useEffect(() => {
    let isMounted = true;
    (async () => {
      if (!file) return;
      const loadingTask = pdfjs.getDocument(file);
      const loadedPdf = await loadingTask.promise;
      if (!isMounted) return;
      setPdf(loadedPdf);
      setNumPages(loadedPdf.numPages);
      const images: string[] = [];
      for (let i = 1; i <= loadedPdf.numPages; i++) {
        const page = await loadedPdf.getPage(i);
        const viewport = page.getViewport({ scale: 1.5 });
        const canvas = document.createElement('canvas');
        const context = canvas.getContext('2d');
        canvas.width = viewport.width;
        canvas.height = viewport.height;
        await page.render({ canvasContext: context!, viewport }).promise;
        images.push(canvas.toDataURL());
      }
      pageImages.current = images;
    })();
    return () => { isMounted = false; };
  }, [file]);

  // Init StPageFlip
  useEffect(() => {
    if (!containerRef.current || !pageImages.current.length) return;
    let flip: any;
    loadStPageFlip().then(({ PageFlip }) => {
      flip = new PageFlip(containerRef.current, {
        width: 400,
        height: 566,
        size: 'fixed',
        minWidth: 315,
        maxWidth: 1000,
        minHeight: 420,
        maxHeight: 1536,
        maxShadowOpacity: 0.5,
        showCover: false,
        mobileScrollSupport: true,
        usePortrait: false,
        drawShadow: true,
        flippingTime: 700,
      });
      flip.loadFromImages(pageImages.current);
      flipInstance.current = flip;
    });
    return () => {
      if (flipInstance.current) {
        flipInstance.current.destroy();
        flipInstance.current = null;
      }
    };
  }, [pageImages.current.length]);

  return (
    <div style={{ minHeight: '80vh', background: '#f3f4f6' }}>
      <PdfFlipbookSpread file={file} />
    </div>
  );
}
