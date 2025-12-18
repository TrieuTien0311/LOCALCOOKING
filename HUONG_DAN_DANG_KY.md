# Hướng Dẫn Tích Hợp Đăng Ký

## 🎯 Tổng Quan
Đã tích hợp thành công API đăng ký giữa Backend (Spring Boot) và Frontend (Android).

## 📋 Các File Đã Tạo/Cập Nhật

### Backend (BE)
1. **RegisterRequest.java** - DTO cho request đăng ký
2. **RegisterResponse.java** - DTO cho response đăng ký
3. **NguoiDungService.java** - Thêm method `register()` để xử lý đăng ký
4. **NguoiDungController.java** - Thêm endpoint `/api/nguoidung/register`

### Frontend (FE)
1. **RegisterRequest.java** - Model cho request đăng ký
2. **RegisterResponse.java** - Model cho response đăng ký
3. **ApiService.java** - Thêm method `register()`
4. **Register.java** - Cập nhật logic đăng ký với API call

---

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
- Thay đổi IP theo hướng dẫn trong `FIX_API_CA_MAY_AO_VA_THAT.md`

### 3. Chạy Android App
- Sync Gradle
- Build và chạy app

### 4. Test Đăng Ký

Mở app → Click "Đăng ký" → Nhập thông tin:

**Thông tin bắt buộc:**
- Tên đăng nhập *
- Email *
- Mật khẩu * (tối thiểu 6 ký tự)
- Nhập lại mật khẩu *
- Đồng ý điều khoản

**Thông tin không bắt buộc:**
- Họ và tên
- Số điện thoại

---

## 📱 Luồng Hoạt Động

### 1️⃣ Người Dùng Nhập Thông Tin
```
Register Screen
├─ Tên đăng nhập: "user123"
├─ Họ và tên: "Nguyễn Văn A" (optional)
├─ Email: "user@example.com"
├─ Số điện thoại: "0123456789" (optional)
├─ Mật khẩu: "password123"
├─ Nhập lại mật khẩu: "password123"
└─ Checkbox: Đồng ý điều khoản
```

### 2️⃣ Validation Frontend
```
✓ Tên đăng nhập không rỗng
✓ Email không rỗng và đúng định dạng
✓ Mật khẩu không rỗng và >= 6 ký tự
✓ Mật khẩu nhập lại khớp
✓ Đã tick đồng ý điều khoản
```

### 3️⃣ Gửi Request Đến Backend
```
POST /api/nguoidung/register
{
  "tenDangNhap": "user123",
  "matKhau": "password123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "soDienThoai": "0123456789"
}
```

### 4️⃣ Backend Xử Lý
```
1. Kiểm tra email đã tồn tại chưa
2. Kiểm tra tên đăng nhập đã tồn tại chưa
3. Tạo người dùng mới với:
   - Vai trò: "HocVien" (mặc định)
   - Trạng thái: "HoatDong"
   - Ngày tạo: Thời gian hiện tại
4. Lưu vào database
5. Trả về response
```

### 5️⃣ Response Thành Công
```json
{
  "success": true,
  "message": "Đăng ký thành công",
  "maNguoiDung": 7,
  "tenDangNhap": "user123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "vaiTro": "HocVien"
}
```

### 6️⃣ Frontend Xử Lý Response
```
✓ Hiển thị Toast: "Đăng ký thành công"
✓ Chuyển sang màn hình Login
✓ Clear activity stack
```

---

## 🔐 Tính Năng

### ✅ Đã Hoàn Thành
- [x] API đăng ký Backend
- [x] Validation email đã tồn tại
- [x] Validation tên đăng nhập đã tồn tại
- [x] Tích hợp Retrofit
- [x] Validate input frontend
- [x] Real-time password validation
- [x] Password confirmation check
- [x] Toggle show/hide password
- [x] Tự động chuyển sang Login sau đăng ký thành công

### 🔄 Validation Rules

**Tên đăng nhập:**
- Bắt buộc
- Phải unique (không trùng)

**Email:**
- Bắt buộc
- Đúng định dạng email
- Phải unique (không trùng)

**Mật khẩu:**
- Bắt buộc
- Tối thiểu 6 ký tự
- Real-time validation khi nhập

**Nhập lại mật khẩu:**
- Bắt buộc
- Phải khớp với mật khẩu
- Real-time validation khi nhập

**Họ và tên:**
- Không bắt buộc

**Số điện thoại:**
- Không bắt buộc

---

## 🐛 Xử Lý Lỗi

### Lỗi 1: Email đã được sử dụng
```
Response: {
  "success": false,
  "message": "Email đã được sử dụng"
}
→ Toast: "Email đã được sử dụng"
```

### Lỗi 2: Tên đăng nhập đã được sử dụng
```
Response: {
  "success": false,
  "message": "Tên đăng nhập đã được sử dụng"
}
→ Toast: "Tên đăng nhập đã được sử dụng"
```

### Lỗi 3: Lỗi kết nối
```
→ Toast: "Lỗi kết nối server"
```

### Lỗi 4: Lỗi network
```
→ Toast: "Lỗi: [error message]"
```

---

## 🧪 Test Case

