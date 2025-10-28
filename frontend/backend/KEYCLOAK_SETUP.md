# Keycloak Setup Guide

Hướng dẫn chi tiết để cấu hình Keycloak cho hệ thống Chinese Learning.

## 🚀 Bước 1: Start Keycloak với Docker

```bash
cd backend
docker-compose up -d keycloak
```

Đợi cho Keycloak start (khoảng 30 giây), sau đó truy cập:
- URL: http://localhost:8180
- Username: `admin`
- Password: `admin` (mặc định cho dev mode)

## 📋 Bước 2: Tạo Realm

1. Login vào Keycloak Admin Console
2. Click vào dropdown "Master" ở góc trên bên trái
3. Click "Create Realm"
4. Nhập Realm name: `chinese-learning`
5. Click "Create"

## 🔧 Bước 3: Cấu hình Client cho Backend

1. Trong Realm `chinese-learning`, chọn **Clients** trong menu bên trái
2. Click "Create client"
3. Điền thông tin:
   - **Client ID**: `chinese-learning-backend`
   - **Client Protocol**: `openid-connect`
   - Click "Next"
4. Điền cấu hình:
   - **Client authentication**: `ON` (để enable confidential client)
   - **Authorization**: `ON`
   - **Valid Redirect URIs**: 
     ```
     http://localhost:5173/*
     http://localhost:8080/*
     http://localhost:3000/*
     ```
   - **Web Origins**: 
     ```
     http://localhost:5173
     http://localhost:8080
     http://localhost:3000
     ```
   - Click "Save"

5. Vào tab **Credentials**, copy **Client Secret** (sẽ dùng trong frontend)

## 🌐 Bước 4: Cấu hình Google OAuth

### 4.1. Tạo Google OAuth Credentials

1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project hiện có
3. Vào **APIs & Services** → **Credentials**
4. Click "Create Credentials" → "OAuth client ID"
5. Chọn **Application type**: Web application
6. Nhập **Authorized redirect URIs**:
   ```
   http://localhost:8180/realms/chinese-learning/broker/google/endpoint
   ```
7. Click "Create"
8. Copy **Client ID** và **Client Secret**

### 4.2. Cấu hình Keycloak

1. Trong Keycloak Admin Console, vào **Identity providers**
2. Click "Add provider" → chọn **Google**
3. Điền thông tin:
   - **Client ID**: (Client ID từ Google)
   - **Client Secret**: (Client Secret từ Google)
   - **Trust Email**: `ON`
   - **Backchannel Supported**: `OFF`
   - Click "Add"

## 📘 Bước 5: Cấu hình Facebook OAuth

### 5.1. Tạo Facebook App

1. Truy cập [Facebook Developers](https://developers.facebook.com/)
2. Vào **My Apps** → "Create App"
3. Chọn **Consumer** → "Next"
4. Điền App Name, App Contact Email
5. Click "Create App"

### 5.2. Lấy App Credentials

1. Trong Facebook App, vào **Settings** → **Basic**
2. Copy **App ID** và **App Secret**
3. Vào **Add Product** → **Facebook Login** → **Settings**
4. Thêm **Valid OAuth Redirect URIs**:
   ```
   http://localhost:8180/realms/chinese-learning/broker/facebook/endpoint
   ```

### 5.3. Cấu hình Keycloak

1. Trong Keycloak Admin Console, vào **Identity providers**
2. Click "Add provider" → chọn **Facebook**
3. Điền thông tin:
   - **Client ID**: (App ID từ Facebook)
   - **Client Secret**: (App Secret từ Facebook)
   - **Trust Email**: `ON`
   - **Backchannel Supported**: `OFF`
   - Click "Add"

## 👤 Bước 6: Cấu hình User Attributes

1. Trong **Realm Settings**, vào tab **User Profile**
2. Thêm custom attributes nếu cần:
   - `avatar_url`
   - `first_name`
   - `last_name`

## 🔐 Bước 7: Cấu hình Client Scopes

1. Vào **Client scopes**
2. Tạo new scope với tên: `chinese-learning-api`
3. Add mapper nếu cần

## ✅ Bước 8: Kiểm tra cấu hình

### Test với Postman/curl

```bash
# Lấy token
curl -X POST http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=chinese-learning-backend" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=client_credentials" \
  -d "scope=openid"

# Sử dụng token
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/protected-endpoint
```

## 📱 Frontend Integration

### Cấu hình trong frontend (.env):

```env
NEXT_PUBLIC_KEYCLOAK_URL=http://localhost:8180
NEXT_PUBLIC_KEYCLOAK_REALM=chinese-learning
NEXT_PUBLIC_KEYCLOAK_CLIENT_ID=chinese-learning-frontend
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
```

### Auth URLs:

- Login: `http://localhost:8180/realms/chinese-learning/protocol/openid-connect/auth`
- Token: `http://localhost:8180/realms/chinese-learning/protocol/openid-connect/token`
- UserInfo: `http://localhost:8180/realms/chinese-learning/protocol/openid-connect/userinfo`

## 🐛 Troubleshooting

### Keycloak không connect được với Google
- Kiểm tra redirect URI đúng chưa
- Kiểm tra Google OAuth consent screen đã publish chưa
- Kiểm tra Client ID và Secret đúng chưa

### CORS issues
- Thêm các origin vào **Web Origins** trong Keycloak client
- Kiểm tra CORS config trong `application.yml`

### Token không được accept
- Kiểm tra `issuer-uri` trong `application.yml` đúng không
- Kiểm tra realm name đúng không
- Kiểm tra client secret đúng không

## 📚 Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Google OAuth Setup](https://developers.google.com/identity/protocols/oauth2)

