# Keycloak Roles Setup Guide

Hướng dẫn cấu hình roles trong Keycloak cho hệ thống Chinese Learning.

## 🎯 Role Hierarchy

```
ADMIN
  └── Full access (create, update, delete courses)
  
TEACHER
  └── Can create and update courses (cannot delete)
  
STUDENT (default)
  └── Read-only access
```

---

## 📋 Bước 1: Tạo Realm Roles

### 1. Login vào Keycloak Admin Console
- URL: http://localhost:8180
- Username: `admin`
- Password: `admin`

### 2. Chọn Realm `chinese-learning`

### 3. Tạo Realm Roles

1. Vào **Realm roles** trong menu bên trái
2. Click **Create role**

#### Tạo role ADMIN:
- **Role name**: `ADMIN`
- **Description**: Full administrative access
- Click **Save**

#### Tạo role TEACHER:
- **Role name**: `TEACHER`
- **Description**: Can manage courses (create, update)
- Click **Save**

#### Tạo role STUDENT:
- **Role name**: `STUDENT`
- **Description**: Read-only access
- Click **Save**

---

## 👤 Bước 2: Tạo Test Users

### 1. Vào **Users** → Click **Add user**

### Tạo Admin User:
- **Username**: `admin@example.com`
- **Email**: `admin@example.com`
- **Email verified**: `ON`
- **First name**: `Admin`
- **Last name**: `User`
- Click **Create**

#### Set password:
1. Vào tab **Credentials**
2. Click **Set password**
3. Password: `admin123`
4. Temporary: `OFF`
5. Click **Save**

#### Assign role:
1. Vào tab **Role mapping**
2. Click **Assign role**
3. Filter by roles → chọn **ADMIN**
4. Click **Assign**

### Tạo Teacher User:
- **Username**: `teacher@example.com`
- **Email**: `teacher@example.com`
- **Email verified**: `ON`
- **First name**: `Teacher`
- **Last name**: `User`

#### Set password:
- Password: `teacher123`
- Temporary: `OFF`

#### Assign role:
- Assign role **TEACHER**

### Tạo Student User:
- **Username**: `student@example.com`
- **Email**: `student@example.com`
- **Email verified**: `ON`
- **First name**: `Student`
- **Last name**: `User`

#### Set password:
- Password: `student123`
- Temporary: `OFF`

#### Assign role:
- Assign role **STUDENT**

---

## 🔧 Bước 3: Configure Client to Include Roles in Token

### 1. Vào **Clients** → chọn `chinese-learning-backend`

### 2. Vào tab **Client scopes**

### 3. Click vào `chinese-learning-backend-dedicated` (hoặc tạo mới nếu chưa có)

### 4. Vào tab **Mappers** → Click **Configure a new mapper**

### 5. Chọn **User Realm Role**

#### Cấu hình Realm Role Mapper:
- **Name**: `realm-roles`
- **Token Claim Name**: `realm_access.roles`
- **Claim JSON Type**: `String`
- **Add to ID token**: `ON`
- **Add to access token**: `ON`
- **Add to userinfo**: `ON`
- Click **Save**

### 6. Tạo thêm Client Role Mapper (optional)

Click **Add mapper** → **By configuration** → **User Client Role**

#### Cấu hình:
- **Name**: `client-roles`
- **Client ID**: `chinese-learning-backend`
- **Token Claim Name**: `resource_access.chinese-learning-backend.roles`
- **Claim JSON Type**: `String`
- **Add to ID token**: `ON`
- **Add to access token**: `ON`
- Click **Save**

---

## ✅ Bước 4: Test Role-based Access

### Test với Admin User

```bash
# 1. Get Admin token
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=admin@example.com" \
  -d "password=admin123" \
  | jq -r '.access_token')

# 2. Create course - SUCCESS ✅
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "textbookId": 1,
    "level": "Hán 7",
    "title": "Hán 7",
    "lessons": 12,
    "duration": "3 tháng",
    "difficulty": "NÂNG_CAO"
  }'

# 3. Delete course - SUCCESS ✅
curl -X DELETE http://localhost:8080/api/courses/7 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Test với Teacher User

```bash
# 1. Get Teacher token
TEACHER_TOKEN=$(curl -s -X POST http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=teacher@example.com" \
  -d "password=teacher123" \
  | jq -r '.access_token')

# 2. Create course - SUCCESS ✅
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer $TEACHER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "textbookId": 1,
    "level": "Hán 8",
    "title": "Hán 8",
    "lessons": 10,
    "duration": "2 tháng",
    "difficulty": "CHUYÊN_GIA"
  }'

# 3. Update course - SUCCESS ✅
curl -X PUT http://localhost:8080/api/courses/8 \
  -H "Authorization: Bearer $TEACHER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title": "Hán 8 - Updated"}'

