'use client'

import { FadeIn } from '../components/motion/fade-in'
import { useState, useEffect } from 'react'
import { StaggerList, StaggerItem } from '../components/motion/stagger-list'
import { HoverCard } from '../components/motion/hover-card'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import React from 'react'

export function HomePage() {
  return (
    <>
      {/* Hero Section */}
      <section className="w-full bg-[#f5f7fa] py-20">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-center gap-12 px-6">
          {/* Left: Text */}
          <div className="flex-1 max-w-xl">
            <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 leading-tight mb-2">
              Học tiếng Trung hiệu quả
            </h1>
            <h2 className="text-3xl md:text-4xl font-extrabold text-[#44a6fa] leading-tight mb-6">
              cùng phương pháp hấp dẫn
            </h2>
            <p className="text-lg text-gray-700 mb-8">
              Khóa học tiếng Trung toàn diện từ cơ bản đến nâng cao, theo chuẩn quốc tế với hệ thống bài học phong phú và tương tác
            </p>
            <button className="bg-[#44a6fa] hover:bg-[#2196f3] text-white font-semibold px-8 py-3 rounded-lg shadow transition text-lg">
              Bắt đầu học ngay
            </button>
          </div>
          {/* Right: Slider */}
          <div className="flex-1 flex justify-center">
            <SliderHero />
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <StatsSection vocab={5000} radicals={300} conversations={1000} readings={500} />

      {/* Bài viết mới */}
      <div className="mx-auto max-w-6xl px-6 py-10">
        <FadeIn>
          <h1 className="text-3xl font-semibold mb-6">Bài viết mới</h1>
        </FadeIn>
        <StaggerList>
          <div className="grid md:grid-cols-3 gap-6">
            {[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15].map(i => (
              <StaggerItem key={i}>
                <HoverCard>
                  <Card className="cursor-pointer">
                    <CardHeader>
                      <div className="text-sm text-muted-foreground">Du Bao Ying • 2025</div>
                      <CardTitle className="text-lg">Bài viết {i}</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <CardDescription>Nội dung tóm tắt bài viết mẫu.</CardDescription>
                    </CardContent>
                  </Card>
                </HoverCard>
              </StaggerItem>
            ))}
          </div>
        </StaggerList>
      </div>
    </>
  )
}

// SliderHero tách ngoài cùng file
function SliderHero() {
  const slides = [
    {
      icon: '汉',
      title: 'HSK 1-6',
      desc: 'Đầy đủ 6 cấp độ',
    },
    {
      icon: '中',
      title: 'Từ vựng',
      desc: 'Học 5000+ từ vựng',
    },
    {
      icon: '文',
      title: 'Ngữ pháp',
      desc: 'Nắm chắc cấu trúc',
    },
  ];
  const [current, setCurrent] = useState(0);
  const [direction, setDirection] = useState(1); // 1: next, -1: prev
  const prev = () => {
    setDirection(-1);
    setCurrent((c) => (c === 0 ? slides.length - 1 : c - 1));
  };
  const next = () => {
    setDirection(1);
    setCurrent((c) => (c === slides.length - 1 ? 0 : c + 1));
  };

  useEffect(() => {
    const timer = setInterval(() => {
      setDirection(1);
      setCurrent((c) => (c === slides.length - 1 ? 0 : c + 1));
    }, 3000);
    return () => clearInterval(timer);
  }, [slides.length]);

  return (
    <div className="relative bg-white rounded-2xl shadow-2xl px-16 py-12 flex flex-col items-center min-w-[280px] max-w-md transition-all duration-300 overflow-hidden">
      {/* Nút trái */}
      <button
        onClick={prev}
        className="absolute left-2 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center rounded-full bg-white/60 border border-[#dbeafe] hover:bg-white hover:border-[#44a6fa] text-[#90caf9] hover:text-[#2196f3] shadow transition z-10 group backdrop-blur-sm"
        aria-label="Trước"
      >
        <svg width="25" height="25" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className="group-hover:scale-110 transition-transform duration-150"><polyline points="18 24 9 14 18 4" /></svg>
      </button>
      {/* Nút phải */}
      <button
        onClick={next}
        className="absolute right-2 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center rounded-full bg-white/60 border border-[#dbeafe] hover:bg-white hover:border-[#44a6fa] text-[#90caf9] hover:text-[#2196f3] shadow transition z-10 group backdrop-blur-sm"
        aria-label="Sau"
      >
        <svg width="25" height="25" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className="group-hover:scale-110 transition-transform duration-150"><polyline points="10 4 19 14 10 24" /></svg>
      </button>
      <div className="relative w-full h-40 flex items-center justify-center">
        <div
          key={current}
          className={`absolute inset-0 flex flex-col items-center justify-center transition-all duration-500
            ${direction === 1 ? 'animate-slide-next' : 'animate-slide-prev'}`}
        >
          <div className="text-[#44a6fa] text-7xl font-bold mb-4 select-none">{slides[current].icon}</div>
          <div className="text-2xl font-bold text-gray-900 mb-2">{slides[current].title}</div>
          <div className="text-gray-700 text-base mb-4">{slides[current].desc}</div>
        </div>
      </div>
      <div className="flex gap-1 mt-4">
        {slides.map((_, i) => (
          <span key={i} className={`w-2 h-2 rounded-full ${i === current ? 'bg-[#44a6fa]' : 'bg-gray-200'}`}></span>
        ))}
      </div>
    </div>
  );
}

