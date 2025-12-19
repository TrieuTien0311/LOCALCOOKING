# CẢI TIẾN THÔNG BÁO LỖI - ĐỔI MẬT KHẨU

## Vấn đề trước đây
- Thông báo lỗi không rõ ràng
- Không phân loại được loại lỗi
- Khó debug khi có vấn đề

## Cải tiến đã thực hiện

### 1. **ChangePassword.java** - Màn hình nhập thông tin

#### Thông báo thành công
```java
✓ Mã OTP đã được gửi đến email user@example.com
```

#### Thông báo lỗi từ server
```java
✗ Lỗi: Email không tồn tại trong hệ thống
✗ Lỗi: Mật khẩu hiện tại không đúng
✗ Lỗi: Mật khẩu mới phải khác mật khẩu hiện tại
✗ Lỗi: Tài khoản đã bị khóa
```

#### Thông báo lỗi kết nối
```java
// Không có kết nối mạng
Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.

// Server không phản hồi
Lỗi kết nối: Timeout. Server không phản hồi.

// Backend chưa chạy
Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra backend đã chạy chưa.

// Lỗi khác
Lỗi kết nối: [Chi tiết lỗi]
```

#### Thông báo lỗi HTTP
```java
✗ Lỗi 400: Bad Request
✗ Lỗi 401: Unauthorized
✗ Lỗi 404: Not Found
✗ Lỗi 500: Internal Server Error
✗ Lỗi không xác định (Code: 503)
```

---

### 2. **ChangePasswordOtp.java** - Màn hình nhập OTP

#### Thông báo thành công
```java
Đổi mật khẩu thành công!
```

#### Thông báo lỗi từ server
```java
✗ Lỗi xác thực: Mã OTP không hợp lệ hoặc đã hết hạn
✗ Lỗi xác thực: Email không tồn tại trong hệ thống
```

#### Thông báo lỗi kết nối
```java
// Không có kết nối mạng
Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.

// Server không phản hồi
Lỗi kết nối: Timeout. Server không phản hồi.

// Backend chưa chạy
Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra backend đã chạy chưa.

// Lỗi khác
Lỗi kết nối: [Chi tiết lỗi]
```

#### Thông báo lỗi HTTP
```java
✗ Lỗi 400: [Chi tiết từ server]
✗ Lỗi 401: Unauthorized
✗ Lỗi không xác định (Code: 500)
```

---

## Code cải tiến

### Phân loại lỗi kết nối
```java
@Override
public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
    // Phân loại lỗi kết nối
    String errorMsg;
    if (t instanceof java.net.UnknownHostException) {
        errorMsg = "Lỗi kết nối: Không thể kết nối đến server. " +
                   "Vui lòng kiểm tra kết nối mạng.";
    } else if (t instanceof java.net.SocketTimeoutException) {
        errorMsg = "Lỗi kết nối: Timeout. Server không phản hồi.";
    } else if (t instanceof java.net.ConnectException) {
        errorMsg = "Lỗi kết nối: Không thể kết nối đến server. " +
                   "Vui lòng kiểm tra backend đã chạy chưa.";
    } else {
        errorMsg = "Lỗi kết nối: " + t.getMessage();
    }
    
    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
}
```

### Xử lý error response từ server
```java
if (response.isSuccessful() && response.body() != null) {
    ChangePasswordResponse result = response.body();
    
    if (result.isSuccess()) {
        // Thành công
        Toast.makeText(this, "✓ " + result.getMessage(), LONG).show();
    } else {
        // Lỗi từ server
        String errorMsg = "✗ Lỗi: " + result.getMessage();
        Toast.makeText(this, errorMsg, LONG).show();
    }
} else {
    // Parse error body
    try {
        if (response.errorBody() != null) {
            String errorBodyString = response.errorBody().string();
            
            // Parse JSON error response
            Gson gson = new Gson();
            ChangePasswordResponse errorResponse = 
                gson.fromJson(errorBodyString, ChangePasswordResponse.class);
            
            if (errorResponse != null && errorResponse.getMessage() != null) {
                String errorMsg = "✗ Lỗi: " + errorResponse.getMessage();
                Toast.makeText(this, errorMsg, LONG).show();
            } else {
                String errorMsg = "✗ Lỗi " + response.code() + ": " + errorBodyString;
                Toast.makeText(this, errorMsg, LONG).show();
            }
        } else {
            String errorMsg = "✗ Lỗi " + response.code() + ": " + response.message();
            Toast.makeText(this, errorMsg, LONG).show();
        }
    } catch (Exception e) {
        String errorMsg = "✗ Lỗi không xác định (Code: " + response.code() + ")";
        Toast.makeText(this, errorMsg, LONG).show();
    }
}
```

