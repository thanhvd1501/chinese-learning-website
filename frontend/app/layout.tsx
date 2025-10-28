import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import { ClientLayout } from './client-layout'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'Học Tiếng Trung - Chinese',
  description: 'Website học tiếng Trung hiện đại với từ vựng, ngữ pháp, giáo trình',
  openGraph: {
    title: 'Học Tiếng Trung - Chinese',
    description: 'Từ vựng, ngữ pháp, giáo trình, công cụ Pinyin',
    url: 'https://localhost:3000',
    type: 'website'
  }
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="vi">
      <head>
        <link rel="icon" href="/chinese_favicon_han.ico?v=2" />
      </head>
      <body className={`${inter.className} bg-white text-neutral-900`}>
        <ClientLayout>
          {children}
        </ClientLayout>
      </body>
    </html>
  )
}