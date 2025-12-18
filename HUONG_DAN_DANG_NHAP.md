# Hướng Dẫn Tích Hợp Đăng Nhập

## 🎯 Tổng Quan
Đã tích hợp thành công API đăng nhập giữa Backend (Spring Boot) và Frontend (Android).

## 📋 Các File Đã Tạo/Cập Nhật

### Backend (BE)
1. **LoginRequest.java** - DTO cho request đăng nhập
2. **LoginResponse.java** - DTO cho response đăng nhập
3. **NguoiDungRepository.java** - Thêm method tìm kiếm theo email và mật khẩu
4. **NguoiDungService.java** - Thêm logic xử lý đăng nhập
5. **NguoiDungController.java** - Thêm endpoint `/api/nguoidung/login`

### Frontend (FE)
1. **RetrofitClient.java** - Client để gọi API
2. **ApiService.java** - Interface định nghĩa các API endpoint
3. **LoginRequest.java** - Model cho request
4. **LoginResponse.java** - Model cho response
5. **SessionManager.java** - Quản lý session người dùng
6. **Login.java** - Cập nhật logic đăng nhập
7. **HomeFragment.java** - Hiển thị "Chào + tên đăng nhập"
8. **build.gradle.kts** - Thêm Retrofit dependencies
9. **AndroidManifest.xml** - Thêm INTERNET permission

## 🚀 Cách Sử Dụng

### 1. Chạy Backend
```bash
cd BE
./gradlew bootRun
```
Backend sẽ chạy tại: `http://localhost:8080`

### 2. Cấu Hình IP cho Android

#### Nếu dùng Emulator:
- Sử dụng: `http://10.0.2.2:8080` (đã cấu hình sẵn)

#### Nếu dùng thiết bị thật:
- Mở file: `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java`
- Thay đổi:
```java
private static final String BASE_URL = "http://192.168.1.X:8080/";
```
(Thay X bằng IP máy tính của bạn)

### 3. Chạy Android App
- Sync Gradle
- Build và chạy app

### 4. Test Đăng Nhập

Sử dụng tài khoản có sẵn trong database:

| Email | Mật khẩu | Vai trò |
|-------|----------|---------|
| admin@localcooking.vn | admin123 | Admin |
| nguyenvanan@gmail.com | gv123 | GiaoVien |
| tranthibinh@gmail.com | gv123 | GiaoVien |
| levancuong@gmail.com | hv123 | HocVien |
| phamthidung@gmail.com | hv123 | HocVien |
| hoangvanem@gmail.com | hv123 | HocVien |

## 📱 Luồng Hoạt Động

1. **Người dùng nhập email và mật khẩu** → Click "Đăng nhập"
2. **App gửi request** → Backend API `/api/nguoidung/login`
3. **Backend kiểm tra**:
   - Email và mật khẩu có đúng không?
   - Tài khoản có bị khóa không?
4. **Backend trả về response**:
   - Thành công: Thông tin người dùng
   - Thất bại: Thông báo lỗi
5. **App xử lý**:
   - Lưu session (SharedPreferences)
   - Chuyển sang màn hình Home
   - Hiển thị "Chào + tên đăng nhập"

## 🔐 Tính Năng

### ✅ Đã Hoàn Thành
- [x] API đăng nhập Backend
- [x] Tích hợp Retrofit
- [x] Quản lý session
- [x] Validate input
- [x] Hiển thị tên người dùng
- [x] Tự động đăng nhập (remember session)
- [x] Xử lý lỗi kết nối

### 🔄 Có Thể Mở Rộng
- [ ] Mã hóa mật khẩu (BCrypt)
- [ ] JWT Token authentication
- [ ] Refresh token
- [ ] Đăng xuất
- [ ] Đổi mật khẩu
- [ ] Quên mật khẩu với OTP

## 🐛 Xử Lý Lỗi

### Lỗi: "Unable to resolve host"
- Kiểm tra Backend đã chạy chưa
- Kiểm tra IP address trong RetrofitClient
- Kiểm tra firewall

### Lỗi: "Connection refused"
- Backend chưa chạy
- Port 8080 bị chặn

### Lỗi: Database connection
- Kiểm tra SQL Server đã chạy
- Kiểm tra application.properties

## 📝 Ghi Chú

- Mật khẩu hiện tại lưu dạng plain text (không an toàn cho production)
- Nên implement BCrypt hoặc Argon2 để hash password
- Nên thêm JWT token cho bảo mật tốt hơn
- Session hiện tại lưu trong SharedPreferences (có thể bị xóa khi clear data)

## 🎉 Kết Quả

Khi đăng nhập thành công:
- Màn hình Home hiển thị: **"Chào admin"** (hoặc tên đăng nhập khác)
- Mũi tên bên cạnh sẽ bị ẩn
- Session được lưu, lần sau mở app không cần đăng nhập lại
