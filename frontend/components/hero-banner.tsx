// File này chứa ảnh hero đầu trang chủ
import Image from 'next/image'

export default function HeroBanner() {
  return (
    <div className="w-full flex justify-center items-center py-8">
      <Image
        src="/hero-hsk.png"
        alt="Hero HSK Banner"
        width={1907}
        height={606}
        className="rounded-xl shadow-lg max-w-full h-auto"
        priority
      />
    </div>
  )
}
