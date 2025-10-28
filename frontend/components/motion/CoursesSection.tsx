// Courses section: card list with difficulty-colored chip
export function CoursesSection({ onStartReading }: { onStartReading?: (courseId: number) => void }) {
  const courses = [
    { id: 1, level: 'Hán 1', title: 'Hán 1', desc: 'Cấp độ cơ bản nhất, học 150 từ vựng thiết yếu', lessons: 15, duration: '2-3 tháng', difficulty: 'Cơ bản', img: '/han-1.png' },
    { id: 2, level: 'Hán 2', title: 'Hán 2', desc: 'Mở rộng từ vựng lên 300 từ, học ngữ pháp cơ bản', lessons: 15, duration: '3-4 tháng', difficulty: 'Cơ bản', img: '/han-2.png' },
    { id: 3, level: 'Hán 3', title: 'Hán 3', desc: 'Nâng cao với 600 từ vựng và ngữ pháp phức tạp hơn', lessons: 10, duration: '4-5 tháng', difficulty: 'Trung bình', img: '/han-3.png' },
    { id: 4, level: 'Hán 4', title: 'Hán 4', desc: 'Thành thạo 1200 từ vựng, có thể giao tiếp tự nhiên', lessons: 10, duration: '6-8 tháng', difficulty: 'Trung bình', img: '/han-4.png' },
    { id: 5, level: 'Hán 5', title: 'Hán 5', desc: 'Trình độ cao với 2500 từ vựng và văn bản phức tạp', lessons: 13, duration: '8-12 tháng', difficulty: 'Nâng cao', img: '/han-5.png' },
    { id: 6, level: 'Hán 6', title: 'Hán 6', desc: 'Cấp độ cao nhất với 5000+ từ vựng và văn học', lessons: 13, duration: '12+ tháng', difficulty: 'Chuyên gia', img: '/han-6.png' },
  ];

  const difficultyColorMap: Record<string, string> = {
    'Cơ bản': 'bg-emerald-100 text-emerald-700 border-emerald-200',
    'Trung bình': 'bg-orange-100 text-orange-700 border-orange-200',
    'Nâng cao': 'bg-indigo-100 text-indigo-700 border-indigo-200',
    'Chuyên gia': 'bg-rose-100 text-rose-700 border-rose-200',
  };
  const chipFallback = 'bg-gray-100 text-gray-700 border-gray-200';

  return (
    <section className="max-w-7xl mx-auto px-6 py-12">
      <div className="grid md:grid-cols-3 gap-6">
        {courses.map(course => (
          <div
            key={course.id}
            className="
              group relative overflow-hidden bg-white rounded-2xl
              border border-gray-300 shadow-sm p-4
              transition-all duration-200 ease-out
              hover:-translate-y-1 hover:border-sky-400 hover:ring-1 hover:ring-sky-200/70
              hover:shadow-[0_16px_48px_rgba(2,132,199,0.15),0_8px_24px_rgba(2,132,199,0.08)]
            "
          >
            {/* Lưới 2 cột: ảnh | nội dung */}
            <div className="grid grid-cols-[6rem_1fr] gap-4 items-center">
              {/* Ảnh bìa (giữa khung) */}
              <div className="w-24 h-32 self-center rounded-lg overflow-hidden bg-gray-50 border border-gray-200 flex items-center justify-center">
                <img src={course.img} alt={course.title} className="w-full h-full object-contain" />
              </div>

              {/* Nội dung phải */}
              <div className="flex flex-col">
                <div className="flex items-start justify-between">
                  <div
                    className={[
                      'inline-flex items-center gap-2 text-sm px-3 py-1 rounded-full border',
                      difficultyColorMap[course.difficulty] ?? chipFallback,
                    ].join(' ')}
                    title={`Độ khó: ${course.difficulty}`}
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none"
                      stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
                      className="opacity-80">
                      <path d="M12 2l2 4 4 .6-3 2.8.8 4L12 12l-3.8 2.4.8-4L6 6.6 10 6 12 2z" />
                    </svg>
                    <span className="font-medium">{course.level}</span>
                  </div>
                </div>

                <h3 className="text-xl font-semibold mt-3">{course.title}</h3>
                <p className="text-gray-600 mt-2">{course.desc}</p>
              </div>

              {/* HÀNG THÔNG TIN – TRẢI NGANG DƯỚI CẢ 2 CỘT */}
              <div className="col-span-2 mt-4 text-[15px] text-gray-500 flex flex items-center gap-x-2 gap-y-2">
                {/* 15 bài học */}
                <div className="inline-flex items-center gap-1">
                  <svg
                    width="16" height="16" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
                    className="text-gray-400"
                    aria-hidden="true"
                  >
                    {/* hai quyển sách chồng nhau */}
                    <rect x="3.5" y="6" width="9" height="12" rx="1.6"></rect>
                    <rect x="11.5" y="6" width="9" height="12" rx="1.6"></rect>
                  </svg>
                  <span className="whitespace-nowrap">{course.lessons} bài học</span>
                </div>

                {/* 2-3 tháng */}
                <div className="inline-flex items-center gap-1">
                  <svg
                    width="16" height="16" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
                    className="text-gray-400"
                    aria-hidden="true"
                  >
                    {/* đồng hồ tròn, kim giờ + phút */}
                    <circle cx="12" cy="12" r="9.2"></circle>
                    <path d="M12 7.5v5l3 1.8"></path>
                  </svg>
                  <span className="whitespace-nowrap">{course.duration}</span>
                </div>

                {/* Cơ bản */}
                <div className="inline-flex items-center gap-1">
                  <svg
                    width="16" height="16" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
                    className="text-gray-400"
                    aria-hidden="true"
                  >
                    {/* ngôi sao outline */}
                    <path d="M12 3.6l2.1 4.25 4.7.68-3.4 3.3.8 4.65-4.2-2.2-4.2 2.2.8-4.65-3.4-3.3 4.7-.68L12 3.6z"></path>
                  </svg>
                  <span className="whitespace-nowrap">{course.difficulty}</span>
                </div>
              </div>

            </div>

            {/* Nút đáy */}
            <button 
              onClick={() => onStartReading?.(course.id)}
              className="mt-4 w-full bg-sky-500 hover:bg-sky-600 text-white font-medium px-4 py-3 rounded-lg transition"
            >
              ▶ Bắt đầu đọc
            </button>
          </div>
        ))}
      </div>
    </section>
  );
}
