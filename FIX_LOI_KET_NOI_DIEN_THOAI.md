# FIX LỖI KẾT NỐI - ĐIỆN THOẠI THẬT

## ❌ LỖI GẶP PHẢI

```
Lỗi: failed to connect to /192.168.137.1 (port 8080) from /10.0.2.16 (port 4213...)
```

## 🔍 NGUYÊN NHÂN

**Vấn đề:** Code auto-detect nhầm điện thoại thật là Emulator

**Kết quả:**
- App nghĩ đang chạy trên Emulator → dùng IP `10.0.2.2`
- Nhưng thực tế đang chạy trên điện thoại thật → cần IP `192.168.137.1`
- Điện thoại không thể kết nối đến `10.0.2.2` (IP này chỉ có trong Emulator)

## ✅ ĐÃ FIX

**Thay đổi trong `RetrofitClient.java`:**

### Trước (Sai):
```java
private static final String BASE_URL = isEmulator() 
        ? "http://10.0.2.2:8080/api/"           // Máy ảo
        : "http://" + IP_MAY_TINH + ":8080/api/"; // Điện thoại thật
```
→ Hàm `isEmulator()` detect sai → chọn sai IP

### Sau (Đúng):
```java
private static final String BASE_URL = "http://192.168.137.1:8080/api/";
```
→ Dùng IP cố định cho điện thoại thật

---

## 📱 HƯỚNG DẪN SỬ DỤNG

### Khi dùng ĐIỆN THOẠI THẬT:

1. **Bật Hotspot trên máy tính** hoặc **kết nối cùng WiFi**
2. **Lấy IP máy tính:**
   ```bash
   ipconfig
   # Tìm: Wireless LAN adapter Local Area Connection* 10
   # IPv4 Address: 192.168.137.1
   ```
3. **Sửa trong `RetrofitClient.java`:**
   ```java
   private static final String BASE_URL = "http://192.168.137.1:8080/api/";
   ```

### Khi dùng EMULATOR:

Sửa thành:
```java
private static final String BASE_URL = "http://10.0.2.2:8080/api/";
```

---

## 🔧 CHECKLIST TRƯỚC KHI CHẠY

### Backend:
- [ ] Backend đang chạy (`.\gradlew.bat bootRun`)
- [ ] Thấy log: `Started BeApplication in X.XXX seconds`
- [ ] Test API: `http://localhost:8080/api/nguoidung` trả về data

### Network:
- [ ] Điện thoại và máy tính cùng mạng (Hotspot hoặc WiFi)
- [ ] Firewall không chặn port 8080
- [ ] Ping được IP máy tính từ điện thoại

### Android App:
- [ ] IP trong `RetrofitClient` đúng với IP máy tính
- [ ] Có quyền Internet trong `AndroidManifest.xml`
- [ ] Đã rebuild app sau khi sửa code

---

## 🧪 CÁCH TEST

### Bước 1: Test Backend
```bash
# Trên máy tính
curl http://localhost:8080/api/nguoidung
```
→ Phải trả về danh sách người dùng

### Bước 2: Test từ điện thoại
Mở trình duyệt trên điện thoại, vào:
```
http://192.168.137.1:8080/api/nguoidung
```
→ Nếu thấy JSON data → Kết nối OK!

### Bước 3: Test app
1. Mở app
2. Thử đăng ký
3. Nếu thành công → DONE! 🎉

---

## ⚠️ LƯU Ý

### IP có thể thay đổi khi:
- Restart máy tính
- Tắt/bật Hotspot
- Đổi mạng WiFi

→ **Cần kiểm tra lại IP bằng `ipconfig`**

### Nếu vẫn lỗi:
1. **Tắt Firewall tạm thời** để test
2. **Kiểm tra Backend log** xem có nhận request không
3. **Dùng IP tĩnh** thay vì DHCP

---

## 🎯 KẾT QUẢ

Sau khi fix:
- ✅ Điện thoại kết nối được backend
- ✅ Đăng ký thành công
- ✅ Đăng nhập thành công
- ✅ Đổi mật khẩu với OTP hoạt động

---

## 📚 TÀI LIỆU THAM KHẢO

**Các IP đặc biệt trong Android:**
- `10.0.2.2` = localhost của máy tính (chỉ trong Emulator)
- `10.0.2.15` = IP của Emulator
- `192.168.x.x` = IP thực trong mạng LAN
- `127.0.0.1` = localhost của chính điện thoại (không phải máy tính!)

**Lệnh hữu ích:**
```bash
# Xem IP máy tính
ipconfig

# Xem port đang mở
netstat -an | findstr 8080

# Test API từ máy tính
curl http://localhost:8080/api/nguoidung
```
