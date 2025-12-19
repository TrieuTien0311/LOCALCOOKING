# HOÀN THÀNH UI ĐỔI MẬT KHẨU

## Tổng quan
Đã cập nhật giao diện các màn hình đổi mật khẩu để đồng nhất với màn hình đăng nhập.

## Các màn hình đã hoàn thiện

### 1. Màn hình Đổi mật khẩu (ChangePassword)
**File**: `FE/app/src/main/res/layout/activity_change_password.xml`

**Các thành phần**:
- ✅ Logo và tên app "LocalCooking"
- ✅ Nút "Quay lại"
- ✅ Card với tiêu đề "Đổi mật khẩu"
- ✅ Ô nhập Email với floating label (style CustomTextInputLayout)
- ✅ Ô nhập Mật khẩu hiện tại với icon show/hide
- ✅ Link "Quên mật khẩu?"
- ✅ Ô nhập Mật khẩu mới với icon show/hide
- ✅ Ô nhập Xác nhận mật khẩu mới với icon show/hide
- ✅ Text thông báo "Chúng tôi sẽ gửi mã xác thực đến email này"
- ✅ Nút "Gửi mã xác nhận" (màu đỏ #B73F41)

**Tính năng**:
- Floating label khi focus vào ô input
- Toggle show/hide password cho 3 ô mật khẩu
- Validation đầy đủ trước khi gửi OTP
- Tự động điền email từ SharedPreferences nếu đã đăng nhập

### 2. Màn hình Nhập OTP (ChangePasswordOtp)
**File**: `FE/app/src/main/res/layout/activity_change_password_otp.xml`

**Các thành phần**:
- ✅ Logo và tên app "LocalCooking"
- ✅ Nút "Quay lại"
- ✅ Card với tiêu đề "Đổi mật khẩu"
- ✅ Text mô tả "Nhập mã OTP đã được gửi đến email của bạn"
- ✅ Tiêu đề "Nhập mã OTP"
- ✅ 6 ô nhập OTP (45x45dp, spacing 8dp)
- ✅ Nút "Xác nhận" (màu đỏ #B73F41)

**Tính năng**:
- 6 ô OTP riêng biệt, mỗi ô 1 số
- Tự động focus sang ô tiếp theo khi nhập
- Style đồng nhất với màn hình quên mật khẩu

## Style đã áp dụng

### CustomTextInputLayout
```xml
<style name="CustomTextInputLayout" parent="Widget.MaterialComponents.TextInputLayout.OutlinedBox">
    <item name="boxStrokeColor">#D4A574</item>
    <item name="boxStrokeWidth">2dp</item>
    <item name="boxCornerRadiusTopStart">8dp</item>
    <item name="boxCornerRadiusTopEnd">8dp</item>
    <item name="boxCornerRadiusBottomStart">8dp</item>
    <item name="boxCornerRadiusBottomEnd">8dp</item>
    <item name="hintTextColor">#D4A574</item>
</style>
```

### Màu sắc chính
- Background: `#F5E6D3` (be)
- Card background: `#FFFFFF` (trắng)
- Header card: `#F4C19A` (cam nhạt)
- Button: `#B73F41` (đỏ)
- Text chính: `#3D2817` (nâu đậm)
- Text phụ: `#666666` (xám)
- Border input: `#D4A574` (vàng nâu)

## Luồng hoạt động

1. **Màn hình Đổi mật khẩu**:
   - Người dùng nhập email, mật khẩu hiện tại, mật khẩu mới, xác nhận mật khẩu mới
   - Nhấn "Gửi mã xác nhận"
   - Hệ thống validate và gửi OTP qua email
   - Chuyển sang màn hình nhập OTP

2. **Màn hình Nhập OTP**:
   - Người dùng nhập 6 số OTP từ email
   - Nhấn "Xác nhận"
   - Hệ thống xác thực OTP và đổi mật khẩu
   - Thông báo thành công và quay về màn hình đăng nhập

## API Endpoints

### Gửi OTP
```
POST /api/nguoidung/change-password/send-otp
Body: {
  "email": "user@example.com",
  "matKhauHienTai": "oldpass",
  "matKhauMoi": "newpass",
  "xacNhanMatKhauMoi": "newpass"
}
```

### Xác thực OTP và đổi mật khẩu
```
POST /api/nguoidung/change-password/verify
Body: {
  "email": "user@example.com",
  "otp": "123456",
  "matKhauMoi": "newpass"
}
```

## Files đã cập nhật

### Frontend
1. `FE/app/src/main/res/layout/activity_change_password.xml` - Layout đổi mật khẩu
2. `FE/app/src/main/res/layout/activity_change_password_otp.xml` - Layout nhập OTP
3. `FE/app/src/main/java/com/example/localcooking_v3t/ChangePassword.java` - Logic xử lý
4. `FE/app/src/main/java/com/example/localcooking_v3t/ChangePasswordOtp.java` - Logic OTP
5. `FE/app/src/main/java/com/example/localcooking_v3t/api/ApiService.java` - API endpoints
6. `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java` - HTTP client

### Backend
1. `BE/src/main/java/com/android/be/controller/NguoiDungController.java` - Controller
2. `BE/src/main/java/com/android/be/service/NguoiDungService.java` - Business logic
3. `BE/src/main/java/com/android/be/service/OtpService.java` - OTP generation
4. `BE/src/main/java/com/android/be/service/EmailService.java` - Email sending
5. `BE/src/main/java/com/android/be/dto/ChangePasswordRequest.java` - DTO
6. `BE/src/main/java/com/android/be/dto/ChangePasswordWithOtpRequest.java` - DTO

## Lưu ý quan trọng

### Database
- Backend cần kết nối SQL Server thành công
- Đã cấu hình hỗ trợ cả SQL Authentication và Windows Authentication
- Kiểm tra mật khẩu SQL Server trong `application.properties`

### Email
- Email service đã được cấu hình với Gmail SMTP
- Email: `LocalCooking23@gmail.com`
- OTP có hiệu lực 5 phút

### Network
- Emulator: sử dụng IP `10.0.2.2:8080`
- Điện thoại thật: sử dụng IP `192.168.137.1:8080` (Hotspot)
- RetrofitClient tự động detect môi trường

## Test

### Bước 1: Chạy Backend
```bash
cd BE
.\gradlew.bat bootRun
```

### Bước 2: Build và chạy App
```bash
cd FE
.\gradlew.bat assembleDebug
```

### Bước 3: Test trên App
1. Mở màn hình Đổi mật khẩu
2. Nhập:
   - Email: `thaovyn0312@gmail.com`
   - Mật khẩu hiện tại: `1234567`
   - Mật khẩu mới: `12345678`
   - Xác nhận: `12345678`
3. Nhấn "Gửi mã xác nhận"
4. Kiểm tra email để lấy OTP
5. Nhập OTP vào màn hình tiếp theo
6. Nhấn "Xác nhận"

## Kết quả
✅ UI đã đồng nhất với màn hình đăng nhập
✅ Floating label hoạt động tốt
✅ Show/hide password hoạt động
✅ OTP boxes có style đẹp và dễ sử dụng
✅ API integration hoàn chỉnh
✅ Build thành công không lỗi
