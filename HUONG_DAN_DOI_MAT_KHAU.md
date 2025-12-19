# HƯỚNG DẪN ĐỔI MẬT KHẨU VỚI OTP

## Luồng Đổi Mật Khẩu

```
[Người dùng nhập thông tin] 
    ↓
[Nhập: Email, Mật khẩu hiện tại, Mật khẩu mới, Xác nhận mật khẩu mới]
    ↓
[Gửi request đến API: /api/nguoidung/change-password/send-otp]
    ↓
[Backend kiểm tra: Email tồn tại? Mật khẩu hiện tại đúng? Mật khẩu mới hợp lệ?]
    ↓
[Tạo OTP 6 số và gửi email]
    ↓
[Người dùng nhận OTP qua email]
    ↓
[Người dùng nhập OTP]
    ↓
[Gửi request đến API: /api/nguoidung/change-password/verify]
    ↓
[Backend xác thực OTP]
    ↓
[Cập nhật mật khẩu mới vào database]
    ↓
[Thành công! Người dùng có thể đăng nhập với mật khẩu mới]
```

---

## API Endpoints

### 1. Gửi OTP để đổi mật khẩu

**Endpoint:** `POST /api/nguoidung/change-password/send-otp`

**Request Body:**
```json
{
  "email": "user@example.com",
  "matKhauHienTai": "password123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Mã OTP đã được gửi đến email user@example.com",
  "email": "user@example.com"
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Mật khẩu hiện tại không đúng"
}
```

**Các lỗi có thể xảy ra:**
- "Email không được để trống"
- "Mật khẩu hiện tại không được để trống"
- "Mật khẩu mới không được để trống"
- "Xác nhận mật khẩu mới không được để trống"
- "Mật khẩu mới và xác nhận mật khẩu không khớp"
- "Mật khẩu mới phải khác mật khẩu hiện tại"
- "Email không tồn tại trong hệ thống"
- "Mật khẩu hiện tại không đúng"
- "Tài khoản đã bị khóa"
- "Lỗi gửi email: ..."

---

### 2. Xác thực OTP và đổi mật khẩu

**Endpoint:** `POST /api/nguoidung/change-password/verify`

**Request Body:**
```json
{
  "email": "user@example.com",
  "matKhauHienTai": "password123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456",
  "otp": "123456"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đổi mật khẩu thành công"
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Mã OTP không hợp lệ hoặc đã hết hạn"
}
```

**Các lỗi có thể xảy ra:**
- "Email không được để trống"
- "Mã OTP không được để trống"
- "Mật khẩu mới không được để trống"
- "Mã OTP không hợp lệ hoặc đã hết hạn"
- "Email không tồn tại trong hệ thống"

---

## Test với Postman

### Bước 1: Gửi OTP

1. Mở Postman
2. Tạo request mới: **POST** `http://localhost:8080/api/nguoidung/change-password/send-otp`
3. Chọn tab **Headers**, thêm:
   - Key: `Content-Type`
   - Value: `application/json`
4. Chọn tab **Body** → **raw** → **JSON**
5. Nhập:
```json
{
  "email": "admin@localcooking.vn",
  "matKhauHienTai": "admin123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456"
}
```
6. Click **Send**
7. Kiểm tra email để lấy mã OTP (có hiệu lực 5 phút)

### Bước 2: Xác thực OTP và đổi mật khẩu

1. Tạo request mới: **POST** `http://localhost:8080/api/nguoidung/change-password/verify`
2. Chọn tab **Headers**, thêm:
   - Key: `Content-Type`
   - Value: `application/json`
3. Chọn tab **Body** → **raw** → **JSON**
4. Nhập (thay `123456` bằng OTP nhận được):
```json
{
  "email": "admin@localcooking.vn",
  "matKhauHienTai": "admin123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456",
  "otp": "123456"
}
```
5. Click **Send**
6. Nếu thành công, mật khẩu đã được đổi!

### Bước 3: Test đăng nhập với mật khẩu mới

1. Tạo request: **POST** `http://localhost:8080/api/nguoidung/login`
2. Body:
```json
{
  "email": "admin@localcooking.vn",
  "matKhau": "newpassword456"
}
```
3. Nếu đăng nhập thành công → Đổi mật khẩu hoàn tất!

---

## Validation Rules

