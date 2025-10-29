# 🔒 Protected API Implementation Summary

## ✅ Đã thêm

### 1. **DTOs cho Course Management**
- `CreateCourseRequest.java` - Request để tạo course mới (có validation)
- `UpdateCourseRequest.java` - Request để update course
- `CourseResponse.java` - Response trả về client

### 2. **Service Layer**
- `CourseService.java` - Business logic với các features:
  - ✅ Validation: textbook phải tồn tại
  - ✅ Duplicate check: không cho trùng level trong cùng textbook
  - ✅ CRUD operations đầy đủ
  - ✅ Transaction management

### 3. **Mapper**
- `CourseMapper.java` - MapStruct mapper để convert Entity ↔ DTO

### 4. **Controller với Protected Endpoints**
- `CourseController.java` đã được cập nhật:
  - ✅ `GET /api/courses` - Public (không cần auth)
  - ✅ `GET /api/courses/{id}` - Public
  - 🔒 `POST /api/courses` - **Cần JWT token**
  - 🔒 `PUT /api/courses/{id}` - **Cần JWT token**
  - 🔒 `DELETE /api/courses/{id}` - **Cần JWT token**

### 5. **Security Configuration**
- `SecurityConfig.java` đã được cập nhật:
  - ✅ Phân biệt rõ ràng giữa HTTP methods
  - ✅ GET requests: public
  - ✅ POST/PUT/DELETE: require authentication
  - ✅ JWT validation từ Keycloak

### 6. **Documentation**
- `API_EXAMPLES.md` - Hướng dẫn chi tiết cách gọi API với curl

---

## 🎯 Demo Flow

### 1️⃣ Public Access (không cần token)

```bash
# Lấy danh sách courses - OK ✅
curl http://localhost:8080/api/courses

# Lấy course theo ID - OK ✅
curl http://localhost:8080/api/courses/1
```

### 2️⃣ Protected Access (cần token)

```bash
# Tạo course MÀ KHÔNG CÓ TOKEN - FAIL ❌ (401 Unauthorized)
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"textbookId": 1, "level": "Hán 7", "title": "Hán 7", "difficulty": "NÂNG_CAO"}'

# Response:
# {
#   "status": 401,
#   "error": "Unauthorized",
#   "message": "Full authentication is required"
# }
```

### 3️⃣ Lấy JWT Token từ Keycloak

```bash
# Step 1: Get token
TOKEN=$(curl -s -X POST http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=test@example.com" \
  -d "password=password123" \
  | jq -r '.access_token')

echo $TOKEN
```

### 4️⃣ Gọi Protected Endpoint với Token

```bash
# Tạo course VỚI TOKEN - SUCCESS ✅ (201 Created)
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "textbookId": 1,
    "level": "Hán 7",
    "title": "Hán 7 - Nâng cao",
    "description": "Khóa học văn học Trung Quốc",
    "lessons": 12,
    "duration": "3 tháng",
    "difficulty": "NÂNG_CAO",
    "coverImageUrl": "/han-7.png"
  }'

# Response:
# {
#   "id": 7,
#   "textbookId": 1,
#   "textbookName": "Hán ngữ 6 Quyển - Phiên bản 3",
#   "level": "Hán 7",
#   "title": "Hán 7 - Nâng cao",
#   ...
# }
```

---

## 🔐 Security Flow

```
Client Request
     ↓
Security Filter Chain
     ↓
Check HTTP Method + Path
     ↓
┌─────────────────────────────────┐
│ GET /api/courses/*              │ → Allow (Public)
│ POST /api/courses/*             │ → Check JWT Token
│ PUT /api/courses/*              │ → Check JWT Token
│ DELETE /api/courses/*           │ → Check JWT Token
└─────────────────────────────────┘
     ↓
JWT Token Validation
     ↓
┌─────────────────────────────────┐
│ Valid Token?                    │
│   ✅ Yes → Process Request      │
│   ❌ No  → 401 Unauthorized     │
└─────────────────────────────────┘
     ↓
Controller → Service → Repository
     ↓
Response
```

---

## 🧪 Validation Examples

### ✅ Valid Request

