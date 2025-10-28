'use client'

import { useState, useEffect } from 'react'
import { FadeIn } from '../../components/motion/fade-in'
import { Breadcrumb } from '../../components/nav/breadcrumb'
import { VocabTable } from '../../components/table/vocab-table'
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card'

export default function TuVungPage() {
  return (
    <div className="mx-auto max-w-6xl px-6 py-10">
      <Breadcrumb items={[{ label: 'Từ vựng' }]} />
      
      <FadeIn>
        <Card className="mb-8">
          <CardHeader>
            <CardTitle className="text-2xl">1000 Từ vựng Cơ bản trong tiếng Trung</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-muted-foreground">
              Học thuộc 1000 từ vựng Giản thể và Phồn thể (Đài Loan) này là có thể ghép câu 1 cách đơn giản.
            </p>
          </CardContent>
        </Card>
      </FadeIn>

      <FadeIn delay={0.1}>
        <VocabTable />
      </FadeIn>
    </div>
  )
}
