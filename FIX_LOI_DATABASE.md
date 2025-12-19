# FIX LỖI DATABASE - "Unable to determine Dialect"

## ❌ LỖI GẶP PHẢI

```
org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata
(please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' 
when a custom Dialect implementation must be provided)
```

## 🔍 NGUYÊN NHÂN

1. **Git merge conflict** trong file `application.properties`
   - Có dấu `<<<<<<< HEAD` và `>>>>>>>` làm hỏng cấu hình
   
2. **Thiếu cấu hình Hibernate Dialect**
   - Spring Boot không biết đang dùng database gì (SQL Server, MySQL, PostgreSQL...)

3. **Thiếu driver class**
   - Không khai báo `spring.datasource.driver-class-name`

## ✅ CÁCH FIX

### Bước 1: Xóa Git conflict markers

Mở file `BE/src/main/resources/application.properties` và xóa các dòng:
```
<<<<<<< HEAD
=======
>>>>>>> branch-name
```

### Bước 2: Cấu hình đầy đủ

File `application.properties` đúng:

```properties
spring.application.name=BE

# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=DatLichHocNauAn;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=Anhthu1907@
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl

# Server Configuration
server.port=8080

# Email SMTP Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=LocalCooking23@gmail.com
spring.mail.password=llyvdbxcmexasdjt
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Bước 3: Kiểm tra SQL Server

**Đảm bảo SQL Server đang chạy:**

1. Mở **SQL Server Configuration Manager**
2. Kiểm tra **SQL Server (MSSQLSERVER)** đang chạy
3. Hoặc mở **Services** (services.msc) và tìm **SQL Server**

**Kiểm tra database tồn tại:**

```sql
-- Mở SQL Server Management Studio (SSMS)
-- Kết nối với localhost
-- Chạy query:

SELECT name FROM sys.databases WHERE name = 'DatLichHocNauAn';
```

Nếu không có, tạo database:
```sql
CREATE DATABASE DatLichHocNauAn;
```

### Bước 4: Kiểm tra username/password

Đảm bảo username `sa` và password `Anhthu1907@` đúng:

```sql
-- Test login trong SSMS với:
Server name: localhost
Authentication: SQL Server Authentication
Login: sa
Password: Anhthu1907@
```

### Bước 5: Rebuild và chạy

```bash
cd BE
.\gradlew.bat clean build -x test
.\gradlew.bat bootRun
```

## 🎯 KIỂM TRA THÀNH CÔNG

Nếu backend chạy thành công, bạn sẽ thấy:

```
Started BeApplication in X.XXX seconds
Tomcat started on port 8080
```

Test API:
```
GET http://localhost:8080/api/nguoidung
```

Nếu trả về danh sách người dùng (hoặc `[]` nếu chưa có data) → **THÀNH CÔNG!**

---

## 🔧 CÁC LỖI KHÁC CÓ THỂ GẶP

### Lỗi: "Login failed for user 'sa'"
→ Sai password, đổi trong `application.properties`

### Lỗi: "Cannot open database 'DatLichHocNauAn'"
→ Database chưa tạo, chạy SQL:
```sql
CREATE DATABASE DatLichHocNauAn;
```

### Lỗi: "The TCP/IP connection to the host localhost, port 1433 has failed"
→ SQL Server chưa chạy hoặc TCP/IP chưa enable:
1. Mở **SQL Server Configuration Manager**
2. **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
3. Enable **TCP/IP**
4. Restart SQL Server

### Lỗi: "Port 8080 was already in use"
→ Đổi port trong `application.properties`:
```properties
server.port=8081
```

---

## 📝 CHECKLIST TRƯỚC KHI CHẠY

- [ ] File `application.properties` không có Git conflict markers
- [ ] SQL Server đang chạy
- [ ] Database `DatLichHocNauAn` đã tạo
- [ ] Username/password đúng
- [ ] TCP/IP đã enable trong SQL Server
- [ ] Port 8080 chưa được sử dụng
- [ ] Đã chạy `.\gradlew.bat clean build` thành công

---

## ✨ KẾT QUẢ

Sau khi fix, backend đã chạy thành công và sẵn sàng nhận request từ Android app!

**Các API có thể test:**
- `POST /api/nguoidung/login` - Đăng nhập
- `POST /api/nguoidung/register` - Đăng ký
- `POST /api/nguoidung/change-password/send-otp` - Gửi OTP đổi mật khẩu
- `POST /api/nguoidung/change-password/verify` - Xác thực OTP và đổi mật khẩu
- `GET /api/nguoidung` - Lấy danh sách người dùng
