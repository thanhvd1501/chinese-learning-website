'use client'

import { useState, useRef, useEffect } from 'react'
import { usePathname } from 'next/navigation'
import Link from 'next/link'
import { m } from 'framer-motion'
import { Menu, X, ChevronDown, Home, BookOpen, FileText, FileStack, BookMarked, GraduationCap, Layers, FileArchive } from 'lucide-react'
import { Button } from '../ui/button'
import dynamic from 'next/dynamic'

const AuthModal = dynamic(() => import('./AuthModal'))

export function Header() {
  const pathname = usePathname();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)
  const [activeDropdown, setActiveDropdown] = useState<string | null>(null)
  const [showSearch, setShowSearch] = useState(false)
  const searchInputRef = useRef<HTMLInputElement>(null)
  const [authOpen, setAuthOpen] = useState<null | 'login' | 'register'>(null)

  // Focus input when popup hiện
  useEffect(() => {
    if (showSearch && searchInputRef.current) {
      searchInputRef.current.focus();
    }
    // Prevent scroll when modal open
    if (showSearch) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [showSearch]);

  const navItems = [
    { label: 'Trang chủ', href: '/', icon: <Home className="w-5 h-5 mr-1" /> },
    { label: 'HSK', href: '/hsk', icon: <GraduationCap className="w-5 h-5 mr-1" /> },
    { 
      label: 'Giáo trình', 
      href: '/giao-trinh',
      icon: <BookOpen className="w-5 h-5 mr-1" />, 
      dropdown: [
        { label: 'Giáo trình Hán ngữ 6 Quyển', href: '/giao-trinh/han-ngu-6-quyen' },
        { label: 'Phiên bản 3', href: '/giao-trinh/phien-ban-3' },
        { label: 'Phiên bản Mới', href: '/giao-trinh/phien-ban-moi' },
        { label: 'Phiên bản Cũ', href: '/giao-trinh/phien-ban-cu' }
      ]
    },
    { label: 'Ngữ pháp', href: '/ngu-phap', icon: <FileText className="w-5 h-5 mr-1" /> },
    { label: 'Từ vựng', href: '/tu-vung', icon: <Layers className="w-5 h-5 mr-1" /> },
    { label: 'Tự học', href: '/tu-hoc', icon: <BookMarked className="w-5 h-5 mr-1" /> },
    { 
      label: 'Tài liệu', 
      href: '/tai-lieu',
      icon: <FileStack className="w-5 h-5 mr-1" />, 
      dropdown: [
        { label: 'Phần mềm', href: '/tai-lieu/phan-mem' },
        { label: 'Kinh nghiệm học', href: '/tai-lieu/kinh-nghiem' },
        { label: 'Du học Trung Quốc', href: '/tai-lieu/du-hoc' },
        { label: 'Từ Hán Việt', href: '/tai-lieu/tu-han-viet' },
        { label: 'Pinyin Convert', href: '/tu-hoc' },
        { label: 'Pinyin Chart', href: '/tai-lieu/pinyin-chart' }
      ]
    },
    { label: 'Chat với AI', href: '/khoa-hoc', icon: <FileArchive className="w-5 h-5 mr-1" /> }
  ]

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/60">
      <div className="container mx-auto px-4">
        {/* Hàng trên: logo + nút */}
        <div className="flex h-16 items-center justify-between gap-4">
          {/* Logo + Title */}
          <Link href="/" className="flex items-center space-x-2 min-w-0">
            <div className="h-9 w-9 sm:h-10 sm:w-10 rounded-full bg-[#FF944D] flex items-center justify-center flex-shrink-0">
              <span className="text-white font-bold text-lg sm:text-xl">汉</span>
            </div>
            <div className="leading-tight truncate">
              <div className="font-extrabold text-base sm:text-xl text-[#2196F3] truncate">Chinese</div>
              <div className="text-xs text-gray-500 truncate">Học Tiếng Trung</div>
            </div>
          </Link>
          {/* Search, Contact, Auth */}
          <div className="flex items-center gap-1 sm:gap-2 xl:gap-4 flex-shrink-0">
            <button
              className="flex items-center px-2 sm:px-3 py-1.5 rounded-lg bg-blue-50 text-[#2196F3] hover:bg-blue-100 transition whitespace-nowrap"
              onClick={() => setShowSearch(true)}
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5 mr-0 sm:mr-1">
                <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 104.5 4.5a7.5 7.5 0 0012.15 12.15z" />
              </svg>
              <span className="hidden sm:inline text-sm">Tìm kiếm</span>
            </button>
            <button
              type="button"
              onClick={() => {
                if (typeof window === 'undefined') return
                if (pathname === '/') {
                  const el = document.getElementById('lien-he')
                  if (el) {
                    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
                    return
                  }
                }
                // fallback: navigate to homepage hash
                window.location.href = '/#lien-he'
              }}
              className="hidden sm:flex items-center px-2 sm:px-3 py-1.5 rounded-lg text-gray-600 hover:bg-blue-50 transition text-sm whitespace-nowrap"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5 mr-0 sm:mr-1">
                <path strokeLinecap="round" strokeLinejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25H4.5a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15A2.25 2.25 0 002.25 6.75m19.5 0v.243a2.25 2.25 0 01-.659 1.591l-7.091 7.091a2.25 2.25 0 01-3.182 0L3.909 8.584A2.25 2.25 0 013.25 6.993V6.75" />
              </svg>
              <span className="hidden md:inline">Liên hệ</span>
            </button>
            <button onClick={() => setAuthOpen('login')} className="px-2 sm:px-3 py-1.5 rounded-lg border border-blue-200 text-[#2196F3] hover:bg-blue-50 transition text-sm font-medium whitespace-nowrap">Đăng nhập</button>
            <button onClick={() => setAuthOpen('register')} className="px-2 sm:px-3 py-1.5 rounded-lg bg-gradient-to-r from-[#36d1c4] via-[#5b86e5] to-[#f857a6] text-white hover:opacity-90 transition text-sm font-medium whitespace-nowrap">Đăng ký</button>
          </div>
          {/* Mobile Menu Button */}
          <Button
            variant="ghost"
            size="icon"
            className="md:hidden ml-1"
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            aria-label="Mở menu"
          >
            {isMobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </Button>
        </div>
        {/* Hàng dưới: menu chính */}
        <div className="w-full flex justify-center border-t border-gray-100 bg-white">
          <nav className="hidden md:flex items-center space-x-2 xl:space-x-4 min-w-0 flex-1 justify-center">
            {navItems.map((item) => (
              <div
                key={item.href}
                className="relative flex items-center h-12"
                onMouseEnter={() => item.dropdown && setActiveDropdown(item.label)}
                onMouseLeave={() => setActiveDropdown(null)}
              >
                <Link
                  href={item.href}
                  className={
                    `flex items-center h-12 px-3 text-sm font-medium rounded-lg whitespace-nowrap transition-colors duration-200 ` +
                    (pathname === item.href || (item.href !== '/' && pathname.startsWith(item.href))
                      ? 'bg-blue-50 text-[#2196F3]' : 'text-gray-700 hover:text-[#2196F3] hover:bg-blue-50')
                  }
                  style={{paddingTop: 0, paddingBottom: 0}}
                >
                  {item.icon && item.icon}
                  <span>{item.label}</span>
                  {item.dropdown && <ChevronDown className="h-4 w-4 ml-1" />}
                </Link>
                  {item.dropdown && activeDropdown === item.label && (
                  <div
                    className="absolute left-0 mt-0 min-w-full bg-white rounded-xl shadow-2xl border border-gray-100 z-50 flex flex-col py-2"
                    style={{top: '100%'}}
                    onMouseEnter={() => setActiveDropdown(item.label)}
                    onMouseLeave={() => setActiveDropdown(null)}
                  >
                    {item.dropdown.map((dropdownItem) => (
                      <Link
                        key={dropdownItem.href}
                        href={dropdownItem.href}
                        className="px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-[#2196F3] transition-colors duration-150 whitespace-nowrap text-left"
                        style={{minWidth: 160}}
                      >
                        {dropdownItem.label}
                      </Link>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </nav>
        </div>

        {/* Search Popup */}
        {showSearch && (
          <div
            className="fixed inset-0 z-[100] flex items-center justify-center min-h-screen p-4"
            style={{ background: 'rgba(30,32,38,0.45)', backdropFilter: 'blur(2px)' }}
            onClick={() => setShowSearch(false)}
          >
            <div
              className="relative w-full max-w-[700px] mx-auto p-0"
              style={{
                maxWidth: '700px',
                maxHeight: '85vh',
                borderRadius: '1.5rem',
                overflow: 'hidden',
                boxShadow: '0 8px 32px 0 rgba(80, 80, 200, 0.18), 0 2px 8px 0 rgba(80, 80, 200, 0.10)'
              }}
              onClick={e => e.stopPropagation()}
            >
              {/* Header modal */}
              <div className="flex items-center justify-between bg-[#f4f6fa] px-6 pt-5 pb-3 border-b border-gray-200">
                <div className="font-bold text-xl text-gray-800">Tìm kiếm từ vựng</div>
                <button
                  className="w-9 h-9 flex items-center justify-center rounded-full bg-[#e9ecf2] hover:bg-[#d3d7e3] transition"
                  onClick={() => setShowSearch(false)}
                >
                  <X className="w-6 h-6 text-gray-500" />
                </button>
              </div>
              {/* Body modal */}
              <div className="backdrop-blur-md bg-white/90 px-6 pb-5 pt-4">
                <div className="relative mb-4">
                  <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
                    <svg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' strokeWidth={1.5} stroke='currentColor' className='w-5 h-5'>
                      <path strokeLinecap='round' strokeLinejoin='round' d='M21 21l-4.35-4.35m0 0A7.5 7.5 0 104.5 4.5a7.5 7.5 0 0012.15 12.15z' />
                    </svg>
                  </span>
                  <input
                    ref={searchInputRef}
                    className="w-full border border-blue-300 rounded-2xl pl-10 pr-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300 bg-white/95 placeholder:text-gray-500 text-base text-gray-800 shadow-[0_2px_8px_0_rgba(80,120,255,0.08)]"
                    style={{ fontSize: '1rem', height: '44px' }}
                    placeholder="Nhập từ vựng tiếng Trung hoặc tiếng Việt..."
                  />
                </div>
                <div className="text-sm text-gray-600 mb-2 font-semibold">Gợi ý tìm kiếm</div>
                <div className="flex flex-wrap gap-2">
                  {['你好', '谢谢', '学习', '工作', '朋友', '家人'].map((kw) => (
                    <button
                      key={kw}
                      className="px-4 py-1.5 rounded-full bg-[#f4f6fa] text-gray-600 text-base font-medium hover:bg-blue-50 transition border border-transparent hover:border-blue-200 focus:outline-none"
                      tabIndex={-1}
                      type="button"
                    >
                      {kw}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Auth Modal (rendered in-place) */}
        {authOpen && (
          <AuthModal
            mode={authOpen}
            onClose={() => setAuthOpen(null)}
            onSwitchMode={(m) => setAuthOpen(m)}
          />
        )}

        {/* Mobile Menu */}
        {isMobileMenuOpen && (
          <div className="md:hidden border-t border-border bg-white">
            <nav className="flex flex-col space-y-2 py-4">
              {navItems.map((item) => (
                <div key={item.href}>
                  <Link
                    href={item.href}
                    className="flex items-center justify-between px-4 py-2 text-sm font-medium text-gray-700 hover:text-[#2196F3] hover:bg-blue-50 transition-colors"
                    onClick={() => setIsMobileMenuOpen(false)}
                  >
                    <span>{item.label}</span>
                    {item.dropdown && <ChevronDown className="h-4 w-4" />}
                  </Link>
                  {item.dropdown && (
                    <div className="ml-4 space-y-1">
                      {item.dropdown.map((dropdownItem) => (
                        <Link
                          key={dropdownItem.href}
                          href={dropdownItem.href}
                          className="block px-4 py-1 text-sm text-gray-700 hover:text-[#2196F3] transition-colors"
                          onClick={() => setIsMobileMenuOpen(false)}
                        >
                          {dropdownItem.label}
                        </Link>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </nav>
            <div className="flex flex-col gap-2 px-4 mt-2">
              <button
                type="button"
                onClick={() => {
                  if (typeof window === 'undefined') return
                  if (pathname === '/') {
                    const el = document.getElementById('lien-he')
                    if (el) {
                      el.scrollIntoView({ behavior: 'smooth', block: 'start' })
                      setIsMobileMenuOpen(false)
                      return
                    }
                  }
                  setIsMobileMenuOpen(false)
                  window.location.href = '/#lien-he'
                }}
                className="flex items-center px-3 py-2 rounded-lg text-gray-600 hover:bg-blue-50 transition text-sm"
              >
                Liên hệ
              </button>
              <button onClick={() => { setIsMobileMenuOpen(false); setAuthOpen('login') }} className="px-3 py-2 rounded-lg border border-blue-200 text-[#2196F3] hover:bg-blue-50 transition text-sm font-medium">Đăng nhập</button>
              <button onClick={() => { setIsMobileMenuOpen(false); setAuthOpen('register') }} className="px-3 py-2 rounded-lg bg-gradient-to-r from-[#2196F3] to-[#FF4D4F] text-white hover:opacity-90 transition text-sm font-medium">Đăng ký</button>
            </div>
          </div>
        )}
      </div>
    </header>
  )
}
