# 🚀 QUICK START - TEST API NGAY

## Bước 1: Chạy Server
```bash
cd BE
./gradlew bootRun
```

Hoặc Windows:
```bash
gradlew.bat bootRun
```

## Bước 2: Test Health Check
Mở browser hoặc dùng curl:
```
http://localhost:8080/api/health
```

Kết quả mong đợi:
```json
{
  "status": "OK",
  "message": "Server đang chạy tốt!",
  "timestamp": "2025-01-20T18:00:00",
  "database": "SQLite - datlichmonan_app.db"
}
```

## Bước 3: Test Lấy Danh Sách Lớp Học
```
http://localhost:8080/api/lophoc
```

## Bước 4: Test Lấy Lớp Học Kèm Rating
```
http://localhost:8080/api/lophoc/with-rating
```

---

## ⚡ TEST NHANH BẰNG CURL

### 1. Health Check
```bash
curl http://localhost:8080/api/health
```

### 2. Get All Classes
```bash
curl http://localhost:8080/api/lophoc
```

### 3. Get Class with Rating
```bash
curl http://localhost:8080/api/lophoc/with-rating
```

### 4. Get Class by ID
```bash
curl http://localhost:8080/api/lophoc/1
```

### 5. Create New Class
```bash
curl -X POST http://localhost:8080/api/lophoc \
  -H "Content-Type: application/json" \
  -d '{
    "tenLopHoc": "Test Class",
    "moTa": "Test Description",
    "giaTien": 500000,
    "soLuongToiDa": 20,
    "thoiGian": "18:00 - 20:00",
    "diaDiem": "Test Location",
    "trangThai": "Sắp diễn ra",
    "gioBatDau": "18:00",
    "gioKetThuc": "20:00"
  }'
```

---

## 🐛 Nếu Gặp Lỗi

### Lỗi: Port 8080 đã được sử dụng
Sửa trong `application.properties`:
```properties
server.port=8081
```

### Lỗi: Database
Xóa file `datlichmonan_app.db` và chạy lại server.

### Lỗi: Dependencies
```bash
./gradlew clean build
```

---

## ✅ DONE!
Server đã sẵn sàng tại: **http://localhost:8080**

Xem thêm chi tiết tại: `BE/API_TEST_GUIDE.md`
