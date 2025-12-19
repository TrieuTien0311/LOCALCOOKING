# TROUBLESHOOTING - BACKEND BỊ DỪNG

## Cách kiểm tra lỗi

### 1. Chạy backend và xem log
```bash
cd BE
.\gradlew.bat bootRun
```

Xem log trong console để biết lỗi cụ thể.

---

## Các lỗi phổ biến

### ❌ Lỗi 1: Port 8080 đã được sử dụng
```
***************************
APPLICATION FAILED TO START
***************************

Description:
Web server failed to start. Port 8080 was already in use.
```

**Giải pháp:**
- Tắt ứng dụng đang chạy trên port 8080
- Hoặc đổi port trong `application.properties`:
```properties
server.port=8081
```

---

### ❌ Lỗi 2: Không kết nối được SQL Server
```
Cannot create PoolableConnectionFactory
(The TCP/IP connection to the host localhost, port 1433 has failed)
```

**Giải pháp:**
1. Kiểm tra SQL Server đã chạy chưa
2. Kiểm tra username/password trong `application.properties`:
```properties
spring.datasource.username=sa
spring.datasource.password=123456
```
3. Kiểm tra database `DatLichHocNauAn` đã tạo chưa

---

### ❌ Lỗi 3: Thiếu cấu hình Email (khi gửi OTP)
```
Mail server connection failed
```

**Giải pháp:**
Thêm vào `application.properties`:
```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Lưu ý:** Với Gmail, cần tạo App Password:
1. Vào Google Account → Security
2. Bật 2-Step Verification
3. Tạo App Password
4. Dùng App Password thay vì mật khẩu thường

---

### ❌ Lỗi 4: Table không tồn tại
```
Invalid object name 'NguoiDung'
```

**Giải pháp:**
1. Chạy file SQL để tạo database:
```bash
# Chạy file DatLichHocNauAnDiaPhuong.sql trong SQL Server Management Studio
```

2. Hoặc để Hibernate tự tạo table (đã cấu hình):
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

### ❌ Lỗi 5: Dependency injection failed
```
Field otpService in com.android.be.service.NguoiDungService required a bean of type 'com.android.be.service.OtpService' that could not be found.
```

**Giải pháp:**
- Kiểm tra `OtpService.java` có annotation `@Service` không
- Clean và rebuild project:
```bash
.\gradlew.bat clean build
```

---

## Cách debug từng bước

### Bước 1: Kiểm tra build
```bash
.\gradlew.bat clean build -x test
```
→ Nếu lỗi: Fix syntax error trong code

### Bước 2: Kiểm tra database
```bash
# Mở SQL Server Management Studio
# Kết nối với localhost
# Kiểm tra database DatLichHocNauAn có tồn tại không
```

### Bước 3: Chạy backend
```bash
.\gradlew.bat bootRun
```
→ Xem log để biết lỗi cụ thể

### Bước 4: Test API
```bash
# Mở Postman
# Test GET http://localhost:8080/api/nguoidung
```
→ Nếu trả về dữ liệu: Backend chạy OK

---

## Checklist trước khi chạy

- [ ] SQL Server đang chạy
- [ ] Database `DatLichHocNauAn` đã tạo
- [ ] Port 8080 chưa được sử dụng
- [ ] File `application.properties` đã cấu hình đúng
- [ ] Đã chạy `.\gradlew.bat clean build` thành công

---

## Nếu vẫn không chạy được

Gửi cho tôi:
1. Log đầy đủ khi chạy `.\gradlew.bat bootRun`
2. Nội dung file `application.properties`
3. Screenshot lỗi

Tôi sẽ giúp bạn fix cụ thể!
