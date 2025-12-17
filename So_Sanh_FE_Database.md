# SO SÁNH DỮ LIỆU GIỮA FE VÀ DATABASE

## 📋 TỔNG QUAN

Sau khi phân tích các Activity và Model trong dự án FE (Android), đây là so sánh với database schema để xác định những gì còn thiếu.

---

## 1️⃣ NGƯỜI DÙNG (NguoiDung)

### ✅ Có trong Database:
- maNguoiDung (INT PRIMARY KEY)
- tenDangNhap (VARCHAR)
- matKhau (VARCHAR)
- hoTen (NVARCHAR)
- email (VARCHAR)
- soDienThoai (VARCHAR)
- diaChi (NVARCHAR)
- trangThai (NVARCHAR) - HoatDong | BiKhoa
- ngayTao (DATETIME)
- lanCapNhatCuoi (DATETIME)

### ❌ Thiếu trong FE:
FE chỉ có form đăng ký với:
- Tài khoản (tenDangNhap)
- Email
- Mật khẩu

**THIẾU:**
- ❌ Họ tên (hoTen) - không có trong form Register
- ❌ Số điện thoại (soDienThoai) - không có trong form Register
- ❌ Địa chỉ (diaChi) - không có trong form Register
- ❌ Vai trò người dùng (Admin/GiaoVien/HocVien) - Database không có trường này!

---

## 2️⃣ LỚP HỌC (LopHoc)

### ✅ Có trong Database:
- maLopHoc (INT PRIMARY KEY)
- tenLopHoc (NVARCHAR)
- moTa (NVARCHAR)
- maGiaoVien (INT FK)
- soLuongToiDa (INT)
- soLuongHienTai (INT)
- giaTien (DECIMAL)
- thoiGian (NVARCHAR)
- diaDiem (NVARCHAR)
- trangThai (NVARCHAR) - "Sắp diễn ra"
- ngayDienRa (DATE)
- gioBatDau (TIME)
- gioKetThuc (TIME)
- hinhAnh (VARCHAR)
- ngayTao (DATETIME)

### ✅ Có trong FE (Class.java):
- tenLop
- moTa
- thoiGian
- ngay
- diaDiem
- gia
- danhGia (float)
- soDanhGia (int)
- hinhAnh (int - resource ID)
- coUuDai (boolean)
- thoiGianKetThuc
- suat (số lượng còn lại)
- isFavorite (boolean)
- daDienRa (boolean)
- lichTrinhLopHoc (List<Category>)

**THIẾU TRONG DATABASE:**
- ❌ danhGia (điểm đánh giá trung bình) - phải tính từ bảng DanhGia
- ❌ soDanhGia (số lượt đánh giá) - phải đếm từ bảng DanhGia
- ❌ coUuDai (boolean) - không có trong DB
- ❌ lichTrinhLopHoc (danh sách món ăn theo category) - không có trong DB

**THIẾU TRONG FE:**
- ❌ maLopHoc (ID) - FE không lưu ID
- ❌ maGiaoVien (ID giáo viên) - FE không có
- ❌ soLuongToiDa - FE chỉ có "suat" (còn lại)
- ❌ soLuongHienTai - FE không có

---

## 3️⃣ LỊCH TRÌNH LỚP HỌC (Category & Food)

### ❌ HOÀN TOÀN THIẾU TRONG DATABASE!

FE có:
- **Category.java**: Danh mục món ăn (Món khai vị, Món chính, Món tráng miệng)
  - tenDanhMuc
  - thoiGian
  - iconDanhMuc
  - danhSachMon (List<Food>)

- **Food.java**: Chi tiết món ăn
  - tenMon
  - gioiThieu
  - nguyenLieu
  - hinhAnh