### Test 1: Đăng ký thành công
1. Mở app → Click "Đăng ký"
2. Nhập:
   - Tên đăng nhập: "testuser"
   - Email: "test@example.com"
   - Mật khẩu: "123456"
   - Nhập lại: "123456"
   - Tick điều khoản
3. Click "Đăng ký"
4. ✅ Toast: "Đăng ký thành công"
5. ✅ Chuyển sang Login

### Test 2: Email đã tồn tại
1. Nhập email: "admin@localcooking.vn"
2. Click "Đăng ký"
3. ✅ Toast: "Email đã được sử dụng"

### Test 3: Tên đăng nhập đã tồn tại
1. Nhập tên đăng nhập: "admin"
2. Click "Đăng ký"
3. ✅ Toast: "Tên đăng nhập đã được sử dụng"

### Test 4: Mật khẩu không khớp
1. Mật khẩu: "123456"
2. Nhập lại: "654321"
3. ✅ Error hiển thị: "Mật khẩu bạn nhập lại không khớp"

### Test 5: Mật khẩu quá ngắn
1. Mật khẩu: "123"
2. ✅ Error hiển thị: "Mật khẩu phải có ít nhất 6 ký tự"

### Test 6: Email không hợp lệ
1. Email: "invalid-email"
2. Click "Đăng ký"
3. ✅ Toast: "Email không hợp lệ"

### Test 7: Chưa tick điều khoản
1. Nhập đầy đủ thông tin
2. Không tick checkbox
3. Click "Đăng ký"
4. ✅ Toast: "Vui lòng đồng ý với điều khoản"

### Test 8: Kiểm tra trong database
1. Đăng ký thành công
2. Mở SQL Server Management Studio
3. Query:
```sql
SELECT * FROM NguoiDung WHERE email = 'test@example.com'
```
4. ✅ Thấy người dùng mới với:
   - vaiTro = "HocVien"
   - trangThai = "HoatDong"
   - ngayTao = thời gian hiện tại

---

## 📊 Database Schema

Sau khi đăng ký, dữ liệu được lưu vào bảng `NguoiDung`:

```sql
maNguoiDung      INT (auto increment)
tenDangNhap      VARCHAR(50) UNIQUE NOT NULL
matKhau          VARCHAR(255) NOT NULL
hoTen            NVARCHAR(100)
email            VARCHAR(100) UNIQUE NOT NULL
soDienThoai      VARCHAR(15)
diaChi           NVARCHAR(255)
vaiTro           NVARCHAR(20) DEFAULT 'HocVien'
trangThai        NVARCHAR(20) DEFAULT 'HoatDong'
ngayTao          DATETIME DEFAULT GETDATE()
lanCapNhatCuoi   DATETIME DEFAULT GETDATE()
```

---

## 🎉 Kết Quả

Sau khi đăng ký thành công:
- ✅ Người dùng mới được tạo trong database
- ✅ Vai trò mặc định: "HocVien"
- ✅ Trạng thái: "HoatDong"
- ✅ Có thể đăng nhập ngay với tài khoản vừa tạo
- ✅ Hiển thị trong danh sách người dùng

---

## 🔑 API Endpoint

### POST /api/nguoidung/register

**Request Body:**
```json
{
  "tenDangNhap": "string",
  "matKhau": "string",
  "hoTen": "string (optional)",
  "email": "string",
  "soDienThoai": "string (optional)"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đăng ký thành công",
  "maNguoiDung": 7,
  "tenDangNhap": "user123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "vaiTro": "HocVien"
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Email đã được sử dụng",
  "maNguoiDung": null,
  "tenDangNhap": null,
  "hoTen": null,
  "email": null,
  "vaiTro": null
}
```

---

## 💡 Lưu Ý

1. **Mật khẩu hiện tại chưa mã hóa** - Nên thêm BCrypt để bảo mật
2. **Chưa có xác thực email** - Có thể thêm OTP verification
3. **Vai trò mặc định là HocVien** - Admin phải tạo từ database
4. **Session không tự động tạo** - Phải đăng nhập sau khi đăng ký
5. **Validation chỉ ở frontend và backend cơ bản** - Có thể thêm nhiều rule hơn

---

## 🔒 Bảo Mật (Khuyến Nghị)

### Nên thêm:
1. **Password hashing** - BCrypt hoặc Argon2
2. **Email verification** - Gửi OTP qua email
3. **Rate limiting** - Giới hạn số lần đăng ký
4. **CAPTCHA** - Chống bot spam
5. **Strong password policy** - Yêu cầu chữ hoa, số, ký tự đặc biệt
6. **Username validation** - Không cho phép ký tự đặc biệt
7. **SQL injection prevention** - Đã có sẵn với JPA
8. **XSS prevention** - Sanitize input

---

## 📞 Hỗ Trợ

Nếu gặp lỗi:
1. Kiểm tra Backend đã chạy: `http://localhost:8080/api/nguoidung`
2. Kiểm tra Logcat Android
3. Kiểm tra database có nhận được dữ liệu không
4. Xem file `FIX_API_CA_MAY_AO_VA_THAT.md` để cấu hình IP