# 4. Delete course - FAIL ❌ (403 Forbidden)
curl -X DELETE http://localhost:8080/api/courses/8 \
  -H "Authorization: Bearer $TEACHER_TOKEN"

# Response:
# {
#   "status": 403,
#   "error": "Forbidden",
#   "message": "Access Denied"
# }
```

### Test với Student User

```bash
# 1. Get Student token
STUDENT_TOKEN=$(curl -s -X POST http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=password" \
  -d "username=student@example.com" \
  -d "password=student123" \
  | jq -r '.access_token')

# 2. Get courses - SUCCESS ✅
curl http://localhost:8080/api/courses \
  -H "Authorization: Bearer $STUDENT_TOKEN"

# 3. Create course - FAIL ❌ (403 Forbidden)
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer $STUDENT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "textbookId": 1,
    "level": "Hán 9",
    "title": "Hán 9",
    "lessons": 10,
    "difficulty": "NÂNG_CAO"
  }'

# Response:
# {
#   "status": 403,
#   "error": "Forbidden",
#   "message": "Access Denied"
# }
```

---

## 🔍 Decode JWT Token để xem Roles

### Sử dụng jwt.io

1. Truy cập: https://jwt.io/
2. Paste access token vào

### Token payload sẽ chứa:

```json
{
  "exp": 1698537000,
  "iat": 1698536700,
  "jti": "abc123",
  "iss": "http://localhost:8180/realms/chinese-learning",
  "sub": "user-uuid-123",
  "typ": "Bearer",
  "azp": "chinese-learning-backend",
  "preferred_username": "admin@example.com",
  "email": "admin@example.com",
  "email_verified": true,
  "realm_access": {
    "roles": [
      "ADMIN",
      "default-roles-chinese-learning",
      "offline_access",
      "uma_authorization"
    ]
  },
  "resource_access": {
    "chinese-learning-backend": {
      "roles": []
    }
  }
}
```

---

## 📊 Permission Matrix

| Action | Endpoint | ADMIN | TEACHER | STUDENT | Anonymous |
|--------|----------|-------|---------|---------|-----------|
| List courses | GET `/api/courses` | ✅ | ✅ | ✅ | ✅ |
| View course | GET `/api/courses/{id}` | ✅ | ✅ | ✅ | ✅ |
| Create course | POST `/api/courses` | ✅ | ✅ | ❌ | ❌ |
| Update course | PUT `/api/courses/{id}` | ✅ | ✅ | ❌ | ❌ |
| Delete course | DELETE `/api/courses/{id}` | ✅ | ❌ | ❌ | ❌ |

---

## 🐛 Troubleshooting

### Token không chứa roles

**Problem**: Access token không có `realm_access.roles`

**Solution**:
1. Kiểm tra Client Scopes → Mappers
2. Ensure realm-roles mapper được configure đúng
3. Kiểm tra Token Claim Name = `realm_access.roles`
4. Restart Keycloak nếu cần

### 403 Forbidden khi có đúng role

**Problem**: User có role ADMIN nhưng vẫn bị 403

**Solution**:
1. Check JwtAuthConverter đang extract roles đúng cách
2. Debug: print authorities trong token
3. Ensure role prefix là `ROLE_` (Spring Security convention)
4. Check @PreAuthorize expression syntax

### Student có thể tạo course

**Problem**: Student bypass được authorization

**Solution**:
1. Check SecurityConfig → ensure POST /api/courses/** is protected
2. Verify @PreAuthorize annotation trên controller method
3. Ensure @EnableMethodSecurity trong SecurityConfig

---

## 🎓 Best Practices

1. **Principle of Least Privilege**
   - Mặc định user là STUDENT
   - Chỉ assign TEACHER/ADMIN khi cần thiết

2. **Role Naming Convention**
   - Uppercase: ADMIN, TEACHER, STUDENT
   - Clear and descriptive

3. **Token Expiration**
   - Short-lived access tokens (5-15 minutes)
   - Longer refresh tokens (30 days)

4. **Audit Logging**
   - Log all admin actions
   - Track who created/modified courses

---

## 📝 Quick Reference

### Create Test Users Script

```bash
# Tạo file create-users.sh

#!/bin/bash

KEYCLOAK_URL="http://localhost:8180"
REALM="chinese-learning"
ADMIN_USER="admin"
ADMIN_PASS="admin"

# Get admin token
TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/admin/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=$ADMIN_USER" \
  -d "password=$ADMIN_PASS" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" \
  | jq -r '.access_token')

# Create users and assign roles
# (Add API calls here)
```

---

## ✅ Verification Checklist

- [ ] Created ADMIN, TEACHER, STUDENT roles in Keycloak
- [ ] Created test users with different roles
- [ ] Configured realm-roles mapper
- [ ] Tested admin can create/update/delete
- [ ] Tested teacher can create/update but not delete
- [ ] Tested student can only read
- [ ] Verified JWT token contains roles
- [ ] @PreAuthorize annotations working correctly

