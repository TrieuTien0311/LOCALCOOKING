# HOÀN THÀNH UI QUÊN MẬT KHẨU

## Tổng quan
Đã cập nhật giao diện các màn hình quên mật khẩu để đồng nhất với màn hình đăng nhập và đăng ký.

## Các màn hình đã hoàn thiện

### 1. Màn hình Quên mật khẩu (ForgotPassword)
**File**: `FE/app/src/main/res/layout/activity_forgot_password.xml`

**Các thành phần**:
- ✅ Logo và tên app "LocalCooking"
- ✅ Nút "Quay lại"
- ✅ Card với tiêu đề "Quên mật khẩu"
- ✅ Text mô tả "Nhập email để nhận liên kết đặt lại mật khẩu"
- ✅ Ô nhập Email với floating label (style CustomTextInputLayout)
- ✅ Text thông báo "Chúng tôi sẽ gửi mã xác thực đến email này"
- ✅ Nút "Gửi liên kết" (màu đỏ #B73F41)
- ✅ Link "Quay lại trang đăng nhập"

**Thay đổi**:
- ❌ Trước: Dùng `@drawable/input_bg` với padding tùy chỉnh
- ✅ Sau: Dùng `@style/CustomTextInputLayout` với floating label

### 2. Màn hình Xác thực OTP và Đặt mật khẩu mới (OtpVerification)
**File**: `FE/app/src/main/res/layout/activity_otp_verification.xml`

**Các thành phần**:
- ✅ Logo và tên app "LocalCooking"
- ✅ Nút "Quay lại"
- ✅ Card với tiêu đề "Quên mật khẩu"
- ✅ Text mô tả "Nhập mã OTP đã được gửi đến email của bạn"
- ✅ Tiêu đề "Nhập mã OTP"
- ✅ 6 ô nhập OTP (45x45dp, spacing 8dp)
- ✅ Ô nhập Mật khẩu mới với floating label và icon show/hide
- ✅ Ô nhập Xác nhận mật khẩu mới với floating label và icon show/hide
- ✅ Nút "Xác nhận" (màu đỏ #B73F41)

**Thay đổi**:
- ❌ Trước: Dùng `@drawable/input_bg` với `passwordToggleEnabled`
- ✅ Sau: Dùng `@style/CustomTextInputLayout` với `drawableRight` icon
- ✅ Tăng kích thước OTP boxes từ 40x40dp lên 45x45dp
- ✅ Tăng spacing từ 5dp lên 8dp

### 3. Java Code - OtpVerification.java
**File**: `FE/app/src/main/java/com/example/localcooking_v3t/OtpVerification.java`

**Tính năng đã thêm**:
- ✅ Toggle show/hide password cho ô "Mật khẩu mới"
- ✅ Toggle show/hide password cho ô "Xác nhận mật khẩu mới"
- ✅ Tự động chuyển icon giữa `icon_eye` và `icon_eye_hide_tt`
- ✅ Giữ cursor position khi toggle

**Code mới**:
```java
private boolean isPasswordVisible1 = false;
private boolean isPasswordVisible2 = false;

private void setupPasswordToggles() {
    // Toggle cho Mật khẩu mới
    idMatKhauMoi.setOnTouchListener((v, event) -> {
        // Logic toggle password visibility
    });
    
    // Toggle cho Xác nhận mật khẩu
    idXacNhanMatKhau.setOnTouchListener((v, event) -> {
        // Logic toggle password visibility
    });
}
```

## Style đã áp dụng

### CustomTextInputLayout
Tất cả các ô input giờ đều sử dụng style này:
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

### Đặc điểm
- Border màu vàng nâu (#D4A574)
- Border width 2dp
- Corner radius 8dp
- Floating label animation
- Hint color thay đổi khi focus

## So sánh trước và sau

### Trước
```xml
<!-- Email input cũ -->
<TextInputEditText
    android:background="@drawable/input_bg"
    android:paddingLeft="30dp"
    android:paddingTop="10dp"
    android:paddingEnd="15dp"
    android:paddingBottom="10dp" />

<!-- Password input cũ -->
<TextInputLayout
    app:passwordToggleEnabled="true"
    app:passwordToggleTint="#666666">
    <TextInputEditText
        android:background="@drawable/input_bg"
        android:paddingLeft="30dp"
        android:paddingEnd="50dp" />
</TextInputLayout>
```

### Sau
```xml
<!-- Email input mới -->
<TextInputLayout
    style="@style/CustomTextInputLayout"
    android:hint="Email">
    <TextInputEditText
        android:padding="16dp"
        android:textSize="16sp" />
</TextInputLayout>

<!-- Password input mới -->
<TextInputLayout
    style="@style/CustomTextInputLayout"
    android:hint="Mật khẩu mới">
    <TextInputEditText
        android:drawableRight="@drawable/icon_eye_hide_tt"
        android:padding="16dp"
        android:textSize="16sp" />
</TextInputLayout>
```

## Luồng hoạt động

### Quên mật khẩu
1. **Màn hình ForgotPassword**:
   - Người dùng nhập email
   - Nhấn "Gửi liên kết"
   - Hệ thống gửi OTP qua email
   - Chuyển sang màn hình OtpVerification

2. **Màn hình OtpVerification**:
   - Người dùng nhập 6 số OTP
   - Nhấn "Xác nhận" lần 1 → Xác thực OTP
   - Nếu OTP đúng, hiển thị 2 ô nhập mật khẩu
   - Người dùng nhập mật khẩu mới và xác nhận
   - Nhấn "Xác nhận" lần 2 → Đổi mật khẩu
   - Thành công → Chuyển về màn hình đăng nhập

## Files đã cập nhật

### Layout XML
1. `FE/app/src/main/res/layout/activity_forgot_password.xml` - Màn hình quên mật khẩu
2. `FE/app/src/main/res/layout/activity_otp_verification.xml` - Màn hình OTP và đặt mật khẩu mới

### Java Code
1. `FE/app/src/main/java/com/example/localcooking_v3t/OtpVerification.java` - Thêm password toggle

## Lợi ích của việc cập nhật

### 1. Đồng nhất UI/UX
- Tất cả màn hình giờ có cùng style input
- Người dùng dễ nhận biết và sử dụng
- Trải nghiệm mượt mà hơn

### 2. Floating Label
- Label tự động di chuyển lên trên khi focus
- Tiết kiệm không gian
- Hiện đại và chuyên nghiệp

### 3. Show/Hide Password
- Người dùng có thể kiểm tra mật khẩu đã nhập
- Giảm lỗi nhập sai
- Tăng trải nghiệm người dùng

### 4. Dễ bảo trì
- Sử dụng style chung
- Thay đổi 1 chỗ, áp dụng toàn bộ
- Code sạch và nhất quán

## Test

### Bước 1: Test màn hình Quên mật khẩu
1. Mở app → Đăng nhập → "Quên mật khẩu?"
2. Nhập email: `thaovyn0312@gmail.com`
3. Nhấn "Gửi liên kết"
4. Kiểm tra:
   - ✅ Floating label hoạt động
   - ✅ Border màu vàng nâu khi focus
   - ✅ Email được gửi thành công

### Bước 2: Test màn hình OTP
1. Kiểm tra email để lấy OTP (6 số)
2. Nhập OTP vào 6 ô
3. Nhấn "Xác nhận"
4. Kiểm tra:
   - ✅ OTP boxes có kích thước 45x45dp
   - ✅ Spacing 8dp giữa các ô
   - ✅ Tự động focus sang ô tiếp theo
   - ✅ Hiển thị 2 ô mật khẩu sau khi OTP đúng

### Bước 3: Test đổi mật khẩu
1. Nhập mật khẩu mới: `12345678`
2. Nhập xác nhận: `12345678`
3. Nhấn icon mắt để show/hide password
4. Nhấn "Xác nhận"
5. Kiểm tra:
   - ✅ Floating label hoạt động
   - ✅ Show/hide password hoạt động
   - ✅ Icon thay đổi khi toggle
   - ✅ Đổi mật khẩu thành công
   - ✅ Chuyển về màn hình đăng nhập

## Kết quả
✅ UI đã đồng nhất với màn hình đăng nhập và đăng ký
✅ Floating label hoạt động tốt trên tất cả ô input
✅ Show/hide password hoạt động cho 2 ô mật khẩu
✅ OTP boxes có kích thước và spacing phù hợp
✅ Build thành công không lỗi
✅ Code sạch và dễ bảo trì

## Tổng kết toàn bộ dự án

### Các màn hình đã hoàn thiện
1. ✅ Login - Đăng nhập
2. ✅ Register - Đăng ký
3. ✅ ForgotPassword - Quên mật khẩu
4. ✅ OtpVerification - Xác thực OTP và đặt mật khẩu mới
5. ✅ ChangePassword - Đổi mật khẩu
6. ✅ ChangePasswordOtp - Xác thực OTP đổi mật khẩu

### Style chung
- CustomTextInputLayout cho tất cả input fields
- Floating label animation
- Show/hide password với icon toggle
- OTP boxes 45x45dp với spacing 8dp
- Button màu đỏ #B73F41
- Background màu be #F5E6D3

### Backend API
- ✅ Login/Register
- ✅ Forgot Password (send OTP, verify OTP, reset password)
- ✅ Change Password (send OTP, verify OTP, change password)
- ✅ Email service với Gmail SMTP
- ✅ OTP service với expiry 5 phút