**CẦN THÊM VÀO DATABASE:**
```sql
-- Bảng danh mục món ăn trong lớp học
CREATE TABLE DanhMucMonAn (
    maDanhMuc INT PRIMARY KEY,
    maLopHoc INT NOT NULL,
    tenDanhMuc NVARCHAR(100) NOT NULL, -- Món khai vị, Món chính, Món tráng miệng
    thoiGian NVARCHAR(50), -- 14:00 - 15:00
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- Bảng món ăn
CREATE TABLE MonAn (
    maMonAn INT PRIMARY KEY,
    maDanhMuc INT NOT NULL,
    tenMon NVARCHAR(200) NOT NULL,
    gioiThieu NVARCHAR(MAX),
    nguyenLieu NVARCHAR(MAX),
    hinhAnh VARCHAR(255),
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucMonAn(maDanhMuc)
);
```

---

## 4️⃣ ĐẶT LỊCH (DatLich)

### ✅ Có trong Database:
- maDatLich (INT PRIMARY KEY)
- maHocVien (INT FK)
- maLopHoc (INT FK)
- ngayDat (DATETIME)
- trangThai (NVARCHAR) - "Chờ Duyệt"
- ghiChu (NVARCHAR)

### ✅ Có trong FE (Booking.java):
- Thông tin lớp học (lopHoc)
- Số lượng đặt (soLuongDat)
- Tổng tiền (tongTien)

**THIẾU TRONG DATABASE:**
- ❌ soLuongNguoi (INT) - Số người đặt trong 1 booking
- ❌ tongTien (DECIMAL) - Tổng tiền của booking

**CẦN THÊM:**
```sql
ALTER TABLE DatLich
ADD soLuongNguoi INT DEFAULT 1,
    tongTien DECIMAL(10,2);
```

---

## 5️⃣ THANH TOÁN (ThanhToan & Payment)

### ✅ Có trong Database:
- maThanhToan (INT PRIMARY KEY)
- maDatLich (INT FK)
- soTien (DECIMAL)
- phuongThuc (NVARCHAR) - ChuyenKhoan | MoMo
- trangThai (NVARCHAR)
- ngayThanhToan (DATETIME)
- maGiaoDich (VARCHAR)
- ghiChu (NVARCHAR)

### ✅ Có trong FE (Payment.java):
- Thông tin lớp học
- Số lượng đặt
- Tổng tiền
- Phương thức thanh toán (MoMo, Thẻ)
- Thông tin liên hệ (Tên, Email, SĐT)

**THIẾU TRONG DATABASE:**
- ❌ Thông tin người đặt trong Payment (tên, email, SĐT) - nên lưu vào DatLich hoặc tạo bảng riêng

**CẦN THÊM:**
```sql
ALTER TABLE DatLich
ADD tenNguoiDat NVARCHAR(100),
    emailNguoiDat VARCHAR(100),
    sdtNguoiDat VARCHAR(15);
```

---

## 6️⃣ ƯU ĐÃI (UuDai & Voucher)

### ✅ Có trong Database:
- maUuDai (INT PRIMARY KEY)
- maCode (VARCHAR)
- tenUuDai (NVARCHAR)
- moTa (NVARCHAR)
- loaiGiam (NVARCHAR) - PhanTram | SoTien
- giaTriGiam (DECIMAL)
- giamToiDa (DECIMAL)
- soLuong (INT)
- soLuongDaSuDung (INT)
- ngayBatDau (DATE)
- ngayKetThuc (DATE)
- trangThai (NVARCHAR)
- ngayTao (DATETIME)

### ✅ Có trong FE (Voucher.java):
- hinhAnh (int)
- tieuDe (String)
- moTa (String)
- HSD (String) - Hạn sử dụng
- duocChon (boolean)

**THIẾU TRONG DATABASE:**
- ❌ hinhAnh (VARCHAR) - Ảnh voucher

**CẦN THÊM:**
```sql
ALTER TABLE UuDai
ADD hinhAnh VARCHAR(255);
```

---

## 7️⃣ THÔNG BÁO (ThongBao & Notice)

### ✅ Có trong Database:
- maThongBao (INT PRIMARY KEY)
- maNguoiNhan (INT FK)
- tieuDe (NVARCHAR)
- noiDung (NVARCHAR)
- loaiThongBao (NVARCHAR)
- daDoc (BIT)
- ngayTao (DATETIME)

