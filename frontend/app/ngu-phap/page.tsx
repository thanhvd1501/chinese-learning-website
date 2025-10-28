'use client'

import { FadeIn } from '../../components/motion/fade-in'
import { StaggerList} from '../../components/motion/stagger-list'
import { StaggerItem} from '../../components/motion/stagger-list'
import { HoverCard } from '../../components/motion/hover-card'
import { Breadcrumb } from '../../components/nav/breadcrumb'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../../components/ui/card'

export default function NguPhapPage() {
  const grammarTopics = [
    'Các loại từ trong tiếng Trung',
    'Kết cấu cú pháp câu',
    'Thành phần ngữ pháp Hán Ngữ',
    'Các kiểu câu trong tiếng trung',
    'Cấu trúc ngữ pháp sơ cấp',
    'Mẫu cấu trúc ngữ pháp Hán ngữ cố định',
    'Lượng từ trong tiếng Trung',
    'Sách ngữ pháp tiếng Trung cơ bản'
  ]

  return (
    <div className="mx-auto max-w-6xl px-6 py-10">
      <Breadcrumb items={[{ label: 'Ngữ pháp' }]} />
      
      <FadeIn>
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-4">Ngữ pháp tiếng Trung</h1>
          <p className="text-muted-foreground">
            Trong ngữ pháp tiếng Trung bao gồm các thành phần như dưới đây:
          </p>
        </div>
      </FadeIn>

      <StaggerList>
        <Card className="mb-8">
          <CardHeader>
            <CardTitle>Chủ đề ngữ pháp</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="space-y-2">
              {grammarTopics.map((topic, index) => (
                <StaggerItem key={index}>
                  <li className="flex items-center">
                    <div className="w-2 h-2 bg-accent rounded-full mr-3" />
                    <span>{topic}</span>
                  </li>
                </StaggerItem>
              ))}
            </ul>
          </CardContent>
        </Card>
      </StaggerList>

      <FadeIn delay={0.2}>
        <HoverCard>
          <Card>
            <CardHeader>
              <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                <span>Du Bao Ying</span>
                <span>•</span>
                <span>12 Tháng 10, 2024</span>
              </div>
              <CardTitle className="text-lg">Đại từ trong tiếng Trung: Hướng dẫn và Ví dụ mới nhất 2025</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>
                Các đại từ cơ bản trong tiếng Trung: 我 (wǒ): tôi, tớ, chúng tôi; 你 (nǐ): bạn, cậu, anh/chị; 
                他 (tā): anh ấy, cậu ấy, ông ấy; 她 (tā): cô ấy, bà ấy; 它 (tā): nó.
              </CardDescription>
            </CardContent>
          </Card>
        </HoverCard>
      </FadeIn>
    </div>
  )
}
