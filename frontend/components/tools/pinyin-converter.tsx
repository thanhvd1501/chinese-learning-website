'use client'

import { useState } from 'react'
import { m } from 'framer-motion'
import { Button } from '../../components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card'
import { Input } from '../../components/ui/input'

export function PinyinConverter() {
  const [input, setInput] = useState('')
  const [output, setOutput] = useState('')
  const [loading, setLoading] = useState(false)

  const handleConvert = async () => {
    if (!input.trim()) return
    
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/api/tools/pinyin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ input: input.trim() }),
      })
      
      const data = await response.json()
      setOutput(data.output || '')
    } catch (error) {
      console.error('Error converting pinyin:', error)
      setOutput('Lỗi khi chuyển đổi')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card className="max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>Nhập từ cần chuyển đổi. Ví dụ: pin4yin1</CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="space-y-2">
          <label className="text-sm font-medium">Input:</label>
          <Input
            placeholder="pin4yin1"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            className="font-mono"
          />
        </div>
        
        <m.div
          whileTap={{ scale: 0.98 }}
          className="flex justify-center"
        >
          <Button
            onClick={handleConvert}
            disabled={loading || !input.trim()}
            className="btn-ripple bg-accent hover:bg-accent/90 text-white px-8 py-2"
          >
            {loading ? 'Đang chuyển đổi...' : 'Convert Pinyin'}
          </Button>
        </m.div>
        
        {output && (
          <m.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-2"
          >
            <label className="text-sm font-medium">Output:</label>
            <div className="p-4 bg-muted rounded-md font-mono text-lg">
              {output}
            </div>
          </m.div>
        )}
        
        <div className="text-center text-sm text-muted-foreground">
          by chinese.edu.vn
        </div>
      </CardContent>
    </Card>
  )
}