### ✅ Có trong FE (Notice.java):
- tieuDeTB (String)
- noiDungTB (String)
- thoiGianTB (String)
- anhTB (int) - Resource ID
- trangThai (boolean) - đã đọc/chưa đọc

**THIẾU TRONG DATABASE:**
- ❌ hinhAnh (VARCHAR) - Ảnh thông báo

**CẦN THÊM:**
```sql
ALTER TABLE ThongBao
ADD hinhAnh VARCHAR(255);
```

---

## 8️⃣ LỊCH SỬ ĐẶT LỊCH (OrderHistory)

### ✅ Có trong FE (OrderHistory.java):
- hinhAnh (int)
- tieuDe (String)
- soLuongNguoi (String)
- lich (String) - Ngày giờ
- diaDiem (String)
- gia (String)
- thoiGianHuy (String)
- trangThai (String)

### ✅ Database có đủ thông tin từ:
- DatLich (trạng thái, ngày đặt)
- LopHoc (thông tin lớp)
- ThanhToan (giá tiền)

**OK - Không thiếu gì**

---

## 9️⃣ ĐÁNH GIÁ (DanhGia & Review)

### ✅ Có trong Database:
- maDanhGia (INT PRIMARY KEY)
- maHocVien (INT FK)
- maLopHoc (INT FK)
- diemDanhGia (INT) - 1-5
- binhLuan (NVARCHAR)
- ngayDanhGia (DATETIME)

### ❌ FE chưa implement:
- Review.java chỉ là Activity rỗng
- Chưa có model cho đánh giá

**CẦN THÊM VÀO FE:**
```java
public class Review {
    private int maHocVien;
    private int maLopHoc;
    private int diemDanhGia; // 1-5 sao
    private String binhLuan;
    private String ngayDanhGia;
    private String tenHocVien;
    private String avatarHocVien;
}
```

---

## 🔟 YÊU THÍCH (YeuThich & Favorite)

### ✅ Có trong Database:
- maYeuThich (INT PRIMARY KEY)
- maHocVien (INT FK)
- maLopHoc (INT FK)
- ngayThem (DATETIME)

### ✅ Có trong FE:
- Class.java có trường `isFavorite` (boolean)
- FavoriteFragment để hiển thị danh sách yêu thích

**OK - Đầy đủ**

---

## 1️⃣1️⃣ HÓA ĐƠN (HoaDon & Bill)

### ✅ Có trong Database:
- maHoaDon (INT PRIMARY KEY)
- maThanhToan (INT FK)
- soHoaDon (VARCHAR)
- ngayXuatHoaDon (DATETIME)
- tongTien (DECIMAL)
- VAT (DECIMAL)
- thanhTien (DECIMAL)
- trangThai (NVARCHAR)
- filePDF (VARCHAR)

### ❌ FE chưa implement:
- Bill.java chỉ là Activity rỗng
- Chưa có model cho hóa đơn

**CẦN THÊM VÀO FE:**
```java
public class Bill {
    private String soHoaDon;
    private String ngayXuatHoaDon;
    private double tongTien;
    private double VAT;
    private double thanhTien;
    private String trangThai;
    private String filePDF;
    // Thông tin lớp học
    // Thông tin người đặt
}
```

---

## 1️⃣2️⃣ LỊCH HỌC (LichHoc & Calendar)

### ✅ Có trong Database:
- maLichHoc (INT PRIMARY KEY)
- maLopHoc (INT FK)
- ngayHoc (DATE)
- gioBatDau (TIME)
- gioKetThuc (TIME)
- noiDung (NVARCHAR)
- trangThai (NVARCHAR) - "Chưa Học"

### ✅ Có trong FE:
- CalendarActivity.java
- CalendarDay.java
- CalendarMonth.java
- CalendarAdapter.java

**THIẾU TRONG FE:**
- ❌ Không có model để lưu lịch học của từng lớp
- ❌ Calendar hiện tại chỉ là UI chọn ngày, chưa hiển thị lịch học