### Debug logging
```java
// Log request
android.util.Log.d("CHANGE_PASSWORD", "=== REQUEST DEBUG ===");
android.util.Log.d("CHANGE_PASSWORD", "Email: " + email);
android.util.Log.d("CHANGE_PASSWORD", "API URL: " + url);

// Log response
android.util.Log.d("CHANGE_PASSWORD", "Response code: " + response.code());
android.util.Log.d("CHANGE_PASSWORD", "Success: " + result.isSuccess());
android.util.Log.d("CHANGE_PASSWORD", "Message: " + result.getMessage());

// Log error
android.util.Log.e("CHANGE_PASSWORD", "Error: " + errorMsg);
```

---

## Các loại lỗi được xử lý

### 1. Lỗi Validation (Client-side)
```
❌ Vui lòng nhập email
❌ Vui lòng điền đầy đủ thông tin
❌ Mật khẩu mới không khớp
❌ Vui lòng nhập đầy đủ mã OTP
```

### 2. Lỗi Business Logic (Server-side)
```
✗ Lỗi: Email không tồn tại trong hệ thống
✗ Lỗi: Mật khẩu hiện tại không đúng
✗ Lỗi: Mật khẩu mới phải khác mật khẩu hiện tại
✗ Lỗi: Tài khoản đã bị khóa
✗ Lỗi xác thực: Mã OTP không hợp lệ hoặc đã hết hạn
```

### 3. Lỗi Kết nối (Network)
```
🔌 Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.
⏱️ Lỗi kết nối: Timeout. Server không phản hồi.
🖥️ Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra backend đã chạy chưa.
```

### 4. Lỗi HTTP (Server Error)
```
✗ Lỗi 400: Bad Request
✗ Lỗi 401: Unauthorized
✗ Lỗi 404: Not Found
✗ Lỗi 500: Internal Server Error
✗ Lỗi không xác định (Code: 503)
```

---

## Lợi ích

### 1. Người dùng
- ✅ Biết chính xác lỗi gì
- ✅ Biết cách khắc phục
- ✅ Trải nghiệm tốt hơn

### 2. Developer
- ✅ Dễ debug
- ✅ Log chi tiết trong Logcat
- ✅ Phát hiện lỗi nhanh

### 3. Support
- ✅ Người dùng báo lỗi rõ ràng
- ✅ Dễ tái hiện lỗi
- ✅ Giải quyết nhanh hơn

---

## Ví dụ thực tế

### Scenario 1: Email không tồn tại
```
User nhập: test@gmail.com
Backend response: {
  "success": false,
  "message": "Email không tồn tại trong hệ thống"
}
Toast hiển thị: ✗ Lỗi: Email không tồn tại trong hệ thống
```

### Scenario 2: Mật khẩu hiện tại sai
```
User nhập: wrongpassword
Backend response: {
  "success": false,
  "message": "Mật khẩu hiện tại không đúng"
}
Toast hiển thị: ✗ Lỗi: Mật khẩu hiện tại không đúng
```

### Scenario 3: OTP hết hạn
```
User nhập OTP sau 6 phút
Backend response: {
  "success": false,
  "message": "Mã OTP không hợp lệ hoặc đã hết hạn"
}
Toast hiển thị: ✗ Lỗi xác thực: Mã OTP không hợp lệ hoặc đã hết hạn
```

### Scenario 4: Backend chưa chạy
```
User nhấn "Gửi mã xác nhận"
Exception: ConnectException
Toast hiển thị: Lỗi kết nối: Không thể kết nối đến server. 
                Vui lòng kiểm tra backend đã chạy chưa.
```

### Scenario 5: Không có internet
```
User nhấn "Gửi mã xác nhận"
Exception: UnknownHostException
Toast hiển thị: Lỗi kết nối: Không thể kết nối đến server. 
                Vui lòng kiểm tra kết nối mạng.
```

---

## Files đã cập nhật

1. **FE/app/src/main/java/com/example/localcooking_v3t/ChangePassword.java**
   - Thêm phân loại lỗi kết nối
   - Cải thiện thông báo lỗi HTTP
   - Thêm icon ✓ và ✗ cho thông báo
   - Thêm debug logging

2. **FE/app/src/main/java/com/example/localcooking_v3t/ChangePasswordOtp.java**
   - Thêm phân loại lỗi kết nối
   - Cải thiện thông báo lỗi HTTP
   - Thêm icon ✗ cho thông báo lỗi
   - Thêm debug logging

---

## Kết quả
✅ Build thành công
✅ Thông báo lỗi rõ ràng và chi tiết
✅ Dễ debug với logging
✅ Trải nghiệm người dùng tốt hơn