// Thêm Tailwind animation vào tailwind.config.js:
// theme: { extend: { keyframes: { 'slide-next': { '0%': { opacity:0, transform:'translateX(60%) rotateY(60deg)' }, '100%': { opacity:1, transform:'translateX(0) rotateY(0)' } }, 'slide-prev': { '0%': { opacity:0, transform:'translateX(-60%) rotateY(-60deg)' }, '100%': { opacity:1, transform:'translateX(0) rotateY(0)' } } }, animation: { 'slide-next': 'slide-next 0.5s cubic-bezier(.4,0,.2,1)', 'slide-prev': 'slide-prev 0.5s cubic-bezier(.4,0,.2,1)' } } }

function StatsSection({ vocab, radicals, conversations, readings }: { vocab: number, radicals: number, conversations: number, readings: number }) {
  const ref = React.useRef<HTMLElement | null>(null)
  const [visible, setVisible] = React.useState(false)

  React.useEffect(() => {
    if (!ref.current) return
    const obs = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        setVisible(true)
        obs.disconnect()
      }
    }, { threshold: 0.35 })
    obs.observe(ref.current)
    return () => obs.disconnect()
  }, [])

  function useCount(to: number, duration = 1200) {
    const [n, setN] = React.useState(0)
    React.useEffect(() => {
      if (!visible) return
      let raf = 0
      let start: number | null = null
      const step = (ts: number) => {
        if (start === null) start = ts
        const progress = Math.min((ts - start) / duration, 1)
        const current = Math.floor(to * progress)
        setN(current)
        if (progress < 1) raf = requestAnimationFrame(step)
      }
      raf = requestAnimationFrame(step)
      return () => cancelAnimationFrame(raf)
    }, [to, duration, visible])
    return n
  }

  const v1 = useCount(vocab)
  const v2 = useCount(radicals)
  const v3 = useCount(conversations)
  const v4 = useCount(readings)

  return (
    <section
      ref={ref}
      className="w-full py-16"
      style={{
        // updated to teal/blue gradient (no purple)
        background: 'linear-gradient(90deg, #06b6d4 0%, #3b82f6 45%, #0ea5a4 100%)',
        backgroundSize: '200% 200%',
        animation: 'gradientShift 14s ease infinite'
      }}
    >
      <style>{`@keyframes popIn { 0% { transform: scale(.92); opacity: 0 } 70% { transform: scale(1.02); opacity: 1 } 100% { transform: scale(1); } } @keyframes jump { 0% { transform: translateY(0); } 25% { transform: translateY(-8px); } 50% { transform: translateY(0); } 75% { transform: translateY(-4px); } 100% { transform: translateY(0); } } @keyframes wave { 0% { transform: translateY(0); } 20% { transform: translateY(-12px); } 40% { transform: translateY(0); } 60% { transform: translateY(-6px); } 100% { transform: translateY(0); } }`}</style>
      <div className="max-w-7xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8 px-4 md:px-8 text-center">
        <div className="flex flex-col items-center">
          <div
            className={`text-5xl md:text-6xl font-extrabold text-white drop-shadow ${visible ? 'animate-[popIn_700ms_ease]' : ''}`}
            style={visible ? { animationDelay: '0s', animation: 'jump 1200ms cubic-bezier(.2,.8,.2,1) 0s infinite' } : undefined}
          >{v1.toLocaleString()}+</div>
          <div className="text-lg md:text-xl font-semibold text-white/90 mt-2">Từ vựng HSK</div>
        </div>
        <div className="flex flex-col items-center">
          <div
            className={`text-5xl md:text-6xl font-extrabold text-white drop-shadow ${visible ? 'animate-[popIn_700ms_ease_150ms]' : ''}`}
            style={visible ? { animationDelay: '150ms', animation: 'jump 1200ms cubic-bezier(.2,.8,.2,1) 150ms infinite' } : undefined}
          >{v2.toLocaleString()}+</div>
          <div className="text-lg md:text-xl font-semibold text-white/90 mt-2">Bộ thủ Hán tự</div>
        </div>
        <div className="flex flex-col items-center">
          <div
            className={`text-5xl md:text-6xl font-extrabold text-white drop-shadow ${visible ? 'animate-[popIn_700ms_ease_300ms]' : ''}`}
            style={visible ? { animationDelay: '300ms', animation: 'jump 1200ms cubic-bezier(.2,.8,.2,1) 300ms infinite' } : undefined}
          >{v3.toLocaleString()}+</div>
          <div className="text-lg md:text-xl font-semibold text-white/90 mt-2">Hội thoại mẫu</div>
        </div>
        <div className="flex flex-col items-center">
          <div
            className={`text-5xl md:text-6xl font-extrabold text-white drop-shadow ${visible ? 'animate-[popIn_700ms_ease_450ms]' : ''}`}
            style={visible ? { animationDelay: '450ms', animation: 'jump 1200ms cubic-bezier(.2,.8,.2,1) 450ms infinite' } : undefined}
          >{v4.toLocaleString()}+</div>
          <div className="text-lg md:text-xl font-semibold text-white/90 mt-2">Bài đọc hiểu</div>
        </div>
      </div>
    </section>
  )
}

