# Fix API Cho Cả Máy Ảo Và Điện Thoại Thật

## 🎯 Giải Pháp

Đã cập nhật `RetrofitClient.java` để **tự động phát hiện** môi trường:
- **Máy ảo (Emulator)**: Dùng `http://10.0.2.2:8080/`
- **Điện thoại thật**: Dùng `http://192.168.1.X:8080/`

---

## ⚙️ Bước 1: Lấy IP Máy Tính

### Windows:
Mở **Command Prompt** và chạy:
```cmd
ipconfig
```

Tìm dòng **IPv4 Address** trong phần **Wireless LAN adapter Wi-Fi**:
```
Wireless LAN adapter Wi-Fi:
   IPv4 Address. . . . . . . . . . . : 192.168.1.100
```

**Ghi lại IP này!** Ví dụ: `192.168.1.100`

---

## 🔧 Bước 2: Cập Nhật IP Trong Code

Mở file: `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java`

Tìm dòng:
```java
private static final String IP_MAY_TINH = "192.168.1.100"; // <-- THAY ĐỔI Ở ĐÂY
```

**Thay đổi** `192.168.1.100` thành IP máy tính của bạn.

### Ví dụ:
Nếu IP của bạn là `192.168.1.105`:
```java
private static final String IP_MAY_TINH = "192.168.1.105";
```

---

## 🚀 Bước 3: Chạy Backend

```bash
cd BE
./gradlew bootRun
```

Hoặc trên Windows:
```cmd
cd BE
gradlew.bat bootRun
```

**Kiểm tra Backend:**
Mở trình duyệt, truy cập: `http://localhost:8080/api/nguoidung`

Nếu thấy JSON → Backend OK ✅

---

## 📱 Bước 4: Test Trên Máy Ảo

1. **Sync Gradle** trong Android Studio
2. **Rebuild Project**
3. Chọn **Emulator** trong device list
4. Click **Run**

**Kết quả:**
- App sẽ tự động dùng `http://10.0.2.2:8080/`
- Đăng nhập với: `admin@localcooking.vn` / `admin123`
- Nếu thành công → Hiển thị "Chào admin" ✅

---

## 📲 Bước 5: Test Trên Điện Thoại Thật

### 5.1. Đảm bảo cùng WiFi
- Điện thoại và máy tính phải cùng mạng WiFi

### 5.2. Test kết nối
Mở Chrome trên điện thoại, truy cập:
```
http://192.168.1.100:8080/api/nguoidung
```
*(Thay bằng IP của bạn)*

Nếu thấy JSON → Kết nối OK ✅

### 5.3. Chạy app
1. Kết nối điện thoại qua USB
2. Bật **USB Debugging**
3. Chọn điện thoại trong device list
4. Click **Run**

**Kết quả:**
- App sẽ tự động dùng `http://192.168.1.100:8080/`
- Đăng nhập thành công ✅

---

## 🔍 Debug

### Xem Log để biết đang dùng URL nào:

1. Mở **Logcat** trong Android Studio
2. Filter: `LOGIN_DEBUG`
3. Sẽ thấy log:
```
API URL: http://10.0.2.2:8080/        (Máy ảo)
hoặc
API URL: http://192.168.1.100:8080/   (Điện thoại thật)
```

---

## 🐛 Xử Lý Lỗi

### Lỗi: "Unable to resolve host"

**Nguyên nhân:**
- IP máy tính sai
- Không cùng WiFi
- Backend chưa chạy
- Firewall chặn

**Giải pháp:**

#### 1. Kiểm tra IP
```cmd
ipconfig
```
Đảm bảo IP đúng và cập nhật vào `IP_MAY_TINH`

#### 2. Kiểm tra Backend
Truy cập: `http://localhost:8080/api/nguoidung`

#### 3. Kiểm tra Firewall
**Tắt tạm thời để test:**
- Mở **Windows Defender Firewall**
- Click **Turn Windows Defender Firewall on or off**
- Chọn **Turn off** (Private network)
- Click **OK**

**Hoặc tạo rule:**
1. **Windows Defender Firewall** → **Advanced settings**
2. **Inbound Rules** → **New Rule**
3. **Port** → **TCP** → **8080**
4. **Allow the connection**
5. Tick tất cả (Domain, Private, Public)
6. Đặt tên: **Spring Boot API**

#### 4. Test từ điện thoại
Mở Chrome trên điện thoại:
```
http://192.168.1.100:8080/api/nguoidung
```

Nếu không thấy gì → Vấn đề ở mạng/firewall

---

## 📋 Checklist

### Trước khi chạy:
- [ ] Lấy IP máy tính: `ipconfig`
- [ ] Cập nhật `IP_MAY_TINH` trong `RetrofitClient.java`
- [ ] Backend đang chạy: `./gradlew bootRun`
- [ ] Test Backend: `http://localhost:8080/api/nguoidung`
- [ ] Firewall cho phép port 8080
- [ ] Sync Gradle và Rebuild Project

### Test máy ảo:
- [ ] Chọn Emulator
- [ ] Run app
- [ ] Xem Logcat: `API URL: http://10.0.2.2:8080/`
- [ ] Đăng nhập thành công

### Test điện thoại thật:
- [ ] Điện thoại và máy tính cùng WiFi
- [ ] Test trình duyệt: `http://192.168.1.X:8080/api/nguoidung`
- [ ] Kết nối USB và bật USB Debugging
- [ ] Run app
- [ ] Xem Logcat: `API URL: http://192.168.1.X:8080/`
- [ ] Đăng nhập thành công

---

## 💡 Cách Hoạt Động

### Code tự động phát hiện:
```java
private static boolean isEmulator() {
    return Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk".equals(Build.PRODUCT);
}
```

### Chọn URL:
```java
private static final String BASE_URL = isEmulator() 
        ? "http://10.0.2.2:8080/"           // Máy ảo
        : "http://" + IP_MAY_TINH + ":8080/"; // Điện thoại thật
```

---

## 🎉 Kết Quả

Sau khi làm đúng:
- ✅ **Máy ảo**: Tự động dùng `10.0.2.2`
- ✅ **Điện thoại thật**: Tự động dùng IP máy tính
- ✅ **Không cần thay đổi code** khi chuyển đổi
- ✅ **Đăng nhập thành công** trên cả 2 môi trường

---

## 📞 Nếu Vẫn Lỗi

### Cung cấp thông tin:
1. **IP máy tính** (từ `ipconfig`)
2. **Log từ Logcat** (filter: `LOGIN_DEBUG`)
3. **Screenshot lỗi**
4. **Backend có chạy không?** (test `http://localhost:8080`)

### Test nhanh:
```cmd
# Trên máy tính
curl http://localhost:8080/api/nguoidung

# Trên điện thoại (Chrome)
http://192.168.1.100:8080/api/nguoidung
```

---

## 🔑 Tài Khoản Test

| Email | Password | Vai trò |
|-------|----------|---------|
| admin@localcooking.vn | admin123 | Admin |
| levancuong@gmail.com | hv123 | HocVien |
| nguyenvanan@gmail.com | gv123 | GiaoVien |
