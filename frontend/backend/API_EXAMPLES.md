# API Examples

## 🔓 Public Endpoints (Không cần authentication)

### 1. Lấy danh sách khóa học

```bash
curl -X GET "http://localhost:8080/api/courses" \
  -H "Accept: application/json"
```

**Response:**
```json
[
  {
    "id": 1,
    "textbookId": 1,
    "textbookName": "Hán ngữ 6 Quyển - Phiên bản 3",
    "level": "Hán 1",
    "title": "Hán 1",
    "description": "Cấp độ cơ bản nhất, học 150 từ vựng thiết yếu",
    "lessons": 15,
    "duration": "2-3 tháng",
    "difficulty": "CƠ_BẢN",
    "coverImageUrl": "/han-1.png",
    "createdAt": "2025-10-28T21:00:00",
    "updatedAt": "2025-10-28T21:00:00"
  }
]
```

### 2. Lấy khóa học theo ID

```bash
curl -X GET "http://localhost:8080/api/courses/1" \
  -H "Accept: application/json"
```

### 3. Filter khóa học theo textbook

```bash
curl -X GET "http://localhost:8080/api/courses?textbookId=1" \
  -H "Accept: application/json"
```

---

## 🔒 Protected Endpoints (Cần JWT token)

### Bước 1: Lấy JWT Token từ Keycloak

#### Option A: Client Credentials Flow (cho testing)

```bash
curl -X POST "http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=client_credentials" \
  -d "scope=openid"
```

#### Option B: Password Flow (cho user login)

```bash
curl -X POST "http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=test@example.com" \
  -d "password=password123" \
  -d "scope=openid"
```

**Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...",
  "token_type": "Bearer"
}
```

### Bước 2: Sử dụng Token để gọi Protected Endpoints

#### 1. Tạo khóa học mới (POST)

```bash
curl -X POST "http://localhost:8080/api/courses" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "textbookId": 1,
    "level": "Hán 7",
    "title": "Hán 7",
    "description": "Khóa học nâng cao về văn học Trung Quốc",
    "lessons": 12,
    "duration": "3 tháng",
    "difficulty": "NÂNG_CAO",
    "coverImageUrl": "/han-7.png"
  }'
```

**Success Response (201 Created):**
```json
{
  "id": 7,
  "textbookId": 1,
  "textbookName": "Hán ngữ 6 Quyển - Phiên bản 3",
  "level": "Hán 7",
  "title": "Hán 7",
  "description": "Khóa học nâng cao về văn học Trung Quốc",
  "lessons": 12,
  "duration": "3 tháng",
  "difficulty": "NÂNG_CAO",
  "coverImageUrl": "/han-7.png",
  "createdAt": "2025-10-28T22:30:00",
  "updatedAt": "2025-10-28T22:30:00"
}
```

**Error Response (401 Unauthorized) - Không có token:**
```json
{
  "timestamp": "2025-10-28T22:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/courses"
}
```

**Error Response (403 Forbidden) - Token không hợp lệ:**
```json
{
  "timestamp": "2025-10-28T22:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Invalid JWT token",
  "path": "/api/courses"
}
```

**Validation Error (400 Bad Request):**
```json
{
  "timestamp": "2025-10-28T22:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "textbookId": "Textbook ID không được null",
    "title": "Title không được trống",
    "difficulty": "Difficulty không được null"
  },
  "path": "/api/courses"
}
```

#### 2. Cập nhật khóa học (PUT)

```bash
curl -X PUT "http://localhost:8080/api/courses/7" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Hán 7 - Văn học Trung Quốc",
    "description": "Khóa học chuyên sâu về văn học cổ điển Trung Quốc",
    "lessons": 15
  }'
```

**Success Response (200 OK):**
```json
{
  "id": 7,
  "textbookId": 1,
  "textbookName": "Hán ngữ 6 Quyển - Phiên bản 3",
  "level": "Hán 7",
  "title": "Hán 7 - Văn học Trung Quốc",
  "description": "Khóa học chuyên sâu về văn học cổ điển Trung Quốc",
  "lessons": 15,
  "duration": "3 tháng",
  "difficulty": "NÂNG_CAO",
  "coverImageUrl": "/han-7.png",
  "createdAt": "2025-10-28T22:30:00",
  "updatedAt": "2025-10-28T22:35:00"
}
```

#### 3. Xóa khóa học (DELETE)

```bash
curl -X DELETE "http://localhost:8080/api/courses/7" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Success Response (204 No Content):**
```
(No response body)
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2025-10-28T22:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Course not found with id: 7",
  "path": "/api/courses/7"
}
```

---

## 🧪 Testing với Postman

### 1. Tạo Collection mới

### 2. Thêm Authorization

1. Trong Collection/Request, chọn tab **Authorization**
2. Type: **OAuth 2.0**
3. Add auth data to: **Request Headers**

4. Configure New Token:
   - **Token Name**: Keycloak Token
   - **Grant Type**: Password Credentials
   - **Access Token URL**: `http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token`
   - **Client ID**: `chinese-learning-backend`
   - **Client Secret**: (your client secret)
   - **Username**: test@example.com
   - **Password**: password123
   - **Scope**: openid
   - **Client Authentication**: Send as Basic Auth header

5. Click **Get New Access Token**
6. Click **Use Token**

### 3. Test các endpoints

- GET requests: hoạt động mà không cần token
- POST/PUT/DELETE: cần token trong Authorization header

---

## 🔍 Decode JWT Token (để xem thông tin)

Truy cập: https://jwt.io/

Paste access token vào để xem payload:

```json
{
  "exp": 1698537000,
  "iat": 1698536700,
  "jti": "abc123...",
  "iss": "http://localhost:8180/realms/chinese-learning",
  "sub": "user-uuid-123",
  "typ": "Bearer",
  "azp": "chinese-learning-backend",
  "preferred_username": "test@example.com",
  "email": "test@example.com",
  "email_verified": true,
  "realm_access": {
    "roles": ["user"]
  }
}
```

---

## 📊 Swagger UI Testing

1. Truy cập: http://localhost:8080/swagger-ui.html

2. Click **Authorize** button (ở góc trên bên phải)

3. Nhập Bearer Token:
   ```
   Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...
   ```

4. Click **Authorize**

5. Test các protected endpoints trực tiếp trong Swagger UI

---

## 🎯 Business Logic trong CourseService

### Validation Rules:

1. **Textbook phải tồn tại** khi tạo course
2. **Không được duplicate level** trong cùng một textbook
3. **Số bài học** phải > 0
4. **Difficulty** phải thuộc enum: CƠ_BẢN, TRUNG_BÌNH, NÂNG_CAO, CHUYÊN_GIA

### Error Messages:

```json
// Textbook not found
{
  "message": "Textbook not found with id: 999"
}

// Duplicate level
{
  "message": "Course with level Hán 1 already exists for textbook: Hán ngữ 6 Quyển"
}

// Course not found
{
  "message": "Course not found with id: 999"
}
```