**CẦN THÊM VÀO FE:**
```java
public class Schedule {
    private int maLichHoc;
    private int maLopHoc;
    private String tenLopHoc;
    private String ngayHoc;
    private String gioBatDau;
    private String gioKetThuc;
    private String noiDung;
    private String trangThai;
}
```

---

## 1️⃣3️⃣ OTP

### ✅ Có trong Database:
- maOTP (INT PRIMARY KEY)
- maNguoiDung (INT FK)
- maXacThuc (VARCHAR(6))
- loaiOTP (NVARCHAR) - DangKy | QuenMatKhau | XacThuc
- thoiGianTao (DATETIME)
- thoiGianHetHan (DATETIME)
- daSuDung (BIT)

### ✅ Có trong FE:
- OtpVerification.java
- 6 ô nhập OTP (otpBox1-6)

**OK - Đầy đủ**

---

## 📊 TỔNG KẾT CÁC VẤN ĐỀ CHÍNH

### 🔴 THIẾU NGHIÊM TRỌNG TRONG DATABASE:

1. **Vai trò người dùng (Role)**
   ```sql
   ALTER TABLE NguoiDung
   ADD vaiTro NVARCHAR(20) DEFAULT N'HocVien'; -- HocVien | GiaoVien | Admin
   ```

2. **Danh mục món ăn và món ăn trong lớp học**
   ```sql
   CREATE TABLE DanhMucMonAn (...);
   CREATE TABLE MonAn (...);
   ```

3. **Số lượng người đặt trong booking**
   ```sql
   ALTER TABLE DatLich
   ADD soLuongNguoi INT DEFAULT 1,
       tongTien DECIMAL(10,2),
       tenNguoiDat NVARCHAR(100),
       emailNguoiDat VARCHAR(100),
       sdtNguoiDat VARCHAR(15);
   ```

4. **Hình ảnh cho Voucher và Thông báo**
   ```sql
   ALTER TABLE UuDai ADD hinhAnh VARCHAR(255);
   ALTER TABLE ThongBao ADD hinhAnh VARCHAR(255);
   ```

### 🟡 THIẾU TRONG FE:

1. **Form đăng ký thiếu trường:**
   - Họ tên
   - Số điện thoại
   - Địa chỉ

2. **Model chưa có:**
   - Review (đánh giá chi tiết)
   - Bill (hóa đơn chi tiết)
   - Schedule (lịch học chi tiết)

3. **ID không được lưu:**
   - maLopHoc
   - maHocVien
   - maDatLich
   - Các ID khác cần thiết cho API

### 🟢 ĐỀ XUẤT BỔ SUNG:

1. **Thêm bảng Giáo viên (nếu cần thông tin chi tiết)**
   ```sql
   CREATE TABLE GiaoVien (
       maGiaoVien INT PRIMARY KEY,
       maNguoiDung INT UNIQUE,
       chuyenMon NVARCHAR(200),
       kinhNghiem NVARCHAR(MAX),
       hinhAnh VARCHAR(255),
       moTa NVARCHAR(MAX),
       FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
   );
   ```

2. **Thêm bảng Hình ảnh lớp học (nhiều ảnh)**
   ```sql
   CREATE TABLE HinhAnhLopHoc (
       maHinhAnh INT PRIMARY KEY,
       maLopHoc INT NOT NULL,
       duongDan VARCHAR(255) NOT NULL,
       thuTu INT DEFAULT 1,
       FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
   );
   ```

---

## ✅ KẾT LUẬN

Database hiện tại **thiếu nhiều thông tin quan trọng** mà FE đang sử dụng:
- ❌ Danh mục món ăn và món ăn trong lớp học
- ❌ Vai trò người dùng
- ❌ Số lượng người đặt trong booking
- ❌ Thông tin người đặt (tên, email, SĐT) trong thanh toán
- ❌ Hình ảnh cho voucher và thông báo

FE cũng **thiếu một số model và trường** cần thiết:
- ❌ ID các entity để gọi API
- ❌ Model Review, Bill, Schedule chi tiết
- ❌ Form đăng ký thiếu trường họ tên, SĐT, địa chỉ

**Cần cập nhật cả Database và FE để đồng bộ!**
