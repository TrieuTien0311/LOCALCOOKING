# 📚 API Endpoints - Cấu Trúc Mới (KhoaHoc + LichTrinhLopHoc)

## 🎯 Tổng Quan

Backend đã được cập nhật để khớp với schema mới:
- **KhoaHoc**: Chứa thông tin nội dung khóa học (món ăn, giá, mô tả)
- **LichTrinhLopHoc**: Chứa thông tin lịch trình (thời gian, địa điểm, giáo viên)

## 🔗 Base URL
```
http://localhost:8080/api
```

---

## 📖 1. API Khóa Học (KhoaHoc)

### 1.1. Lấy Tất Cả Khóa Học
```http
GET /api/khoahoc
```

**Response:**
```json
[
  {
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng của ẩm thực phố cổ",
    "gioiThieu": "Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội",
    "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc...",
    "giaTien": 650000,
    "hinhAnh": "phobo.png",
    "soLuongDanhGia": 0,
    "saoTrungBinh": 0.0,
    "coUuDai": true,
    "lichTrinhList": [
      {
        "maLichTrinh": 1,
        "maKhoaHoc": 1,
        "maGiaoVien": 1,
        "thuTrongTuan": "2,3,4,5,6,7,CN",
        "gioBatDau": "17:30:00",
        "gioKetThuc": "20:30:00",
        "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
        "soLuongToiDa": 20,
        "trangThai": true
      }
    ]
  }
]
```

### 1.2. Lấy Khóa Học Theo ID
```http
GET /api/khoahoc/{id}
```

**Ví dụ:**
```
GET /api/khoahoc/1
```

---

## 📅 2. API Lớp Học (Tương Thích Ngược)

### 2.1. Lấy Tất Cả Lớp Học
```http
GET /api/lophoc
```

**Response (Format cũ cho Android):**
```json
[
  {
    "maLopHoc": 1,
    "tenLop": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng của ẩm thực phố cổ",
    "gioiThieu": "Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội",
    "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc...",
    "gia": "650,000đ",
    "hinhAnh": "phobo.png",
    "danhGia": 0.0,
    "soDanhGia": 0,
    "coUuDai": true,
    "thoiGian": "17:30 - 20:30",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "suat": 20,
    "cacNgayTrongTuan": "2,3,4,5,6,7,CN",
    "trangThai": "Đang mở",
    "tenGiaoVien": "Nguyễn Văn An",
    "loaiLich": "HangNgay",
    "ngayBatDau": "2025-01-01",
    "ngayKetThuc": "2025-12-31",
    "isFavorite": false,
    "daDienRa": false
  }
]
```

### 2.2. Tìm Lớp Học Theo Địa Điểm
```http
GET /api/lophoc/search?diaDiem={diaDiem}
```

**Ví dụ:**
```
GET /api/lophoc/search?diaDiem=Hà Nội
```

### 2.3. Tìm Lớp Học Theo Địa Điểm và Ngày
```http
GET /api/lophoc/search?diaDiem={diaDiem}&ngayTimKiem={ngay}
```

**Ví dụ:**
```
GET /api/lophoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

**Response (Sử dụng Stored Procedure):**
```json
[
  {
    "maLopHoc": 1,
    "tenLop": "Ẩm thực phố cổ Hà Nội",
    "hinhAnh": "phobo.png",
    "gia": "650,000đ",
    "danhGia": 0.0,
    "soDanhGia": 0,
    "thoiGian": "17:30:00 - 20:30:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "suat": 20,
    "trangThai": "Còn Nhận",
    "loaiLich": "HangNgay",
    "isFavorite": false,
    "daDienRa": false
  }
]
```

---

## 🔄 3. Mapping Giữa Schema Cũ và Mới

| Schema Cũ (LopHoc) | Schema Mới | Ghi Chú |
|-------------------|-----------|---------|
| maLopHoc | maKhoaHoc | ID khóa học |
| tenLop | tenKhoaHoc | Tên khóa học |
| diaDiem | LichTrinhLopHoc.diaDiem | Địa điểm học |
| thoiGian | gioBatDau + gioKetThuc | Thời gian học |
| gia | giaTien | Giá khóa học |
| tenGiaoVien | GiaoVien.hoTen | Tên giáo viên |
| suat | soLuongToiDa | Số chỗ tối đa |

---

## 📝 4. Stored Procedure

### sp_LayDanhSachLopTheoNgay

**Mô tả:** Lấy danh sách lớp học theo ngày cụ thể, tự động tính số chỗ còn trống.

**Tham số:**
- `@NgayCanXem` (DATE): Ngày cần xem lớp học

**Cách gọi từ Java:**
```java
@Query(value = "EXEC sp_LayDanhSachLopTheoNgay :ngayCanXem", nativeQuery = true)
List<Object[]> findClassesByDate(@Param("ngayCanXem") String ngayCanXem);
```

**Kết quả trả về:**
```
[0] maKhoaHoc
[1] tenKhoaHoc
[2] hinhAnh
[3] giaTien
[4] saoTrungBinh
[5] soLuongDanhGia
[6] maLichTrinh
[7] gioBatDau
[8] gioKetThuc
[9] diaDiem
[10] TongCho
[11] DaDat
[12] ConTrong
[13] TrangThaiHienThi
```

---

## ✅ 5. Tương Thích Với Android

Android app **KHÔNG CẦN THAY ĐỔI** vì:
1. API endpoint `/api/lophoc` vẫn giữ nguyên
2. Response format vẫn giống như cũ (LopHocDTO)
3. Backend tự động convert từ KhoaHoc + LichTrinhLopHoc → LopHocDTO

---

## 🧪 6. Test API

### Test với Postman/Browser:

```bash
# Lấy tất cả lớp học
http://localhost:8080/api/lophoc

# Tìm lớp ở Hà Nội
http://localhost:8080/api/lophoc/search?diaDiem=Hà Nội

# Tìm lớp ở Hà Nội vào ngày 25/12/2025
http://localhost:8080/api/lophoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25

# Lấy khóa học mới (API mới)
http://localhost:8080/api/khoahoc
```

---

## 📦 7. Các File Đã Tạo/Cập Nhật

### Models:
- ✅ `KhoaHoc.java` - Model khóa học
- ✅ `LichTrinhLopHoc.java` - Model lịch trình
- ✅ `MonAn.java` - Cập nhật từ maLopHoc → maKhoaHoc

### Repositories:
- ✅ `KhoaHocRepository.java`
- ✅ `LichTrinhLopHocRepository.java`
- ✅ `MonAnRepository.java` - Cập nhật methods

### DTOs:
- ✅ `KhoaHocDTO.java`
- ✅ `LichTrinhLopHocDTO.java`
- ✅ `LopHocDTO.java` - Giữ nguyên cho tương thích

### Services:
- ✅ `KhoaHocService.java` - Service mới
- ✅ `LopHocServiceNew.java` - Service tương thích ngược

### Controllers:
- ✅ `KhoaHocController.java` - Controller mới
- ✅ `LopHocController.java` - Cập nhật sử dụng service mới

---

## 🎉 Kết Luận

✅ Backend đã được cập nhật hoàn toàn để khớp với schema mới
✅ Android app không cần thay đổi gì (tương thích ngược 100%)
✅ Có thể sử dụng API mới `/api/khoahoc` cho các tính năng mới
✅ Stored procedure hoạt động tốt với cấu trúc mới
