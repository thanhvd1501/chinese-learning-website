# Website Học Tiếng Trung - Chinese Learning Platform

Modern Chinese learning website with **Frontend + Backend + Database** using **Next.js 14**, **Spring Boot**, **PostgreSQL**, and **Framer Motion** for smooth animations.

## 🚀 Tech Stack

- **Frontend**: Next.js 14 (App Router) + TypeScript + TailwindCSS + shadcn/ui + Framer Motion
- **Backend**: Spring Boot 3.3.2 + JWT Auth + REST API
- **Database**: PostgreSQL + Prisma ORM
- **Deploy**: Docker Compose (local), Vercel (FE), Railway/Render (BE + DB)

## 🎨 Design System

- **Colors**: White background, orange accent (#FF8A3D), dark gray text
- **Typography**: Inter font family
- **Motion**: Framer Motion with GPU-accelerated transforms
- **Components**: shadcn/ui + Radix UI primitives

## 📁 Project Structure

```
├── docker-compose.yml          # Postgres service
├── db/                         # Database package
│   ├── prisma/schema.prisma   # Database schema
│   ├── seed.js                # Seed data
│   └── package.json
├── backend/                    # Spring Boot API
│   ├── src/main/java/com/chinese/
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data repositories
│   │   ├── controller/        # REST controllers
│   │   ├── security/          # JWT auth
│   │   └── config/            # Configuration
│   └── pom.xml
└── frontend/                   # Next.js app
    ├── app/                   # App Router pages
    ├── components/            # UI components
    │   ├── ui/               # shadcn/ui components
    │   ├── motion/           # Framer Motion utilities
    │   ├── nav/              # Navigation components
    │   ├── table/            # Data tables
    │   └── tools/            # Tool components
    └── lib/                  # Utilities
```

## 🛠️ Setup & Run

### 1. Start Database
```bash
docker compose up -d
```

### 2. Setup Database
```bash
cd db
npm install
npx prisma generate
npx prisma migrate dev --name init
npm run seed
```

### 3. Start Backend
```bash
cd backend
mvn spring-boot:run
```

### 4. Start Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🌐 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/me` - Get current user

### Content
- `GET /api/vocab` - Vocabulary list (search, filter, paginate)
- `GET /api/articles` - Articles list
- `GET /api/articles/{slug}` - Article by slug
- `GET /api/textbooks` - Textbooks list
- `GET /api/radicals` - Radicals list

### Tools
- `POST /api/tools/pinyin` - Convert pinyin (ni3hao3 → nǐhǎo)

## 🎭 Motion & Animations

### Page Transitions
- Fade + subtle slide (20px)
- Duration: 220-280ms
- Easing: easeOut

### Micro-interactions
- **Card hover**: lift + scale + shadow
- **Button tap**: scale 0.98 + ripple effect
- **Search focus**: ring + glow
- **Table row hover**: background tint

### Performance
- GPU-accelerated transforms (translate/scale/opacity)
- Lazy Motion for code splitting
- Reduced motion support
- 60fps animations

## 📱 Pages

- **/** - Homepage with article grid
- **/tu-vung** - Vocabulary table with search/filter
- **/ngu-phap** - Grammar topics
- **/giao-trinh** - Textbook versions
- **/tu-hoc** - Pinyin converter tool

## 🔧 Development

### Database Schema
- `User` - User accounts with roles
- `Article` - Blog posts with views
- `Vocabulary` - Chinese words with variants
- `Textbook` - Textbook versions
- `Lesson` - Textbook lessons
- `Radical` - Chinese radicals

### Features
- ✅ JWT Authentication
- ✅ Search & Filter vocabulary
- ✅ Pagination
- ✅ Pinyin converter
- ✅ Responsive design
- ✅ Motion animations
- ✅ SEO metadata
- ✅ Skeleton loading

## 🚀 Deployment

### Local Development
All services run locally with Docker Compose for database.

### Production
- **Frontend**: Deploy to Vercel
- **Backend + DB**: Deploy to Railway/Render
- **Database**: PostgreSQL on Railway/Render

## 📊 Performance

- **Lighthouse Score**: Perf ≥ 90, Best Practices ≥ 90, SEO ≥ 90
- **Motion**: 60fps with GPU acceleration
- **Accessibility**: Keyboard navigation, reduced motion support
- **Type Safety**: TypeScript strict mode

## 🎯 Key Features

1. **Modern UI/UX** with smooth animations
2. **Vocabulary Table** with real-time search
3. **Pinyin Converter** tool
4. **Responsive Design** for all devices
5. **SEO Optimized** with metadata
6. **Type Safe** with TypeScript
7. **Accessible** with proper ARIA labels

---

Built with ❤️ for Chinese language learners worldwide.