// Contact section (id="lien-he") matching the screenshots
export function ContactSection() {
  return (
    <section id="lien-he" className="w-full bg-white">
      <div
        className="w-full py-12"
          style={{
            background: 'linear-gradient(90deg, #06b6d4 0%, #3b82f6 40%, #36D1C4 70%)',
            backgroundSize: '200% 200%',
            animation: 'gradientShift 10s ease infinite'
          }}
      >
        <div className="max-w-7xl mx-auto px-6 text-white">
          <h2 className="text-4xl font-extrabold">Liên hệ với chúng tôi</h2>
          <p className="mt-3 text-lg opacity-90">Chúng tôi luôn sẵn sàng hỗ trợ bạn trong hành trình học tiếng Hán</p>
        </div>
      </div>

      {/* Animated gradient keyframes (kept local to this component) */}
      <style>{`@keyframes gradientShift { 0%{background-position:0% 50%} 50%{background-position:100% 50%} 100%{background-position:0% 50%} }`}</style>

      <div className="max-w-7xl mx-auto px-6 py-16">

        <div className="mt-12 grid md:grid-cols-3 gap-6">
          <div className="bg-white rounded-2xl p-6 shadow">
            <h5 className="font-semibold mb-2">Email hỗ trợ</h5>
            <p className="text-sm text-gray-500">Gửi email cho chúng tôi để được hỗ trợ chi tiết</p>
            <div className="mt-4">
              <div className="bg-gray-100 px-4 py-2 rounded">hihsk.com@gmail.com</div>
            </div>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow">
            <h5 className="font-semibold mb-2">Facebook Messenger</h5>
            <p className="text-sm text-gray-500">Chat trực tiếp với đội ngũ hỗ trợ qua Messenger</p>
            <div className="mt-4">
              <button className="px-4 py-2 bg-blue-500 text-white rounded">Nhắn tin</button>
            </div>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow">
            <h5 className="font-semibold mb-2">Câu hỏi thường gặp</h5>
            <p className="text-sm text-gray-500">Tìm câu trả lời nhanh cho các vấn đề phổ biến</p>
            <div className="mt-4">
              <button className="px-4 py-2 border border-blue-300 text-blue-500 rounded">Xem FAQ</button>
            </div>
          </div>
        </div>

        <div className="mt-12 grid md:grid-cols-2 gap-8">
          <div>
            <h4 className="text-2xl font-bold mb-4">Gửi tin nhắn</h4>
            <p className="text-gray-600 mb-6">Điền thông tin bên dưới, chúng tôi sẽ liên hệ lại sớm nhất</p>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow">
            <form className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input className="border rounded px-3 py-2" placeholder="Họ và tên *" />
              <input className="border rounded px-3 py-2" placeholder="Email *" />
              <input className="border rounded px-3 py-2" placeholder="Số điện thoại" />
              <select className="border rounded px-3 py-2"><option>Chọn chủ đề</option></select>
              <textarea className="col-span-1 md:col-span-2 border rounded px-3 py-2 h-36" placeholder="Nội dung *" />
              <div className="col-span-1 md:col-span-2 flex justify-between items-center">
                <button type="submit" className="bg-[#06b6d4] text-white px-6 py-2 rounded">Gửi tin nhắn</button>
                <button type="reset" className="border px-4 py-2 rounded">Làm mới</button>
              </div>
            </form>
          </div>
        </div>

        <div className="mt-16 bg-slate-800 text-gray-100 rounded-2xl p-8 text-center shadow-lg">
          <h5 className="text-xl font-semibold">Giờ hỗ trợ</h5>
          <div className="mt-4 grid grid-cols-2 gap-4 text-sm">
            <div>
              <div className="font-bold">Thứ 2 - Thứ 6</div>
              <div className="text-sky-300 font-semibold">8:00 - 18:00</div>
            </div>
            <div>
              <div className="font-bold">Thứ 7 - Chủ nhật</div>
              <div className="text-sky-300 font-semibold">9:00 - 17:00</div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

// Render contact section at end of homepage
export default function HomePageWithContact() {
  return (
    <>
      <HomePage />
      <ContactSection />
    </>
  )
}

