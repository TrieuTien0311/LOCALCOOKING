# 📚 Hướng Dẫn Sử Dụng Stored Procedures

## 🎯 Tổng Quan

Database có 2 stored procedures chính để tính số chỗ trống:

1. **sp_LayDanhSachLopTheoNgay** - Lấy tất cả lớp học theo ngày
2. **sp_KiemTraChoTrong** - Kiểm tra số chỗ trống cho một lịch trình cụ thể

---

## 📅 1. sp_LayDanhSachLopTheoNgay

### Mô tả
Lấy danh sách tất cả lớp học diễn ra vào một ngày cụ thể, tự động tính số chỗ còn trống.

### Tham số
- `@NgayCanXem` (DATE): Ngày cần xem lớp học (VD: '2025-12-25')

### Cách gọi

```sql
-- Xem lớp học vào ngày 25/12/2025
EXEC sp_LayDanhSachLopTheoNgay '2025-12-25';

-- Xem lớp học vào ngày 27/02/2025
EXEC sp_LayDanhSachLopTheoNgay '2025-02-27';

-- Xem lớp học hôm nay
EXEC sp_LayDanhSachLopTheoNgay @NgayCanXem = CAST(GETDATE() AS DATE);
```

### Kết quả trả về

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| maKhoaHoc | INT | Mã khóa học |
| tenKhoaHoc | NVARCHAR | Tên khóa học |
| hinhAnh | VARCHAR | Đường dẫn hình ảnh |
| giaTien | DECIMAL | Giá tiền |
| saoTrungBinh | FLOAT | Đánh giá trung bình |
| soLuongDanhGia | INT | Số lượng đánh giá |
| maLichTrinh | INT | Mã lịch trình |
| gioBatDau | TIME | Giờ bắt đầu |
| gioKetThuc | TIME | Giờ kết thúc |
| diaDiem | NVARCHAR | Địa điểm học |
| TongCho | INT | Tổng số chỗ |
| DaDat | INT | Số người đã đặt |
| **ConTrong** | INT | **Số chỗ còn trống** |
| TrangThaiHienThi | NVARCHAR | "Còn Nhận" hoặc "Hết Chỗ" |

### Ví dụ kết quả

```
maKhoaHoc: 1
tenKhoaHoc: Ẩm thực phố cổ Hà Nội
giaTien: 650000
maLichTrinh: 1
gioBatDau: 17:30:00
gioKetThuc: 20:30:00
diaDiem: 45 Hàng Bạc, Hoàn Kiếm, Hà Nội
TongCho: 20
DaDat: 3
ConTrong: 17  ← Còn 17 chỗ trống
TrangThaiHienThi: Còn Nhận
```

### Logic hoạt động

1. **Xác định thứ** của ngày cần xem (Chủ Nhật = 1, Thứ 2 = 2, ...)
2. **Tìm các lớp** có lịch học vào thứ đó (kiểm tra cột `thuTrongTuan`)
3. **Đếm số người đã đặt** trong ngày đó (bỏ qua đơn đã hủy)
4. **Tính số chỗ trống** = `soLuongToiDa - DaDat`
5. **Trả về trạng thái** "Còn Nhận" hoặc "Hết Chỗ"

---

## 🔍 2. sp_KiemTraChoTrong

### Mô tả
Kiểm tra số chỗ trống cho một lịch trình cụ thể vào một ngày cụ thể.

### Tham số
- `@MaLichTrinh` (INT): Mã lịch trình cần kiểm tra
- `@NgayThamGia` (DATE): Ngày tham gia (VD: '2025-02-27')

### Cách gọi

```sql
-- Kiểm tra lịch trình số 1 vào ngày 27/02/2025
EXEC sp_KiemTraChoTrong @MaLichTrinh = 1, @NgayThamGia = '2025-02-27';

-- Kiểm tra lịch trình số 5 vào ngày 25/12/2025
EXEC sp_KiemTraChoTrong 5, '2025-12-25';
```

### Kết quả trả về

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| maLichTrinh | INT | Mã lịch trình |
| maKhoaHoc | INT | Mã khóa học |
| tenKhoaHoc | NVARCHAR | Tên khóa học |
| TongCho | INT | Tổng số chỗ |
| DaDat | INT | Số người đã đặt |
| **ConTrong** | INT | **Số chỗ còn trống** |
| TrangThai | NVARCHAR | "Còn Nhiều", "Sắp Hết", hoặc "Hết Chỗ" |

### Ví dụ kết quả

```
maLichTrinh: 1
maKhoaHoc: 1
tenKhoaHoc: Ẩm thực phố cổ Hà Nội
TongCho: 20
DaDat: 3
ConTrong: 17  ← Còn 17 chỗ trống
TrangThai: Còn Nhiều
```

### Logic trạng thái

- **"Hết Chỗ"**: ConTrong = 0
- **"Sắp Hết"**: ConTrong <= 5
- **"Còn Nhiều"**: ConTrong > 5

---

## 🔗 3. Tích Hợp Với Backend

### 3.1. Trong Repository