### Email
- ✅ Không được để trống
- ✅ Phải tồn tại trong hệ thống
- ✅ Tài khoản phải ở trạng thái "HoatDong"

### Mật khẩu hiện tại
- ✅ Không được để trống
- ✅ Phải khớp với mật khẩu trong database

### Mật khẩu mới
- ✅ Không được để trống
- ✅ Phải khác mật khẩu hiện tại
- ✅ Phải khớp với xác nhận mật khẩu mới

### OTP
- ✅ Không được để trống
- ✅ Phải là mã 6 số
- ✅ Có hiệu lực trong 5 phút
- ✅ Chỉ sử dụng được 1 lần

---

## Lưu ý quan trọng

1. **OTP có hiệu lực 5 phút**: Sau 5 phút, OTP sẽ hết hạn và cần gửi lại
2. **OTP chỉ dùng 1 lần**: Sau khi xác thực thành công, OTP sẽ bị xóa
3. **Mật khẩu mới phải khác mật khẩu cũ**: Không thể đổi sang mật khẩu giống mật khẩu hiện tại
4. **Email phải tồn tại**: Chỉ có thể đổi mật khẩu cho tài khoản đã đăng ký
5. **Tài khoản phải hoạt động**: Tài khoản bị khóa không thể đổi mật khẩu

---

## Tích hợp vào Android App

### Retrofit API Interface

Thêm vào `ApiService.java`:

```java
// Gửi OTP để đổi mật khẩu
@POST("nguoidung/change-password/send-otp")
Call<Map<String, Object>> sendOtpForChangePassword(@Body ChangePasswordRequest request);

// Xác thực OTP và đổi mật khẩu
@POST("nguoidung/change-password/verify")
Call<Map<String, Object>> changePasswordWithOtp(@Body ChangePasswordWithOtpRequest request);
```

### DTO Classes

Tạo `ChangePasswordRequest.java`:
```java
public class ChangePasswordRequest {
    private String email;
    private String matKhauHienTai;
    private String matKhauMoi;
    private String xacNhanMatKhauMoi;
    
    // Constructor, getters, setters
}
```

Tạo `ChangePasswordWithOtpRequest.java`:
```java
public class ChangePasswordWithOtpRequest {
    private String email;
    private String matKhauHienTai;
    private String matKhauMoi;
    private String xacNhanMatKhauMoi;
    private String otp;
    
    // Constructor, getters, setters
}
```

### UI Flow

1. **Màn hình Đổi Mật Khẩu:**
   - EditText: Email
   - EditText: Mật khẩu hiện tại (type: password)
   - EditText: Mật khẩu mới (type: password)
   - EditText: Xác nhận mật khẩu mới (type: password)
   - Button: "Gửi mã OTP"

2. **Sau khi nhấn "Gửi mã OTP":**
   - Hiện Dialog hoặc màn hình mới với:
   - EditText: Nhập mã OTP (6 số)
   - Button: "Xác nhận"
   - TextView: "Mã OTP đã được gửi đến email xxx"

3. **Sau khi xác nhận OTP thành công:**
   - Hiện thông báo "Đổi mật khẩu thành công"
   - Chuyển về màn hình đăng nhập

---

## Troubleshooting

### Lỗi: "Mật khẩu hiện tại không đúng"
→ Kiểm tra lại mật khẩu hiện tại của user trong database

### Lỗi: "Mã OTP không hợp lệ hoặc đã hết hạn"
→ OTP đã hết hạn (>5 phút) hoặc nhập sai, gửi lại OTP mới

### Lỗi: "Lỗi gửi email"
→ Kiểm tra cấu hình email trong `application.properties`

### Không nhận được email OTP
→ Kiểm tra:
- Thư mục Spam/Junk
- Cấu hình SMTP trong `application.properties`
- Email có tồn tại và hợp lệ không

---

## Security Notes

⚠️ **Lưu ý bảo mật:**

1. **Mật khẩu chưa được mã hóa**: Hiện tại mật khẩu lưu dạng plain text trong database. Nên sử dụng BCrypt để hash mật khẩu.

2. **Rate limiting**: Nên giới hạn số lần gửi OTP để tránh spam email.

3. **HTTPS**: Trong production, phải sử dụng HTTPS để bảo vệ dữ liệu truyền tải.

4. **Token-based auth**: Sau khi đổi mật khẩu, nên invalidate tất cả session/token cũ.
