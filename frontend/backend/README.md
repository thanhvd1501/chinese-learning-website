# Chinese Learning Backend

Backend API cho hệ thống học tiếng Trung, được xây dựng với Spring Boot, PostgreSQL, và Keycloak.

## 📋 Tính năng

- ✅ RESTful API theo chuẩn Clean Architecture
- ✅ Xác thực và phân quyền với Keycloak (Google & Facebook login)
- ✅ Quản lý từ vựng với tìm kiếm và phân trang
- ✅ Quản lý giáo trình và khóa học
- ✅ OpenAPI/Swagger documentation
- ✅ Validation với Jakarta Validation
- ✅ Caching với Caffeine
- ✅ Logging với Logback
- ✅ CORS configuration
- ✅ Database migration với Flyway

## 🛠️ Yêu cầu hệ thống

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15+
- Keycloak (chạy qua Docker)

## 🚀 Quick Start

### 1. Clone repository

```bash
git clone <repository-url>
cd chinese-learning-website/backend
```

### 2. Chạy PostgreSQL và Keycloak với Docker

```bash
docker-compose up -d
```

Lệnh này sẽ chạy:
- PostgreSQL trên port `5432`
- Keycloak trên port `8180`

Kiểm tra status:
```bash
docker-compose ps
```

### 3. Cấu hình Keycloak

1. Truy cập Keycloak Admin Console:
   - URL: http://localhost:8180
   - Username: `admin`
   - Password: `admin` (mặc định cho dev mode)

2. Tạo Realm mới:
   - Click "Create Realm"
   - Realm name: `chinese-learning`
   - Click "Create"

3. Tạo Client:
   - Realm Settings → Clients → Create client
   - Client ID: `chinese-learning-backend`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `http://localhost:5173/*`, `http://localhost:8080/*`
   - Click "Save"

4. Cấu hình Social Login (Google):
   - Realm Settings → Identity providers
   - Click "Add provider" → chọn "Google"
   - Client ID: (Google Client ID của bạn)
   - Client secret: (Google Client secret)
   - Click "Add"

5. Cấu hình Social Login (Facebook):
   - Realm Settings → Identity providers
   - Click "Add provider" → chọn "Facebook"
   - Client ID: (Facebook App ID của bạn)
   - Client secret: (Facebook App secret)
   - Click "Add"

### 4. Tạo database và chạy migration

Database sẽ được tạo tự động khi chạy application. Flyway migration sẽ chạy tự động.

### 5. Build và chạy application

```bash
# Build
mvn clean install

# Chạy
mvn spring-boot:run
```

Application sẽ chạy tại: http://localhost:8080

## 📚 API Documentation

Sau khi chạy application, truy cập Swagger UI:
- URL: http://localhost:8080/swagger-ui.html

Hoặc OpenAPI JSON:
- URL: http://localhost:8080/v3/api-docs

## 🔑 API Endpoints

### Public Endpoints (không cần authentication)

#### Vocabulary API
```
GET /api/vocab?page=0&size=20&bienThe=GIAN&search=你好
GET /api/vocab/{id}
```

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng items (default: 20)
- `bienThe`: Filter theo biến thể (GIAN, PHON, BOTH)
- `search`: Từ khóa tìm kiếm

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "hanzi": "你好",
      "pinyin": "nǐ hǎo",
      "nghia": "Xin chào",
      "viDu": "你好，我是小明。",
      "bienThe": "GIAN",
      "hskLevel": 1,
      "frequencyRank": 1,
      "tags": ["greeting", "common"]
    }
  ],
  "totalElements": 20,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Textbooks API
```
GET /api/textbooks
```

#### Courses API
```
GET /api/courses
GET /api/courses/{id}
```

#### Grammar Topics API
```
GET /api/grammar
GET /api/grammar/{id}
```

#### Radicals API
```
GET /api/radicals
GET /api/radicals/{id}
```

### Protected Endpoints (cần authentication với Keycloak)

```
POST /api/users/profile
GET /api/users/{id}/progress
POST /api/ai/chat
GET /api/users/ai-usage
```

## 🏗️ Cấu trúc dự án

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/chineselearning/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── domain/              # JPA Entities
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── service/             # Business Logic
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── mapper/              # MapStruct Mappers
│   │   │   ├── exception/           # Exception Handlers
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       ├── application.yml       # Application configuration
│   │       ├── db/
│   │       │   └── migration/       # Flyway migrations
│   │       └── logback-spring.xml   # Logging configuration
│   └── test/
├── pom.xml
├── docker-compose.yml
└── README.md
```

## 🗄️ Database Schema

### Tables:
- `users` - Thông tin người dùng (synced từ Keycloak)
- `vocabulary` - Từ vựng tiếng Trung
- `vocab_tags` - Tags cho từ vựng
- `textbooks` - Giáo trình
- `courses` - Khóa học (Hán 1-6)
- `grammar_topics` - Chủ đề ngữ pháp
- `radicals` - Bộ thủ
- `user_learning_progress` - Tiến trình học tập
- `ai_chat_messages` - Lịch sử chat với AI
- `user_ai_usage` - Giới hạn sử dụng AI hàng ngày

## 🔐 Security

Application sử dụng JWT tokens từ Keycloak để xác thực. 

### Thêm JWT token vào request:
```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8080/api/protected-endpoint
```

### Cấu hình Keycloak trong application.yml:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/chinese-learning
          jwk-set-uri: http://localhost:8180/realms/chinese-learning/protocol/openid-connect/certs
```

## 🧪 Testing

```bash
# Run tests
mvn test

# Run với coverage
mvn test jacoco:report
```

## 📝 Logging

Logs được lưu tại: `logs/application.log`

## 🐛 Troubleshooting

### PostgreSQL connection error
```bash
# Kiểm tra PostgreSQL đang chạy
docker-compose ps

# Xem logs
docker-compose logs postgres
```

### Keycloak không start
```bash
# Xem logs
docker-compose logs keycloak

# Restart Keycloak
docker-compose restart keycloak
```

### Migration lỗi
```bash
# Xem Flyway logs trong application logs
tail -f logs/application.log | grep flyway
```

## 📦 Build và Deploy

### Build JAR
```bash
mvn clean package
```

File JAR sẽ được tạo tại: `target/backend-1.0.0-SNAPSHOT.jar`

### Chạy JAR
```bash
java -jar target/backend-1.0.0-SNAPSHOT.jar
```

### Docker Build
```bash
# Build image
docker build -t chinese-learning-backend:latest .

# Run container
docker run -p 8080:8080 chinese-learning-backend:latest
```

## 🤝 Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the Apache 2.0 License.

## 👥 Authors

- Chinese Learning Team

## 🙏 Acknowledgments

- Spring Boot Team
- Keycloak Community
- PostgreSQL Community