File: `BE/src/main/java/com/android/be/repository/LichTrinhLopHocRepository.java`

```java
@Query(value = "EXEC sp_LayDanhSachLopTheoNgay :ngayCanXem", nativeQuery = true)
List<Object[]> findClassesByDate(@Param("ngayCanXem") String ngayCanXem);

@Query(value = "EXEC sp_KiemTraChoTrong :maLichTrinh, :ngayThamGia", nativeQuery = true)
Object[] checkAvailableSeats(@Param("maLichTrinh") Integer maLichTrinh, 
                              @Param("ngayThamGia") String ngayThamGia);
```

### 3.2. Trong Service

File: `BE/src/main/java/com/android/be/service/LopHocServiceNew.java`

```java
public List<LopHocDTO> searchLopHocByDiaDiemAndDate(String diaDiem, LocalDate ngayTimKiem) {
    String ngayStr = ngayTimKiem.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    List<Object[]> results = lichTrinhRepository.findClassesByDate(ngayStr);
    
    return results.stream()
            .filter(row -> {
                String diaDiemLop = (String) row[9];
                return diaDiemLop != null && diaDiemLop.toLowerCase().contains(diaDiem.toLowerCase());
            })
            .map(this::convertStoredProcResultToDTO)
            .collect(Collectors.toList());
}
```

### 3.3. API Endpoint

```
GET /api/lophoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-02-27
```

---

## 🧪 4. Test Cases

### Test 1: Xem lớp học vào Chủ Nhật

```sql
-- Ngày 21/12/2025 là Chủ Nhật
EXEC sp_LayDanhSachLopTheoNgay '2025-12-21';
```

**Kết quả mong đợi:** Hiển thị các lớp có `thuTrongTuan` chứa 'CN' hoặc '1'

### Test 2: Xem lớp học vào Thứ 2

```sql
-- Ngày 22/12/2025 là Thứ 2
EXEC sp_LayDanhSachLopTheoNgay '2025-12-22';
```

**Kết quả mong đợi:** Hiển thị các lớp có `thuTrongTuan` chứa '2'

### Test 3: Kiểm tra lớp đã đầy

```sql
-- Giả sử lớp 1 có 20 chỗ và đã có 20 người đặt
EXEC sp_KiemTraChoTrong 1, '2025-12-22';
```

**Kết quả mong đợi:**
```
ConTrong: 0
TrangThai: Hết Chỗ
```

### Test 4: Kiểm tra lớp sắp hết chỗ

```sql
-- Giả sử lớp 5 có 20 chỗ và đã có 17 người đặt
EXEC sp_KiemTraChoTrong 5, '2025-12-24';
```

**Kết quả mong đợi:**
```
ConTrong: 3
TrangThai: Sắp Hết
```

---

## 📊 5. Ví Dụ Thực Tế

### Kịch bản: Học viên muốn đặt lớp vào ngày 27/02/2025

**Bước 1:** Xem tất cả lớp học vào ngày đó

```sql
EXEC sp_LayDanhSachLopTheoNgay '2025-02-27';
```

**Bước 2:** Chọn lớp và kiểm tra số chỗ trống

```sql
-- Giả sử chọn lịch trình số 1
EXEC sp_KiemTraChoTrong 1, '2025-02-27';
```

**Bước 3:** Nếu còn chỗ, thực hiện đặt lịch

```sql
INSERT INTO DatLich (maHocVien, maLichTrinh, ngayThamGia, soLuongNguoi, tongTien, tenNguoiDat, emailNguoiDat, sdtNguoiDat)
VALUES (4, 1, '2025-02-27', 1, 650000, N'Nguyễn Văn A', 'a@gmail.com', '0901234567');
```

**Bước 4:** Kiểm tra lại số chỗ trống

```sql
EXEC sp_KiemTraChoTrong 1, '2025-02-27';
-- ConTrong sẽ giảm đi 1
```

---

## ⚠️ Lưu Ý Quan Trọng

### 1. Đơn đã hủy không được tính

Stored procedure tự động bỏ qua các đơn có `trangThai = 'Đã Hủy'`:

```sql
AND d.trangThai <> N'Đã Hủy'
```

### 2. Chỉ tính đơn đặt đúng ngày

Chỉ đếm các đơn đặt lịch có `ngayThamGia` trùng với ngày cần kiểm tra:

```sql
AND d.ngayThamGia = @NgayCanXem
```

### 3. Chỉ hiển thị lớp đang hoạt động

Chỉ lấy các lịch trình có `trangThai = 1`:

```sql
AND lt.trangThai = 1
```

### 4. Format ngày đúng

Khi gọi từ Java, format ngày theo pattern `yyyy-MM-dd`:

```java
String ngayStr = ngayTimKiem.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
```

---

## 🎉 Kết Luận

✅ Stored procedures đã sẵn sàng sử dụng
✅ Tự động tính số chỗ trống chính xác
✅ Hỗ trợ tìm kiếm theo ngày và lịch trình
✅ Tích hợp hoàn chỉnh với Backend
✅ Android app có thể sử dụng ngay

**Chúc bạn thành công! 🚀**
