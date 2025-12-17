# 🚀 HƯỚNG DẪN TEST API

## 📋 Chuẩn bị

1. **Chạy database SQLite:**
   - File database sẽ tự động tạo: `datlichmonan_app.db`
   - Import dữ liệu mẫu từ: `SQLite/SQLite/insert_data.sql`

2. **Chạy Spring Boot:**
   ```bash
   cd BE
   ./gradlew bootRun
   ```
   Hoặc trên Windows:
   ```bash
   gradlew.bat bootRun
   ```

3. **Server sẽ chạy tại:** `http://localhost:8080`

---

## 🎯 ENDPOINTS ĐỂ TEST

### 1. **Lấy tất cả lớp học (Raw Data)**
```http
GET http://localhost:8080/api/lophoc
```

**Response mẫu:**
```json
[
  {
    "maLopHoc": 1,
    "tenLopHoc": "Ẩm thực địa phương Huế",
    "moTa": "Khám phá hương vị đặc trưng...",
    "maGiaoVien": 1,
    "tenGiaoVien": "Nguyễn Thị Anna",
    "soLuongToiDa": 20,
    "soLuongHienTai": 1,
    "giaTien": 715000,
    "thoiGian": "14:00 - 17:00",
    "diaDiem": "23 Lê Duẩn - Đà Nẵng",
    "trangThai": "Sắp diễn ra",
    "ngayDienRa": "2025-02-10",
    "gioBatDau": "14:00:00",
    "gioKetThuc": "17:00:00",
    "hinhAnh": "hue.jpg",
    "coUuDai": true,
    "ngayTao": "2025-01-20T10:00:00"
  }
]
```

---

### 2. **Lấy tất cả lớp học kèm điểm đánh giá** ⭐
```http
GET http://localhost:8080/api/lophoc/with-rating
```

**Response mẫu:**
```json
[
  {
    "maLopHoc": 1,
    "tenLopHoc": "Ẩm thực địa phương Huế",
    "moTa": "Khám phá hương vị đặc trưng...",
    "tenGiaoVien": "Nguyễn Thị Anna",
    "soLuongToiDa": 20,
    "soLuongHienTai": 1,
    "soLuongConLai": 19,
    "giaTien": 715000,
    "thoiGian": "14:00 - 17:00",
    "diaDiem": "23 Lê Duẩn - Đà Nẵng",
    "trangThai": "Sắp diễn ra",
    "ngayDienRa": "2025-02-10",
    "hinhAnh": "hue.jpg",
    "coUuDai": true,
    "diemDanhGia": 5.0,
    "soDanhGia": 1
  }
]
```

---

### 3. **Lấy chi tiết lớp học theo ID**
```http
GET http://localhost:8080/api/lophoc/1
```

---

### 4. **Lấy chi tiết lớp học kèm rating theo ID** ⭐
```http
GET http://localhost:8080/api/lophoc/1/detail
```

---

### 5. **Tạo lớp học mới**
```http
POST http://localhost:8080/api/lophoc
Content-Type: application/json

{
  "tenLopHoc": "Món Ăn Miền Bắc",
  "moTa": "Học nấu các món ăn đặc trưng miền Bắc",
  "maGiaoVien": 1,
  "tenGiaoVien": "Nguyễn Thị Anna",
  "soLuongToiDa": 15,
  "giaTien": 600000,
  "thoiGian": "18:00 - 20:00",
  "diaDiem": "Hà Nội",
  "trangThai": "Sắp diễn ra",
  "ngayDienRa": "2025-03-01",
  "gioBatDau": "18:00:00",
  "gioKetThuc": "20:00:00",
  "hinhAnh": "hanoi.jpg",
  "coUuDai": false
}
```

---

### 6. **Cập nhật lớp học**
```http
PUT http://localhost:8080/api/lophoc/1
Content-Type: application/json

{
  "tenLopHoc": "Ẩm thực Huế - Cập nhật",
  "moTa": "Mô tả mới...",
  "giaTien": 750000,
  "thoiGian": "14:00 - 18:00",
  "diaDiem": "Đà Nẵng",
  "trangThai": "Đang diễn ra"
}
```

---

### 7. **Xóa lớp học**
```http
DELETE http://localhost:8080/api/lophoc/3
```

---

## 🧪 TEST BẰNG CURL

### Test GET all classes:
```bash
curl http://localhost:8080/api/lophoc
```

### Test GET with rating:
```bash
curl http://localhost:8080/api/lophoc/with-rating
```

### Test GET by ID:
```bash
curl http://localhost:8080/api/lophoc/1
```

### Test POST (Create):
```bash
curl -X POST http://localhost:8080/api/lophoc \
  -H "Content-Type: application/json" \
  -d '{
    "tenLopHoc": "Test Class",
    "moTa": "Test Description",
    "giaTien": 500000,
    "thoiGian": "18:00 - 20:00",
    "diaDiem": "Test Location",
    "trangThai": "Sắp diễn ra"
  }'
```

---

## 🔍 KIỂM TRA DATABASE

Sau khi chạy API, kiểm tra file `datlichmonan_app.db` đã được tạo:

```bash
# Mở SQLite
sqlite3 datlichmonan_app.db

# Xem các bảng
.tables

# Xem dữ liệu lớp học
SELECT * FROM LopHoc;

# Thoát
.quit
```

---

## ⚠️ LƯU Ý

1. **CORS đã được enable** cho tất cả origins (`@CrossOrigin(origins = "*")`)
2. **Hibernate DDL** đang ở chế độ `update` - sẽ tự động tạo/cập nhật bảng
3. **SQL logging** đã được bật để debug
4. Nếu gặp lỗi, check console log để xem SQL queries

---

## 📱 TEST VỚI POSTMAN

1. Import collection từ file này
2. Hoặc tạo request mới với các endpoint trên
3. Set `Content-Type: application/json` cho POST/PUT

---

## ✅ CHECKLIST

- [ ] Server chạy thành công tại port 8080
- [ ] Database file `datlichmonan_app.db` được tạo
- [ ] GET `/api/lophoc` trả về danh sách lớp học
- [ ] GET `/api/lophoc/with-rating` trả về kèm điểm đánh giá
- [ ] POST tạo lớp học mới thành công
- [ ] PUT cập nhật lớp học thành công
- [ ] DELETE xóa lớp học thành công

---

## 🐛 TROUBLESHOOTING

### Lỗi: "Table not found"
- Kiểm tra `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`
- Xóa file `datlichmonan_app.db` và chạy lại

### Lỗi: "Port 8080 already in use"
- Đổi port trong `application.properties`: `server.port=8081`

### Lỗi: "Cannot find SQLite driver"
- Chạy: `./gradlew clean build` để download dependencies

---

## 🎉 DONE!

API đã sẵn sàng để test! Bắt đầu với endpoint đơn giản nhất:
```
http://localhost:8080/api/lophoc
```
