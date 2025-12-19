# Fix Lỗi Kết Nối Mobile Hotspot

## Vấn đề
Lỗi: `Failed to connect to /10.0.1.1 (port 8080) from /192.168.137.33`

## Nguyên nhân
Khi test app trên **điện thoại thật** kết nối vào **Mobile Hotspot từ máy tính**, cần dùng IP gateway của hotspot (thường là `192.168.137.1`), không phải `10.0.2.2` (dành cho emulator).

## Giải pháp đã áp dụng

### 1. Backend - Cho phép nhận request từ mạng ngoài

File: `BE/src/main/resources/application.properties`

```properties
server.port=8080
server.address=0.0.0.0  # ← Đã thêm dòng này
```

### 2. Android - Cập nhật IP trong RetrofitClient

File: `FE/app/src/main/java/com/example/localcooking_v3t/api/RetrofitClient.java`

```java
private static final String IP_MAY_TINH = "192.168.137.1";  // ← Đã đổi từ 10.0.2.2

private static final String BASE_URL = isEmulator()
    ? "http://10.0.2.2:8080/"              // Máy ảo
    : "http://" + IP_MAY_TINH + ":8080/";  // Điện thoại thật
```

## Các bước tiếp theo

### Bước 1: Xác nhận IP của Mobile Hotspot

Mở **Command Prompt** và chạy:
```cmd
ipconfig
```

Tìm phần **"Wireless LAN adapter Local Area Connection* X"**, xem **IPv4 Address**:
- Nếu là `192.168.137.1` → OK, không cần đổi gì
- Nếu khác (ví dụ `192.168.173.1`) → Cập nhật lại `IP_MAY_TINH` trong RetrofitClient

### Bước 2: Restart Backend

```cmd
cd BE
gradlew bootRun
```

Hoặc restart trong IDE

### Bước 3: Rebuild Android App

1. **Clean project:**
   ```
   Build > Clean Project
   ```

2. **Rebuild:**
   ```
   Build > Rebuild Project
   ```

3. **Cài lại app trên điện thoại**

### Bước 4: Kiểm tra Firewall (nếu vẫn lỗi)

**Windows Firewall:**
1. Mở **Windows Defender Firewall**
2. Click **Allow an app or feature through Windows Defender Firewall**
3. Click **Change settings**
4. Tìm **Java(TM) Platform SE binary** hoặc **OpenJDK Platform binary**
5. Tick cả **Private** và **Public**
6. Click **OK**

**Hoặc tạm thời tắt firewall để test:**
```
Control Panel > Windows Defender Firewall > Turn Windows Defender Firewall on or off
```

### Bước 5: Test kết nối

**Từ điện thoại**, mở trình duyệt Chrome và truy cập:
```
http://192.168.137.1:8080/api/nguoidung
```

**Kết quả mong đợi:**
- Thấy JSON response → Kết nối OK ✅
- Timeout hoặc không load → Kiểm tra firewall ❌

## Troubleshooting

### Lỗi: Vẫn không kết nối được

1. **Kiểm tra backend đang chạy:**
   ```cmd
   netstat -ano | findstr :8080
   ```
   Phải thấy dòng có `0.0.0.0:8080` hoặc `[::]:8080`

2. **Ping từ điện thoại:**
   - Cài app **Network Utilities** trên điện thoại
   - Ping `192.168.137.1`
   - Nếu không ping được → Vấn đề network/firewall

3. **Kiểm tra Mobile Hotspot:**
   - Tắt và bật lại Mobile Hotspot
   - Điện thoại ngắt kết nối và kết nối lại

### Lỗi: IP không phải 192.168.137.1

Nếu `ipconfig` hiển thị IP khác, cập nhật trong RetrofitClient:

```java
private static final String IP_MAY_TINH = "192.168.173.1"; // IP thật của bạn
```

### Lỗi: Cleartext HTTP traffic not permitted

Nếu Android báo lỗi này, thêm vào `AndroidManifest.xml`:

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

## IP cho các trường hợp khác

| Trường hợp | IP sử dụng |
|------------|------------|
| Emulator | `10.0.2.2` |
| Điện thoại + Mobile Hotspot (máy tính) | `192.168.137.1` |
| Điện thoại + WiFi chung | IP máy tính trong mạng WiFi |
| Điện thoại + USB Tethering | IP máy tính nhận từ USB |
| Production | Domain thật (https://api.example.com) |

## Kiểm tra cuối cùng

Sau khi làm xong, test API đăng nhập:
1. Mở app trên điện thoại
2. Thử đăng nhập
3. Xem log trong Logcat (Android Studio)
4. Xem log trong terminal backend

Nếu thấy request đến backend → Thành công! 🎉
