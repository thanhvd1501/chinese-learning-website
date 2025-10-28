'use client'

import { LazyMotion, domAnimation } from 'framer-motion'
import { Header } from '../components/nav/header'
import { Footer } from '../components/footer/footer'

interface ClientLayoutProps {
  children: React.ReactNode
}

export function ClientLayout({ children }: ClientLayoutProps) {
  return (
    <LazyMotion features={domAnimation} strict>
      <Header />
      <main className="min-h-screen">
        {children}
      </main>
      <Footer />
    </LazyMotion>
  )
}
