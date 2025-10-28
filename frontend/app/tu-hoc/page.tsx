'use client'

import { useState } from 'react'
import { FadeIn } from '../../components/motion/fade-in'
import { Breadcrumb } from '../../components/nav/breadcrumb'
import { PinyinConverter } from '../../components/tools/pinyin-converter'
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card'

export default function TuHocPage() {
  return (
    <div className="mx-auto max-w-4xl px-6 py-10">
      <Breadcrumb items={[{ label: 'Tự học' }]} />
      
      <FadeIn>
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-4">Công cụ chuyển đổi Pinyin</h1>
          <p className="text-muted-foreground">
            Đây là công cụ để chuyển bính âm có chứa số âm (pin4yin1) sang bính âm có dấu âm (pinyīn) [pin4yin1 → pinyīn]
          </p>
        </div>
      </FadeIn>

      <FadeIn delay={0.1}>
        <PinyinConverter />
      </FadeIn>
    </div>
  )
}
