"use client"

import React from "react"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { X } from "lucide-react"
import { Button } from "../../components/ui/button"
import { Input } from "../../components/ui/input"

export default function RegisterPage() {
  const router = useRouter()

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center" style={{ background: 'rgba(30,32,38,0.45)', backdropFilter: 'blur(2px)'}}>
      <div
        className="relative w-full max-w-[520px] mx-auto bg-white rounded-2xl shadow-2xl border border-gray-100"
        style={{ borderRadius: '1.25rem', overflow: 'hidden' }}
      >
        {/* header */}
        <div className="flex items-center justify-between px-6 pt-6 pb-2 bg-white border-b border-gray-100">
          <h3 className="text-xl font-bold text-gray-800">Đăng ký</h3>
          <button
            aria-label="Đóng"
            className="w-9 h-9 flex items-center justify-center rounded-full bg-[#f1f3f6] hover:bg-[#e6e9ee] transition"
            onClick={() => router.back()}
          >
            <X className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* body */}
        <div className="px-6 pb-8 pt-6 bg-white">
          <div className="space-y-4">
            <button className="w-full flex items-center justify-center gap-2 border border-gray-200 rounded-lg py-3 text-sm text-gray-700 hover:bg-gray-50 transition">
              <svg width="18" height="18" viewBox="0 0 48 48" className="block" aria-hidden>
                <path fill="#EA4335" d="M24 9.5c3.9 0 7 1.4 9.2 3.3l6.8-6.8C35.7 2.6 30.2 0 24 0 14.7 0 6.9 5.7 3.2 13.9l7.9 6.1C12.6 14.2 17.7 9.5 24 9.5z"/>
                <path fill="#34A853" d="M46.5 24c0-1.6-.2-3.1-.5-4.5H24v8.5h12.9c-.6 3.3-2.5 6.1-5.3 7.9l8 6.1C43.7 38.1 46.5 31.6 46.5 24z"/>
                <path fill="#4A90E2" d="M10.1 30.1A14.7 14.7 0 0124 38c3.6 0 6.9-1.1 9.6-3l-8-6.1c-1.9 1.3-4.3 2-6.6 2a11.7 11.7 0 01-9-4.8l-7.9 6.1c2.5 4.1 6.8 7.1 12 7.9z"/>
                <path fill="#FBBC05" d="M3.2 13.9L10.1 20c1.6-3.8 5.3-6.5 9.9-6.5 2.1 0 4 .6 5.6 1.6l8-6.1C30.9 2.6 27.1 1.5 24 1.5 17.7 1.5 12.6 6.2 10.1 13.9z"/>
              </svg>
              <span>Đăng ký với Google</span>
            </button>

            <div className="flex items-center gap-3 text-sm text-gray-400">
              <span className="flex-1 h-px bg-gray-200" />
              <span>hoặc</span>
              <span className="flex-1 h-px bg-gray-200" />
            </div>

            <div className="space-y-3">
              <label className="text-sm text-gray-700 font-medium">Email</label>
              <Input placeholder="Nhập email của bạn" />

              <label className="text-sm text-gray-700 font-medium">Mật khẩu</label>
              <Input placeholder="Nhập mật khẩu" type="password" />

              <label className="text-sm text-gray-700 font-medium">Xác nhận mật khẩu</label>
              <Input placeholder="Nhập lại mật khẩu" type="password" />
            </div>

            <div className="pt-1">
              <Button className="w-full bg-gradient-to-r from-[#36d1c4] via-[#5b86e5] to-[#f857a6] text-white">Đăng ký</Button>
            </div>

            <div className="text-center text-sm text-gray-500">
              Đã có tài khoản? <Link href="/login" className="text-blue-500 font-medium">Đăng nhập</Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
