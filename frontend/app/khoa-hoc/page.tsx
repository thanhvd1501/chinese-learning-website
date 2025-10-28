'use client'

import React, { useEffect, useRef, useState } from 'react'
import { ArrowRight } from 'lucide-react'

export default function KhoaHocPage() {
  const [messages, setMessages] = useState<{ from: 'user' | 'bot', text: string }[]>([
    { from: 'bot', text: 'Xin chào! Tôi là AI trợ lý kiểm tra ngữ pháp tiếng Trung. Hãy gửi câu cần kiểm tra nhé!' }
  ])
  const [value, setValue] = useState('')
  const listRef = useRef<HTMLDivElement | null>(null)

  useEffect(() => {
    listRef.current?.scrollTo({ top: listRef.current.scrollHeight, behavior: 'smooth' })
  }, [messages])

  function send() {
    if (!value.trim()) return
    const text = value.trim()
    setMessages(m => [...m, { from: 'user', text }])
    setValue('')
    // mock bot reply after a short delay
    setTimeout(() => {
      setMessages(m => [...m, { from: 'bot', text: 'Đã nhận: ' + text + ' — (ví dụ phản hồi kiểm tra ngữ pháp)' }])
    }, 900)
  }

  function onKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      send()
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 py-10">
      <div className="max-w-xl mx-auto">
  <div className="bg-gradient-to-r from-[#06b6d4] to-[#3b82f6] rounded-t-2xl text-white p-6">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm opacity-90">AI Kiểm tra ngữ pháp</div>
              <div className="text-xs opacity-90">Free - 3 tin/ngày</div>
            </div>
            <div className="text-sm opacity-90">3/3</div>
          </div>
        </div>

        <div className="bg-white rounded-b-2xl shadow-lg overflow-hidden">
          <div className="p-6 border-b border-gray-100 flex flex-col items-center">
            <div className="w-14 h-14 rounded-full bg-gradient-to-r from-[#06b6d4] to-[#3b82f6] flex items-center justify-center text-white mb-3 text-xl">🤖</div>
            <h3 className="text-xl font-semibold">Xin chào! 🤖</h3>
            <p className="text-sm text-gray-500 text-center">Tôi là AI trợ lý kiểm tra ngữ pháp tiếng Trung. Hãy gửi câu cần kiểm tra nhé!</p>
            <div className="mt-4 w-full bg-blue-50 border border-blue-100 text-blue-700 text-sm rounded p-3">Bạn có thể gửi 3 tin nhắn mỗi ngày. Còn lại: 3 tin.</div>
          </div>

          <div ref={listRef} className="p-6 h-96 overflow-auto space-y-4">
            {messages.map((m, i) => (
              <div key={i} className={m.from === 'bot' ? 'flex items-start gap-3' : 'flex items-end justify-end'}>
                {m.from === 'bot' && <div className="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center">🤖</div>}
                <div className={m.from === 'bot' ? 'bg-gray-100 text-gray-800 p-3 rounded-lg max-w-[78%]' : 'bg-[#3b82f6] text-white p-3 rounded-lg max-w-[78%]'}>
                  {m.text}
                </div>
                {m.from === 'user' && <div className="w-8 h-8 rounded-full bg-[#3b82f6] flex items-center justify-center text-white">Bạn</div>}
              </div>
            ))}
          </div>

          <div className="p-6 border-t border-gray-100">
            <div className="flex items-center gap-3">
              <input
                value={value}
                onChange={e => setValue(e.target.value)}
                onKeyDown={onKeyDown}
                placeholder="Nhập câu tiếng Trung cần kiểm tra..."
                className="flex-1 border-2 border-[#dbeafe] rounded-full px-5 py-3 focus:outline-none focus:ring-2 focus:ring-[#60a5fa]"
              />
              <button onClick={send} className="w-12 h-12 rounded-full bg-gradient-to-r from-[#06b6d4] to-[#3b82f6] flex items-center justify-center text-white">
                <ArrowRight className="w-5 h-5" />
              </button>
            </div>
            <div className="text-xs text-gray-400 mt-2">Nhấn Enter để gửi, Shift+Enter để xuống dòng</div>
          </div>
        </div>
      </div>
    </div>
  )
}
