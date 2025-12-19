# 🔄 Hướng Dẫn Cập Nhật Schema Mới

## 📋 Tổng Quan

Bạn đã tách bảng `LopHoc` thành 2 bảng riêng biệt:
- **KhoaHoc**: Chứa nội dung khóa học (món ăn, giá tiền, mô tả)
- **LichTrinhLopHoc**: Chứa lịch trình (thời gian, địa điểm, giáo viên)

Đây là thiết kế tốt hơn vì:
✅ Tách biệt nội dung và lịch trình
✅ Một khóa học có thể có nhiều lịch trình
✅ Dễ quản lý và mở rộng

---

## 🚀 Bước 1: Chạy Lại Database

### 1.1. Xóa Database Cũ (Nếu Có)
Mở SQL Server Management Studio và chạy:
```sql
USE master;
GO
ALTER DATABASE DatLichHocNauAn SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
DROP DATABASE DatLichHocNauAn;
GO
```

### 1.2. Chạy File SQL Mới
```sql
-- Chạy toàn bộ file DatLichHocNauAnDiaPhuong.sql
```

### 1.3. Kiểm Tra Dữ Liệu
```sql
-- Kiểm tra khóa học
SELECT * FROM KhoaHoc;

-- Kiểm tra lịch trình
SELECT * FROM LichTrinhLopHoc;

-- Kiểm tra món ăn (đã đổi từ maLopHoc → maKhoaHoc)
SELECT * FROM MonAn;

-- Test stored procedure
EXEC sp_LayDanhSachLopTheoNgay '2025-12-25';
```

---

## 🔧 Bước 2: Cập Nhật Backend

### 2.1. Các File Đã Được Tạo/Cập Nhật

#### Models:
- ✅ `BE/src/main/java/com/android/be/model/KhoaHoc.java`
- ✅ `BE/src/main/java/com/android/be/model/LichTrinhLopHoc.java`
- ✅ `BE/src/main/java/com/android/be/model/MonAn.java` (đã cập nhật)

#### Repositories:
- ✅ `BE/src/main/java/com/android/be/repository/KhoaHocRepository.java`
- ✅ `BE/src/main/java/com/android/be/repository/LichTrinhLopHocRepository.java`
- ✅ `BE/src/main/java/com/android/be/repository/MonAnRepository.java` (đã cập nhật)

#### DTOs:
- ✅ `BE/src/main/java/com/android/be/dto/KhoaHocDTO.java`
- ✅ `BE/src/main/java/com/android/be/dto/LichTrinhLopHocDTO.java`

#### Services:
- ✅ `BE/src/main/java/com/android/be/service/KhoaHocService.java`
- ✅ `BE/src/main/java/com/android/be/service/LopHocServiceNew.java`

#### Controllers:
- ✅ `BE/src/main/java/com/android/be/controller/KhoaHocController.java`
- ✅ `BE/src/main/java/com/android/be/controller/LopHocController.java` (đã cập nhật)

### 2.2. Rebuild Backend
```bash
cd BE
./gradlew clean build
./gradlew bootRun
```

### 2.3. Kiểm Tra Backend Đã Chạy
Mở browser:
```
http://localhost:8080/api/lophoc
```

Nếu thấy JSON → Backend OK ✅

---

## 📱 Bước 3: Android App (KHÔNG CẦN THAY ĐỔI)

### 3.1. Tại Sao Không Cần Thay Đổi?

Backend đã được thiết kế để **tương thích ngược 100%**:
- API endpoint `/api/lophoc` vẫn giữ nguyên
- Response format vẫn giống như cũ (LopHocDTO)
- Backend tự động convert: `KhoaHoc + LichTrinhLopHoc → LopHocDTO`

### 3.2. Test Android App

1. **Chạy Backend**: `./gradlew bootRun`
2. **Chạy Android App**: Click Run trong Android Studio
3. **Kiểm tra**:
   - Màn hình Home hiển thị lớp học ✅
   - Tìm kiếm theo địa điểm hoạt động ✅
   - Xem chi tiết lớp học hoạt động ✅

---

## 🧪 Bước 4: Test API

### 4.1. Test API Cũ (Tương Thích)

```bash
# Lấy tất cả lớp học
GET http://localhost:8080/api/lophoc

# Tìm lớp ở Hà Nội
GET http://localhost:8080/api/lophoc/search?diaDiem=Hà Nội

# Tìm lớp ở Hà Nội vào ngày 25/12/2025
GET http://localhost:8080/api/lophoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

### 4.2. Test API Mới (Khóa Học)

```bash
# Lấy tất cả khóa học (với lịch trình)
GET http://localhost:8080/api/khoahoc

