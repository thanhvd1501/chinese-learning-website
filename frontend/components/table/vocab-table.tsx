'use client'

import { useState, useEffect, useMemo } from 'react'
import { m } from 'framer-motion'
import { Search, Filter } from 'lucide-react'
import { Input } from '../../components/ui/input'
import { Button } from '../../components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../../components/ui/table'

interface Vocabulary {
  id: number
  hanzi: string
  pinyin: string
  nghia: string
  viDu?: string
  bienThe: 'GIAN' | 'PHON' | 'BOTH'
  tags: string[]
}

interface VocabResponse {
  content: Vocabulary[]
  totalElements: number
  totalPages: number
  currentPage: number
  size: number
}

export function VocabTable() {
  const [vocab, setVocab] = useState<Vocabulary[]>([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [filter, setFilter] = useState<string>('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)

  const fetchVocab = async () => {
    setLoading(true)
    try {
      const params = new URLSearchParams({
        page: page.toString(),
        size: '20',
        ...(search && { search }),
        ...(filter && { bienThe: filter })
      })
      
      const response = await fetch(`http://localhost:8080/api/vocab?${params}`)
      const data: VocabResponse = await response.json()
      
      setVocab(data.content)
      setTotalPages(data.totalPages)
      setTotalElements(data.totalElements)
    } catch (error) {
      console.error('Error fetching vocab:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchVocab()
  }, [page, search, filter])

  const debouncedSearch = useMemo(() => {
    const timer = setTimeout(() => {
      setSearch(search)
    }, 300)
    return () => clearTimeout(timer)
  }, [search])

  useEffect(() => {
    debouncedSearch()
  }, [search, debouncedSearch])

  if (loading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Đang tải...</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            {[...Array(5)].map((_, i) => (
              <div key={i} className="h-4 bg-muted animate-pulse rounded" />
            ))}
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Từ vựng Cơ bản từ 1 – 1000</CardTitle>
        <div className="flex gap-4 items-center">
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Tìm kiếm từ vựng..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="pl-10"
            />
          </div>
          <Button
            variant="outline"
            onClick={() => setFilter(filter === 'GIAN' ? '' : 'GIAN')}
            className={filter === 'GIAN' ? 'bg-accent text-white' : ''}
          >
            <Filter className="h-4 w-4 mr-2" />
            Giản thể
          </Button>
          <Button
            variant="outline"
            onClick={() => setFilter(filter === 'PHON' ? '' : 'PHON')}
            className={filter === 'PHON' ? 'bg-accent text-white' : ''}
          >
            <Filter className="h-4 w-4 mr-2" />
            Phồn thể
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>STT</TableHead>
              <TableHead>Chữ viết</TableHead>
              <TableHead>Phiên âm + Nghĩa</TableHead>
              <TableHead>Ví dụ</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {vocab.map((item, index) => (
              <m.tr
                key={item.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.05 }}
                className="hover:bg-muted/50 transition-colors"
              >
                <TableCell>{page * 20 + index + 1}</TableCell>
                <TableCell className="font-medium">{item.hanzi}</TableCell>
                <TableCell>
                  <div>
                    <div className="font-mono text-sm">{item.pinyin}</div>
                    <div className="text-sm text-muted-foreground">{item.nghia}</div>
                  </div>
                </TableCell>
                <TableCell className="text-sm text-muted-foreground">
                  {item.viDu || '-'}
                </TableCell>
              </m.tr>
            ))}
          </TableBody>
        </Table>
        
        <div className="flex items-center justify-between mt-6">
          <div className="text-sm text-muted-foreground">
            Hiển thị {page * 20 + 1}-{Math.min((page + 1) * 20, totalElements)} của {totalElements} từ vựng
          </div>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage(Math.max(0, page - 1))}
              disabled={page === 0}
            >
              Trước
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
              disabled={page >= totalPages - 1}
            >
              Sau
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
