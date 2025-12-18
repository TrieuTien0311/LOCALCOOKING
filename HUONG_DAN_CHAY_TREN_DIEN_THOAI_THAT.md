# Hướng Dẫn Chạy API Trên Điện Thoại Thật

## 📱 Yêu Cầu
- Điện thoại và máy tính phải cùng mạng WiFi
- Backend đang chạy trên máy tính
- USB Debugging đã bật trên điện thoại

---

## 🔍 Bước 1: Lấy IP Máy Tính

### Windows:
1. Mở **Command Prompt** (CMD)
2. Gõ lệnh:
```cmd
ipconfig
```

3. Tìm dòng **IPv4 Address** trong phần **Wireless LAN adapter Wi-Fi**:
```
Wireless LAN adapter Wi-Fi:
   IPv4 Address. . . . . . . . . . . : 192.168.1.100
```

### Ví dụ IP thường gặp:
- `192.168.1.X` (Router TP-Link, D-Link)
- `192.168.0.X` (Router Tenda, Asus)
- `10.0.0.X` (Router Apple)

**Lưu ý:** Chỉ lấy IP của WiFi, không phải Ethernet hay VirtualBox

---

## ⚙️ Bước 2: Cấu Hình RetrofitClient

Mở file: `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java`

### Thay đổi BASE_URL:

**Trước (cho Emulator):**
```java
private static final String BASE_URL = "http://10.0.2.2:8080/";
```

**Sau (cho Điện thoại thật):**
```java
private static final String BASE_URL = "http://192.168.1.100:8080/";
```
*(Thay `192.168.1.100` bằng IP máy tính của bạn)*

### Code đầy đủ:
```java
public class RetrofitClient {
    // Emulator: Sử dụng 10.0.2.2
    // private static final String BASE_URL = "http://10.0.2.2:8080/";
    
    // Real Device: Thay bằng IP máy tính của bạn
    private static final String BASE_URL = "http://192.168.1.100:8080/";
    
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
```

---

## 🔥 Bước 3: Tắt Firewall (Nếu Cần)

### Windows Firewall:
1. Mở **Windows Defender Firewall**
2. Click **Allow an app or feature through Windows Defender Firewall**
3. Click **Change settings**
4. Tìm **Java(TM) Platform SE binary**
5. Tick cả **Private** và **Public**
6. Click **OK**

### Hoặc tạo rule mới:
1. Mở **Windows Defender Firewall**
2. Click **Advanced settings**
3. Click **Inbound Rules** → **New Rule**
4. Chọn **Port** → Next
5. Chọn **TCP** → Specific local ports: **8080**
6. Chọn **Allow the connection**
7. Tick tất cả (Domain, Private, Public)
8. Đặt tên: **Spring Boot API**
9. Click **Finish**

---

## 🚀 Bước 4: Chạy Backend

```bash
cd BE
./gradlew bootRun
```

Hoặc trên Windows:
```cmd
cd BE
gradlew.bat bootRun
```

**Kiểm tra Backend đã chạy:**
- Mở trình duyệt trên máy tính
- Truy cập: `http://localhost:8080/api/nguoidung`
- Nếu thấy dữ liệu JSON → Backend OK

---

## 📲 Bước 5: Test Kết Nối Từ Điện Thoại

### Cách 1: Dùng trình duyệt điện thoại
1. Mở Chrome trên điện thoại
2. Truy cập: `http://192.168.1.100:8080/api/nguoidung`
   *(Thay IP của bạn)*
3. Nếu thấy JSON → Kết nối OK

### Cách 2: Dùng Postman Mobile
1. Cài Postman trên điện thoại
2. GET: `http://192.168.1.100:8080/api/nguoidung`
3. Nếu thấy response → OK

---

## 🔨 Bước 6: Build và Chạy App

1. **Sync Gradle** trong Android Studio
2. **Build** → **Rebuild Project**
3. Kết nối điện thoại qua USB
4. Chọn điện thoại trong device list
5. Click **Run** (hoặc Shift + F10)

---

## 🐛 Xử Lý Lỗi

### Lỗi 1: "Unable to resolve host"
**Nguyên nhân:** IP sai hoặc không cùng mạng

**Giải pháp:**
- Kiểm tra lại IP bằng `ipconfig`
- Đảm bảo điện thoại và máy tính cùng WiFi
- Thử ping từ điện thoại đến máy tính

### Lỗi 2: "Connection refused"
**Nguyên nhân:** Backend chưa chạy hoặc Firewall chặn

**Giải pháp:**
- Kiểm tra Backend đã chạy: `http://localhost:8080`
- Tắt Firewall tạm thời để test
- Kiểm tra port 8080 có bị chiếm không

### Lỗi 3: "Timeout"
**Nguyên nhân:** Mạng chậm hoặc Backend quá tải

**Giải pháp:**
- Kiểm tra tốc độ WiFi
- Restart Backend
- Tăng timeout trong Retrofit (nếu cần)

### Lỗi 4: "Cleartext HTTP traffic not permitted"
**Nguyên nhân:** Android 9+ không cho phép HTTP

**Giải pháp:** Đã fix trong AndroidManifest.xml:
```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

---

## 📝 Checklist Trước Khi Chạy

- [ ] Lấy IP máy tính bằng `ipconfig`
- [ ] Cập nhật IP trong `RetrofitClient.java`
- [ ] Backend đang chạy (`./gradlew bootRun`)
- [ ] Firewall đã cho phép port 8080
- [ ] Điện thoại và máy tính cùng WiFi
- [ ] Test kết nối bằng trình duyệt điện thoại
- [ ] Sync Gradle và Rebuild Project
- [ ] USB Debugging đã bật

---

## 🎯 Ví Dụ Cụ Thể

### Tình huống: IP máy tính là 192.168.1.105

**1. RetrofitClient.java:**
```java
private static final String BASE_URL = "http://192.168.1.105:8080/";
```

**2. Test trên trình duyệt điện thoại:**
```
http://192.168.1.105:8080/api/nguoidung
```

**3. Đăng nhập trong app:**
- Email: `admin@localcooking.vn`
- Password: `admin123`
- API sẽ gọi: `http://192.168.1.105:8080/api/nguoidung/login`

---

## 💡 Tips

### Tip 1: IP động
Nếu IP máy tính thay đổi thường xuyên, có thể:
- Set IP tĩnh cho máy tính trong router
- Hoặc dùng hostname thay vì IP (nếu router hỗ trợ)

### Tip 2: Nhiều môi trường
Tạo nhiều build variants:
```gradle
buildTypes {
    debug {
        buildConfigField "String", "BASE_URL", "\"http://10.0.2.2:8080/\""
    }
    release {
        buildConfigField "String", "BASE_URL", "\"https://api.production.com/\""
    }
}
```

### Tip 3: Dùng ngrok (nếu không cùng mạng)
```bash
ngrok http 8080
```
Sẽ tạo URL public: `https://abc123.ngrok.io`

---

## 🎉 Kết Quả

Sau khi làm đúng các bước:
- ✅ App trên điện thoại kết nối được Backend
- ✅ Đăng nhập thành công
- ✅ Hiển thị "Chào admin"
- ✅ Đăng xuất hoạt động bình thường

---

## 📞 Hỗ Trợ

Nếu vẫn gặp lỗi, kiểm tra:
1. Backend log có lỗi gì không
2. Logcat Android có lỗi gì không
3. Wireshark để xem traffic (nâng cao)
