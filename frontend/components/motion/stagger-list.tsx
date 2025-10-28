'use client'

import { m } from 'framer-motion'
import { ReactNode } from 'react'

interface StaggerListProps {
  children: ReactNode
  staggerDelay?: number
}

export function StaggerList({ children, staggerDelay = 0.06 }: StaggerListProps) {
  return (
    <m.div
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true, amount: 0.15 }}
      variants={{
        hidden: { opacity: 0 },
        visible: {
          opacity: 1,
          transition: {
            staggerChildren: staggerDelay
          }
        }
      }}
    >
      {children}
    </m.div>
  )
}

export function StaggerItem({ children }: { children: ReactNode }) {
  return (
    <m.div
      variants={{
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0 }
      }}
      transition={{ duration: 0.25, ease: 'easeOut' }}
    >
      {children}
    </m.div>
  )
}