```json
{
  "textbookId": 1,
  "level": "Hán 7",
  "title": "Hán 7",
  "description": "Advanced course",
  "lessons": 12,
  "duration": "3 months",
  "difficulty": "NÂNG_CAO",
  "coverImageUrl": "/han-7.png"
}
```

### ❌ Invalid Request (Missing required fields)

```json
{
  "level": "Hán 7"
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "textbookId": "Textbook ID không được null",
    "title": "Title không được trống",
    "difficulty": "Difficulty không được null"
  }
}
```

### ❌ Business Logic Error (Duplicate level)

```json
{
  "textbookId": 1,
  "level": "Hán 1",  // Already exists!
  "title": "Hán 1 Duplicate",
  "difficulty": "CƠ_BẢN",
  "lessons": 10
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Course with level Hán 1 already exists for textbook: Hán ngữ 6 Quyển - Phiên bản 3"
}
```

---

## 📊 API Summary Table

| Endpoint | Method | Auth Required | Description |
|----------|--------|---------------|-------------|
| `/api/courses` | GET | ❌ No | Lấy danh sách courses |
| `/api/courses/{id}` | GET | ❌ No | Lấy course theo ID |
| `/api/courses` | POST | ✅ Yes | Tạo course mới |
| `/api/courses/{id}` | PUT | ✅ Yes | Cập nhật course |
| `/api/courses/{id}` | DELETE | ✅ Yes | Xóa course |

---

## 🎓 Learning Points

### 1. **JWT Authentication Flow**
- Frontend lấy token từ Keycloak
- Attach token vào header: `Authorization: Bearer <token>`
- Backend validate token với Keycloak's public key
- Extract user info từ JWT claims

### 2. **Security Best Practices**
- ✅ Public endpoints cho READ operations
- ✅ Protected endpoints cho WRITE operations
- ✅ Validation ở cả DTO level và Business Logic level
- ✅ Clear error messages

### 3. **Clean Architecture**
```
Controller (HTTP) → Service (Business Logic) → Repository (Data Access)
     ↓                    ↓                           ↓
   DTO              Domain Entity                 Database
```

### 4. **Transaction Management**
- `@Transactional(readOnly = true)` cho READ operations
- `@Transactional` cho WRITE operations
- Auto rollback on exception

---

## 🚀 Next Steps

Có thể mở rộng thêm:

1. **Role-based Access Control**
   - Admin: full access
   - Teacher: create/update courses
   - Student: read only

2. **Advanced Validation**
   - Custom validators
   - Cross-field validation

3. **Audit Logging**
   - Track who created/updated/deleted courses
   - Timestamp all changes

4. **Soft Delete**
   - Mark as deleted instead of actual deletion
   - Recovery mechanism

---

## 📝 Files Modified/Created

### Created (7 files)
1. `dto/CreateCourseRequest.java`
2. `dto/UpdateCourseRequest.java`
3. `dto/CourseResponse.java`
4. `mapper/CourseMapper.java`
5. `service/CourseService.java`
6. `API_EXAMPLES.md`
7. `PROTECTED_API_SUMMARY.md` (this file)

### Modified (3 files)
1. `controller/CourseController.java` - Added protected endpoints
2. `config/SecurityConfig.java` - HTTP method-based security
3. `README.md` - Updated API docs

---

## ✅ Testing Checklist

- [ ] GET courses without token → Should work
- [ ] POST course without token → Should return 401
- [ ] POST course with invalid token → Should return 403
- [ ] POST course with valid token → Should create course
- [ ] POST duplicate level → Should return 400 with error message
- [ ] POST with invalid textbookId → Should return 400
- [ ] PUT course with valid token → Should update course
- [ ] DELETE course with valid token → Should delete course
- [ ] Validation errors → Should return 400 with field errors

---

## 🎉 Summary

Bây giờ bạn đã có:
- ✅ 1 ví dụ hoàn chỉnh về Protected API
- ✅ JWT authentication working
- ✅ Validation layer
- ✅ Business logic validation
- ✅ Clear separation giữa public và protected endpoints
- ✅ Swagger docs với security badge
- ✅ Complete CRUD operations

Có thể áp dụng pattern tương tự cho các entities khác!

