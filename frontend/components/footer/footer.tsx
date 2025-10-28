'use client'

import { m } from 'framer-motion'
import { ArrowUp, Facebook, Mail, Phone } from 'lucide-react'
import { Button } from '../ui/button'

export function Footer() {
  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <footer className="bg-[#1877f2] text-white">
  <div className="max-w-6xl mx-auto px-6 py-12">
        <div className="grid md:grid-cols-4 gap-8">
          {/* Trung tâm tiếng Trung Chinese */}
          <div>
            <h3 className="font-bold text-lg mb-4">Trung tâm tiếng Trung Chinese</h3>
            <div className="space-y-2 text-sm">
              <p>Ngõ 79 Mễ Trì Thượng, Hà Nội</p>
              <p>10 Ngách 22, Nam Từ Liêm, Hà Nội</p>
              <div className="flex items-center space-x-2">
                <Phone className="h-4 w-4" />
                <span>(024) 6668.1234</span>
              </div>
              <div className="flex items-center space-x-2">
                <Phone className="h-4 w-4" />
                <span>0989 543 912</span>
              </div>
              <div className="flex items-center space-x-2">
                <Mail className="h-4 w-4" />
                <a href="mailto:hanoi@chinese.edu.vn" className="text-blue-100 hover:text-white">
                  hanoivietnam@chinese.edu.vn
                </a>
              </div>
              <div className="flex items-center space-x-2">
                <Facebook className="h-4 w-4" />
                <a href="https://fb.com/hanoichinese.edu.vn" className="text-blue-100 hover:text-white">
                  fb.com/hanoichinese.edu.vn
                </a>
              </div>
            </div>
          </div>

          {/* Chinese Tp. HCM và Quảng Ninh */}
          <div>
            <h3 className="font-bold text-lg mb-4">Chinese TP. Hà Nội và Tỉnh Ninh Bình</h3>
            <div className="space-y-4 text-sm">
              <div>
                <p className="font-medium">CN1: Tổ 1, Phường Nguyễn Úy, tỉnh Ninh Bình</p>
                <div className="flex items-center space-x-2 mt-1">
                  <Phone className="h-4 w-4" />
                  <span>0348 528 328</span>
                </div>
                <div className="flex items-center space-x-2 mt-1">
                  <Facebook className="h-4 w-4" />
                  <span>TiengTrungNinhBinh</span>
                </div>
              </div>
              <div>
                <p className="font-medium">CN2: Ngõ 79 Mễ Trì Thượng, Nam Từ Liêm, Hà Nội</p>
                <div className="flex items-center space-x-2 mt-1">
                  <Phone className="h-4 w-4" />
                  <span>(028) 3894.3896</span>
                </div>
                <div className="flex items-center space-x-2 mt-1">
                  <Phone className="h-4 w-4" />
                  <span>0936.2345.04</span>
                </div>
              </div>
            </div>
          </div>

          {/* Trung tâm Ngoại ngữ PopodooKids */}
          <div>
            <h3 className="font-bold text-lg mb-4">Trung tâm Ngoại ngữ HanZiEdu</h3>
            <div className="space-y-2 text-sm">
              <p>Tổ 1, Phường Nguyễn Úy, Tỉnh Ninh Bình</p>
              <div className="flex items-center space-x-2">
                <Phone className="h-4 w-4" />
                <span>0912 347 782</span>
              </div>
              <div className="flex items-center space-x-2">
                <Mail className="h-4 w-4" />
                <a href="mailto:thinhlong@popodookids.com" className="text-blue-100 hover:text-white">
                  thanhvu@hanziedu.com
                </a>
              </div>
              <div className="flex items-center space-x-2">
                <span>hanziedu.com</span>
              </div>
              <div className="flex items-center space-x-2">
                <Facebook className="h-4 w-4" />
                <span>trungtamanhnguhanziedu</span>
              </div>
            </div>
          </div>

          {/* Hệ thống website học tiếng Trung */}
          <div>
            <h3 className="font-bold text-lg mb-4">Hệ thống website học tiếng Trung</h3>
            <div className="space-y-2 text-sm">
              <a href="/giao-trinh" className="block text-blue-100 hover:text-white">
                Giáo trình tiếng Trung
              </a>
              <a href="/audio" className="block text-blue-100 hover:text-white">
                Audio tiếng Trung
              </a>
              <a href="/bai-hat" className="block text-blue-100 hover:text-white">
                Bài hát tiếng Trung
              </a>
              <a href="/thi-thu" className="block text-blue-100 hover:text-white">
                Thi thử Online
              </a>
            </div>
          </div>
        </div>
      </div>
      
      {/* Copyright bar */}
  <div className="bg-[#145db2]">
        <div className="max-w-6xl mx-auto px-6 py-4 text-center text-sm">
          ©2010-2025 Trung tâm tiếng Trung Chinese. All Rights Reserved.
        </div>
      </div>

      {/* Scroll to top button */}
      <m.div
        initial={{ opacity: 0, scale: 0 }}
        animate={{ opacity: 1, scale: 1 }}
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        className="fixed bottom-6 right-6 z-50"
      >
        <Button
          onClick={scrollToTop}
          size="icon"
          className="h-12 w-12 rounded-full bg-white hover:bg-blue-100 shadow-lg border border-[#1877f2]"
        >
          <ArrowUp className="h-6 w-6 text-[#1877f2]" />
        </Button>
      </m.div>
    </footer>
  )
}
