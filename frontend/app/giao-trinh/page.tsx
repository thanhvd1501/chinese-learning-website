'use client'

import { useState, useEffect } from 'react'
import { FadeIn } from '../../components/motion/fade-in'
import { StaggerList} from '../../components/motion/stagger-list'
import { StaggerItem} from '../../components/motion/stagger-list'
import { Breadcrumb } from '../../components/nav/breadcrumb'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../../components/ui/card'
import { Button } from '../../components/ui/button'
import dynamic from 'next/dynamic'
import { CoursesSection } from '../../components/motion/CoursesSection'
const PdfFlipbookSpread = dynamic(() => import('../../components/PdfFlipbookSpread'), { ssr: false })

interface Textbook {
  id: number
  name: string
  phienBan: 'PB3' | 'MOI' | 'CU'
  namXuatBan: number
}

export default function GiaoTrinhPage() {
  const [textbooks, setTextbooks] = useState<Textbook[]>([])
  const [showFlipbook, setShowFlipbook] = useState(false)

  useEffect(() => {
    fetch('http://localhost:8080/api/textbooks')
      .then(res => res.json())
      .then(data => setTextbooks(data))
      .catch(console.error)
  }, [])

  const handleStartReading = (courseId: number) => {
    setShowFlipbook(true)
  }

  return (
    <div className="mx-auto max-w-6xl px-6 py-10">
      <Breadcrumb items={[{ label: 'Giáo trình' }]} />
      
      <FadeIn>
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-4">Giáo trình Hán ngữ 6 Quyển</h1>
          <p className="text-muted-foreground mb-4">
            Giáo trình Hán ngữ 6 quyển là tài liệu học tiếng Trung từ cơ bản đến nâng cao, 
            giúp người học thành thạo 4 kỹ năng: Nghe, Nói, Đọc, Viết.
          </p>
          <p className="text-muted-foreground">
            Hiện tại năm 2025 có 3 phiên bản của bộ giáo trình 6 quyển:
          </p>
        </div>
      </FadeIn>

      <StaggerList>
        <div className="grid md:grid-cols-3 gap-6 mb-8">
          <StaggerItem>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Phiên bản 3</CardTitle>
                <CardDescription>Xuất bản năm 2022, tái bản lần 1 năm 2024</CardDescription>
              </CardHeader>
            </Card>
          </StaggerItem>
          <StaggerItem>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Phiên bản Mới</CardTitle>
                <CardDescription>Xuất bản năm 2020, tái bản lần 8 năm 2024</CardDescription>
              </CardHeader>
            </Card>
          </StaggerItem>
          <StaggerItem>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Phiên bản Cũ</CardTitle>
                <CardDescription>Xuất bản năm 2015, tái bản năm 2024</CardDescription>
              </CardHeader>
            </Card>
          </StaggerItem>
        </div>
      </StaggerList>

      <FadeIn delay={0.2}>
        <Card className="mb-8 rounded-2xl shadow-sm">
          <CardHeader className="px-6 pt-6">
            <CardTitle>Danh sách các quyển</CardTitle>
            <CardDescription>
              Tất cả 3 phiên bản đều được chia thành 3 phần, tạo thành 6 quyển, mỗi quyển gồm 2 phần.
            </CardDescription>
          </CardHeader>
          <CardContent className="px-6 pb-6 pt-4">
            <CoursesSection onStartReading={handleStartReading} />
          </CardContent>
        </Card>
      </FadeIn>

      {showFlipbook && (
        <div className="fixed inset-0 z-[200] flex items-center justify-center bg-black/50 backdrop-blur-sm">
          <div className="relative w-[95vw] h-[95vh] max-w-7xl bg-white rounded-xl shadow-2xl overflow-hidden">
            <button
              className="absolute top-4 right-4 z-10 w-10 h-10 rounded-full bg-white/90 hover:bg-red-100 border border-gray-200 flex items-center justify-center text-gray-500 hover:text-red-500 shadow-lg transition-all duration-200"
              onClick={() => setShowFlipbook(false)}
              aria-label="Đóng"
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
            <div className="w-full h-full">
              <PdfFlipbookSpread file="/han_ngu_q1.pdf" />
            </div>
          </div>
        </div>
      )}

      <FadeIn delay={0.3}>
        <Card className="bg-yellow-50 border-yellow-200">
          <CardContent className="pt-6">
            <p className="text-sm text-yellow-800">
              <strong>Góp ý:</strong> Cách đặt tên các quyển như trên thực sự là dài dòng gây khó phân biệt cho người đọc 
              nên trong bài này và các bài viết khác trên site tôi xin rút gọn.
            </p>
          </CardContent>
        </Card>
      </FadeIn>
    </div>
  )
}
