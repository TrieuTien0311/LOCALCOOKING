# FIX LỖI GỬI OTP ĐỔI MẬT KHẨU

## Vấn đề
- Khi nhấn nút "Gửi mã xác nhận" trong màn hình đổi mật khẩu, ứng dụng bị lỗi
- API không được gọi thành công

## Nguyên nhân
File `ApiService.java` thiếu 2 endpoint cho chức năng đổi mật khẩu:
- `/api/nguoidung/change-password/send-otp` (gửi OTP)
- `/api/nguoidung/change-password/verify` (xác thực OTP và đổi mật khẩu)

## Giải pháp đã thực hiện

### 1. Thêm endpoints vào ApiService.java
```java
// Đổi mật khẩu - Bước 1: Gửi OTP
@POST("api/nguoidung/change-password/send-otp")
Call<ChangePasswordResponse> sendOtpForChangePassword(@Body ChangePasswordRequest request);

// Đổi mật khẩu - Bước 2: Xác thực OTP và đổi mật khẩu
@POST("api/nguoidung/change-password/verify")
Call<ChangePasswordResponse> changePasswordWithOtp(@Body ChangePasswordWithOtpRequest request);
```

### 2. Thêm imports cần thiết
```java
import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;
```

### 3. Thêm logging interceptor vào RetrofitClient
Để dễ dàng debug các request/response:
```java
HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();
```

## Cách test

### 1. Chạy backend
```bash
cd BE
.\gradlew.bat bootRun
```

### 2. Build và chạy app Android
```bash
cd FE
.\gradlew.bat assembleDebug
```

### 3. Test trên app
1. Mở màn hình Đổi mật khẩu
2. Nhập thông tin:
   - Email: `thaovyn0312@gmail.com`
   - Mật khẩu hiện tại: `1234567`
   - Mật khẩu mới: `12345678`
   - Xác nhận mật khẩu mới: `12345678`
3. Nhấn "Gửi mã xác nhận"
4. Kiểm tra email để nhận mã OTP
5. Nhập mã OTP vào màn hình tiếp theo

## Kiểm tra logs
Xem Logcat với tag `CHANGE_PASSWORD` để debug:
```
D/CHANGE_PASSWORD: Email: thaovyn0312@gmail.com
D/CHANGE_PASSWORD: Current Password: 1234567
D/CHANGE_PASSWORD: New Password: 12345678
D/CHANGE_PASSWORD: JSON Request: {...}
D/CHANGE_PASSWORD: Response code: 200
D/CHANGE_PASSWORD: Success: true
```

## Files đã sửa
1. `FE/app/src/main/java/com/example/localcooking_v3t/api/ApiService.java` - Thêm endpoints
2. `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java` - Thêm logging

## Lưu ý
- Backend phải chạy trước khi test app
- Email service đã được cấu hình với Gmail SMTP
- OTP có hiệu lực 5 phút
- Đảm bảo IP address đúng (10.0.2.2 cho emulator, 192.168.137.1 cho điện thoại thật)