# Lấy khóa học theo ID
GET http://localhost:8080/api/khoahoc/1
```

### 4.3. Kết Quả Mong Đợi

**API Cũ (`/api/lophoc`):**
```json
[
  {
    "maLopHoc": 1,
    "tenLop": "Ẩm thực phố cổ Hà Nội",
    "gia": "650,000đ",
    "thoiGian": "17:30 - 20:30",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "tenGiaoVien": "Nguyễn Văn An",
    ...
  }
]
```

**API Mới (`/api/khoahoc`):**
```json
[
  {
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "giaTien": 650000,
    "lichTrinhList": [
      {
        "maLichTrinh": 1,
        "gioBatDau": "17:30:00",
        "gioKetThuc": "20:30:00",
        "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
        ...
      }
    ]
  }
]
```

---

## 🔍 Bước 5: Kiểm Tra Dữ Liệu

### 5.1. Kiểm Tra Quan Hệ Giữa Các Bảng

```sql
-- Xem khóa học và lịch trình của nó
SELECT 
    k.maKhoaHoc,
    k.tenKhoaHoc,
    k.giaTien,
    lt.maLichTrinh,
    lt.gioBatDau,
    lt.gioKetThuc,
    lt.diaDiem,
    lt.thuTrongTuan
FROM KhoaHoc k
LEFT JOIN LichTrinhLopHoc lt ON k.maKhoaHoc = lt.maKhoaHoc
ORDER BY k.maKhoaHoc;
```

### 5.2. Kiểm Tra Món Ăn

```sql
-- Xem món ăn thuộc khóa học nào
SELECT 
    k.tenKhoaHoc,
    dm.tenDanhMuc,
    m.tenMon
FROM MonAn m
JOIN KhoaHoc k ON m.maKhoaHoc = k.maKhoaHoc
JOIN DanhMucMonAn dm ON m.maDanhMuc = dm.maDanhMuc
ORDER BY k.maKhoaHoc, dm.maDanhMuc;
```

### 5.3. Test Stored Procedure

```sql
-- Lấy lớp học vào Chủ Nhật (21/12/2025)
EXEC sp_LayDanhSachLopTheoNgay '2025-12-21';

-- Lấy lớp học vào Thứ 2 (23/12/2025)
EXEC sp_LayDanhSachLopTheoNgay '2025-12-23';
```

---

## 🎯 Bước 6: Các Tính Năng Mới Có Thể Làm

Với cấu trúc mới, bạn có thể dễ dàng:

### 6.1. Thêm Nhiều Lịch Trình Cho Một Khóa Học
```sql
-- Ví dụ: Khóa học "Ẩm thực phố cổ Hà Nội" có thêm lịch buổi sáng
INSERT INTO LichTrinhLopHoc (maKhoaHoc, maGiaoVien, thuTrongTuan, gioBatDau, gioKetThuc, diaDiem, soLuongToiDa)
VALUES (1, 1, '2,4,6', '08:00', '11:00', '45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 15);
```

### 6.2. Tìm Tất Cả Lịch Trình Của Một Khóa Học
```java
// Trong Android app (tương lai)
List<LichTrinhLopHoc> lichTrinhs = lichTrinhRepository.findByMaKhoaHoc(1);
```

### 6.3. Tìm Lớp Học Theo Giáo Viên
```sql
SELECT 
    k.tenKhoaHoc,
    nd.hoTen AS tenGiaoVien,
    lt.gioBatDau,
    lt.gioKetThuc,
    lt.diaDiem
FROM LichTrinhLopHoc lt
JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
JOIN GiaoVien gv ON lt.maGiaoVien = gv.maGiaoVien
JOIN NguoiDung nd ON gv.maNguoiDung = nd.maNguoiDung
WHERE nd.hoTen LIKE N'%Nguyễn Văn An%';
```

---

## ⚠️ Lưu Ý Quan Trọng

### 1. Không Xóa File Cũ
- Giữ lại `LopHocService.java` cũ (đổi tên thành `LopHocServiceOld.java`)
- Để backup và tham khảo

### 2. Kiểm Tra Kỹ Trước Khi Deploy
```bash
# Test tất cả API endpoints
# Test Android app trên emulator
# Test trên điện thoại thật
```

### 3. Backup Database
```sql
-- Backup trước khi thay đổi
BACKUP DATABASE DatLichHocNauAn 
TO DISK = 'C:\Backup\DatLichHocNauAn.bak';
```

---

## 🎉 Kết Luận

✅ **Database**: Đã tách thành KhoaHoc + LichTrinhLopHoc
✅ **Backend**: Đã cập nhật models, repositories, services, controllers
✅ **API**: Tương thích ngược 100% với Android app
✅ **Stored Procedure**: Hoạt động tốt với cấu trúc mới
✅ **Android App**: KHÔNG CẦN THAY ĐỔI GÌ

---

## 📞 Hỗ Trợ

Nếu gặp lỗi:
1. Kiểm tra Backend log: Console của `gradlew bootRun`
2. Kiểm tra Database: Chạy các query test ở trên
3. Kiểm tra Android Logcat: Xem có lỗi API không
4. Xem file `BE/API_ENDPOINTS_NEW.md` để biết chi tiết API

**Chúc bạn thành công! 🚀**
