# THÔNG TIN NHÓM VÀ GIỚI THIỆU DỰ ÁN

---

## I. THÔNG TIN NHÓM

| STT | Họ và Tên | MSSV | Vai trò | Email |
|-----|-----------|------|---------|-------|
| 1 | [Họ tên thành viên 1] | [MSSV] | Trưởng nhóm | [email] |
| 2 | [Họ tên thành viên 2] | [MSSV] | Thành viên | [email] |
| 3 | [Họ tên thành viên 3] | [MSSV] | Thành viên | [email] |

**Lớp:** [Tên lớp]  
**Môn học:** [Tên môn học]  
**Giảng viên hướng dẫn:** [Tên GVHD]

---

## II. ĐỐI TƯỢNG SỬ DỤNG

| STT | Đối tượng | Mô tả | Chức năng chính |
|-----|-----------|-------|-----------------|
| 1 | **Học viên** | Người dùng muốn học nấu ăn, tìm kiếm và đặt lịch các khóa học | - Đăng ký/đăng nhập tài khoản<br>- Tìm kiếm khóa học theo địa điểm, ngày<br>- Xem chi tiết khóa học, món ăn<br>- Đặt lịch và thanh toán qua Momo<br>- Áp dụng mã giảm giá<br>- Đánh giá khóa học sau khi hoàn thành<br>- Quản lý danh sách yêu thích<br>- Nhận thông báo nhắc nhở |
| 2 | **Giáo viên** | Đầu bếp chuyên nghiệp, người hướng dẫn các khóa học nấu ăn | - Được gán vào các lịch trình giảng dạy<br>- Có hồ sơ chuyên môn, kinh nghiệm<br>- Hiển thị thông tin trên ứng dụng cho học viên xem |
| 3 | **Quản trị viên (Admin)** | Người quản lý toàn bộ hệ thống qua WebAdmin | - Quản lý người dùng (CRUD)<br>- Quản lý giáo viên<br>- Quản lý khóa học, lịch trình<br>- Quản lý món ăn, danh mục<br>- Quản lý đơn đặt lịch<br>- Xem thống kê, báo cáo doanh thu<br>- Quản lý ưu đãi, mã giảm giá |

---

## III. GIỚI THIỆU DỰ ÁN

### 1. Tên dự án
**Local Cooking - Đặt Lịch Học Nấu Ăn Địa Phương**

### 2. Mô tả tổng quan
Local Cooking là nền tảng đặt lịch học nấu ăn trực tuyến, kết nối học viên với các đầu bếp chuyên nghiệp tại nhiều địa phương trên cả nước. Ứng dụng cho phép người dùng tìm kiếm, đặt lịch và tham gia các khóa học nấu ăn theo vùng miền như Hà Nội, Huế, Đà Nẵng, Cần Thơ...

### 3. Mục tiêu dự án
- Xây dựng nền tảng kết nối học viên với giáo viên dạy nấu ăn
- Quản lý khóa học, lịch trình, đặt lịch và thanh toán trực tuyến
- Cung cấp trải nghiệm học nấu ăn đa dạng theo vùng miền
- Hỗ trợ đánh giá, nhận xét sau khóa học
- Tích hợp thanh toán Momo và đăng nhập Google

---

## IV. CÔNG NGHỆ SỬ DỤNG

### 1. Backend

#### 1.1. Công nghệ Backend

| STT | Công nghệ | Phiên bản | Mô tả |
|-----|-----------|-----------|-------|
| 1 | Java | 21 | Ngôn ngữ lập trình chính |
| 2 | Spring Boot | 4.0.0 | Framework phát triển ứng dụng web |
| 3 | Spring Data JPA | - | ORM, truy vấn cơ sở dữ liệu |
| 4 | Spring Boot Starter Web | - | Xây dựng RESTful API |
| 5 | Spring Boot Starter Mail | - | Gửi email (OTP, thông báo) |
| 6 | Spring Boot DevTools | - | Hot reload trong quá trình phát triển |
| 7 | SQL Server | 2019+ | Hệ quản trị cơ sở dữ liệu |
| 8 | MSSQL JDBC Driver | - | Kết nối Java với SQL Server |
| 9 | Lombok | - | Giảm boilerplate code (getter, setter, constructor) |
| 10 | H2 Database | - | Database backup/testing |

#### 1.2. Tích hợp dịch vụ bên thứ ba (Backend)

| STT | Dịch vụ | Công nghệ/Thư viện | Phiên bản | Mô tả |
|-----|---------|-------------------|-----------|-------|
| 1 | Thanh toán Momo | Jackson Databind | 2.15.2 | Xử lý JSON request/response với Momo API |
| 2 | Thanh toán Momo | OkHttp | 4.12.0 | HTTP Client gọi Momo Payment API |
| 3 | Đăng nhập Google | Google API Client | 2.2.0 | Xác thực Google OAuth 2.0 |
| 4 | Gửi OTP | Retrofit | 2.9.0 | HTTP Client cho các API bên ngoài |
| 5 | Gửi OTP | Gson Converter | 2.9.0 | Chuyển đổi JSON cho Retrofit |

---

### 2. Frontend (Android)

#### 2.1. Công nghệ Frontend Android

| STT | Công nghệ | Phiên bản | Mô tả |
|-----|-----------|-----------|-------|
| 1 | Android SDK | API 22-36 | Nền tảng phát triển ứng dụng Android |
| 2 | Java | 11 | Ngôn ngữ lập trình |
| 3 | AndroidX AppCompat | - | Tương thích ngược với các phiên bản Android cũ |
| 4 | Material Design | 1.12.0 | Thư viện UI Components theo chuẩn Google |
| 5 | ConstraintLayout | - | Layout linh hoạt, hiệu năng cao |
| 6 | RecyclerView | - | Hiển thị danh sách hiệu quả |
| 7 | ViewPager2 | - | Slide/swipe giữa các màn hình |
| 8 | CardView | - | Hiển thị nội dung dạng thẻ |
| 9 | Fragment | - | Quản lý giao diện theo module |

#### 2.2. Tích hợp dịch vụ bên thứ ba (Android)

| STT | Dịch vụ | Công nghệ/Thư viện | Phiên bản | Mô tả |
|-----|---------|-------------------|-----------|-------|
| 1 | Gọi API REST | Retrofit | 2.9.0 | HTTP Client gọi Backend API |
| 2 | Gọi API REST | Gson Converter | 2.9.0 | Chuyển đổi JSON sang Object |
| 3 | Gọi API REST | OkHttp | 4.12.0 | HTTP Client, logging interceptor |
| 4 | Load hình ảnh | Glide | 4.16.0 | Tải và cache hình ảnh hiệu quả |
| 5 | Zoom hình ảnh | PhotoView | 2.3.0 | Phóng to/thu nhỏ hình ảnh |
| 6 | Phát video | ExoPlayer (Media3) | 1.2.1 | Phát video đánh giá |
| 7 | Đăng nhập Google | Google Sign-In | 21.0.0 | Xác thực tài khoản Google |
| 8 | Định vị | Play Services Location | 21.0.1 | Lấy vị trí người dùng |
| 9 | Avatar tròn | CircleImageView | 3.1.0 | Hiển thị ảnh đại diện dạng tròn |

---

### 3. WebAdmin

#### 3.1. Công nghệ WebAdmin

| STT | Công nghệ | Phiên bản | Mô tả |
|-----|-----------|-----------|-------|
| 1 | HTML5 | - | Cấu trúc trang web |
| 2 | CSS3 | - | Định dạng giao diện |
| 3 | JavaScript (Vanilla) | ES6+ | Logic xử lý phía client |
| 4 | Fetch API | - | Gọi RESTful API từ Backend |
| 5 | Font Awesome | 6.4.0 | Thư viện icon |
| 6 | Live Server (VS Code) | - | Server phát triển local |

---

## V. KIẾN TRÚC HỆ THỐNG

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Android App   │     │    WebAdmin     │     │   Momo Server   │
│   (Học viên)    │     │  (Quản trị)     │     │   (Thanh toán)  │
└────────┬────────┘     └────────┬────────┘     └────────┬────────┘
         │                       │                       │
         │      REST API         │      REST API         │
         └───────────┬───────────┴───────────────────────┘
                     │
              ┌──────▼──────┐
              │   Backend   │
              │ Spring Boot │
              │  Port 8080  │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │  SQL Server │
              │  Database   │
              └─────────────┘
```

---

## VI. CƠ SỞ DỮ LIỆU

### Tổng quan: 18 bảng

---

### Bảng 1: NguoiDung (Người Dùng)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maNguoiDung | INT | PRIMARY KEY, IDENTITY | Mã người dùng (tự tăng) |
| tenDangNhap | VARCHAR(50) | UNIQUE, NOT NULL | Tên đăng nhập |
| matKhau | VARCHAR(255) | NOT NULL | Mật khẩu (đã mã hóa) |
| hoTen | NVARCHAR(100) | | Họ và tên |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Email |
| soDienThoai | VARCHAR(15) | | Số điện thoại |
| gioiTinh | NVARCHAR(10) | CHECK (Nam, Nữ, Khác) | Giới tính |
| diaChi | NVARCHAR(255) | | Địa chỉ |
| vaiTro | NVARCHAR(20) | DEFAULT 'HocVien' | Vai trò (Admin, GiaoVien, HocVien) |
| trangThai | NVARCHAR(20) | DEFAULT 'HoatDong' | Trạng thái tài khoản |
| ngayTao | DATETIME | DEFAULT GETDATE() | Ngày tạo tài khoản |
| lanCapNhatCuoi | DATETIME | DEFAULT GETDATE() | Lần cập nhật cuối |

---

### Bảng 2: OTP (Mã Xác Thực)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maOTP | INT | PRIMARY KEY, IDENTITY | Mã OTP (tự tăng) |
| maNguoiDung | INT | FOREIGN KEY | Mã người dùng |
| maXacThuc | VARCHAR(6) | NOT NULL | Mã xác thực 6 số |
| loaiOTP | NVARCHAR(30) | NOT NULL | Loại OTP (DangKy, QuenMatKhau, DoiMatKhau) |
| thoiGianTao | DATETIME | DEFAULT GETDATE() | Thời gian tạo |
| thoiGianHetHan | DATETIME | NOT NULL | Thời gian hết hạn (5 phút) |
| daSuDung | BIT | DEFAULT 0 | Đã sử dụng chưa |

---

### Bảng 3: GiaoVien (Giáo Viên)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maGiaoVien | INT | PRIMARY KEY, IDENTITY | Mã giáo viên (tự tăng) |
| maNguoiDung | INT | UNIQUE, NOT NULL, FK | Liên kết với NguoiDung |
| chuyenMon | NVARCHAR(200) | | Chuyên môn (Ẩm thực Việt, Bánh ngọt...) |
| kinhNghiem | NVARCHAR(MAX) | | Số năm kinh nghiệm |
| lichSuKinhNghiem | NVARCHAR(MAX) | | Lịch sử công tác chi tiết |
| moTa | NVARCHAR(MAX) | | Mô tả về giáo viên |
| hinhAnh | VARCHAR(255) | | Đường dẫn ảnh đại diện |

---

### Bảng 4: KhoaHoc (Khóa Học)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maKhoaHoc | INT | PRIMARY KEY, IDENTITY | Mã khóa học (tự tăng) |
| tenKhoaHoc | NVARCHAR(200) | NOT NULL | Tên khóa học |
| moTa | NVARCHAR(500) | | Mô tả ngắn |
| gioiThieu | NVARCHAR(MAX) | | Giới thiệu chi tiết |
| giaTriSauBuoiHoc | NVARCHAR(MAX) | | Giá trị học viên nhận được |
| giaTien | DECIMAL(10,2) | NOT NULL | Giá tiền (VNĐ) |
| hinhAnh | VARCHAR(255) | | Ảnh banner khóa học |
| soLuongDanhGia | INT | DEFAULT 0 | Số lượng đánh giá |
| saoTrungBinh | FLOAT | DEFAULT 0 | Điểm sao trung bình (1-5) |
| coUuDai | BIT | DEFAULT 0 | Có ưu đãi không |
| ngayTao | DATETIME | DEFAULT GETDATE() | Ngày tạo |

---

### Bảng 5: LichTrinhLopHoc (Lịch Trình Lớp Học)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maLichTrinh | INT | PRIMARY KEY, IDENTITY | Mã lịch trình (tự tăng) |
| maKhoaHoc | INT | NOT NULL, FK | Mã khóa học |
| maGiaoVien | INT | NOT NULL, FK | Mã giáo viên phụ trách |
| thuTrongTuan | VARCHAR(50) | | Các thứ trong tuần (2,3,4,5,6,7,CN) |
| gioBatDau | TIME(0) | | Giờ bắt đầu |
| gioKetThuc | TIME(0) | | Giờ kết thúc |
| diaDiem | NVARCHAR(255) | | Địa điểm học |
| soLuongToiDa | INT | DEFAULT 20 | Số học viên tối đa |
| trangThai | BIT | DEFAULT 1 | Trạng thái (1=Hoạt động) |

---

### Bảng 6: DanhMucMonAn (Danh Mục Món Ăn)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maDanhMuc | INT | PRIMARY KEY, IDENTITY | Mã danh mục (tự tăng) |
| tenDanhMuc | NVARCHAR(100) | NOT NULL | Tên danh mục (Khai vị, Món chính, Tráng miệng) |
| iconDanhMuc | VARCHAR(255) | | Icon danh mục |
| thuTu | INT | DEFAULT 1 | Thứ tự hiển thị |

---

### Bảng 7: MonAn (Món Ăn)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maMonAn | INT | PRIMARY KEY, IDENTITY | Mã món ăn (tự tăng) |
| maKhoaHoc | INT | NOT NULL, FK | Mã khóa học |
| maDanhMuc | INT | NOT NULL, FK | Mã danh mục |
| tenMon | NVARCHAR(200) | NOT NULL | Tên món ăn |
| gioiThieu | NVARCHAR(MAX) | | Giới thiệu món ăn |
| nguyenLieu | NVARCHAR(MAX) | | Danh sách nguyên liệu |

---

### Bảng 8: HinhAnhMonAn (Hình Ảnh Món Ăn)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maHinhAnh | INT | PRIMARY KEY, IDENTITY | Mã hình ảnh (tự tăng) |
| maMonAn | INT | NOT NULL, FK (CASCADE) | Mã món ăn |
| duongDan | VARCHAR(255) | NOT NULL | Đường dẫn ảnh |
| thuTu | INT | DEFAULT 1 | Thứ tự trong slideshow |

---

### Bảng 9: HinhAnhKhoaHoc (Hình Ảnh Khóa Học)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maHinhAnh | INT | PRIMARY KEY, IDENTITY | Mã hình ảnh (tự tăng) |
| maKhoaHoc | INT | NOT NULL, FK | Mã khóa học |
| duongDan | VARCHAR(255) | NOT NULL | Đường dẫn ảnh |
| thuTu | INT | DEFAULT 1 | Thứ tự trong slideshow |

---

### Bảng 10: ThongBao (Thông Báo)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maThongBao | INT | PRIMARY KEY, IDENTITY | Mã thông báo (tự tăng) |
| maNguoiNhan | INT | FK | Mã người nhận |
| tieuDe | NVARCHAR(255) | NOT NULL | Tiêu đề thông báo |
| noiDung | NVARCHAR(MAX) | NOT NULL | Nội dung thông báo |
| loaiThongBao | NVARCHAR(30) | DEFAULT 'Hệ Thống' | Loại (Hệ Thống, DatLich, NhacNho) |
| hinhAnh | VARCHAR(255) | | Hình ảnh đính kèm |
| daDoc | BIT | DEFAULT 0 | Đã đọc chưa |
| ngayTao | DATETIME | DEFAULT GETDATE() | Ngày tạo |

---

### Bảng 11: YeuThich (Yêu Thích)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maYeuThich | INT | PRIMARY KEY, IDENTITY | Mã yêu thích (tự tăng) |
| maHocVien | INT | NOT NULL, FK | Mã học viên |
| maKhoaHoc | INT | NOT NULL, FK | Mã khóa học |
| ngayThem | DATETIME | DEFAULT GETDATE() | Ngày thêm vào yêu thích |

**Constraint:** UNIQUE (maHocVien, maKhoaHoc) - Mỗi học viên chỉ yêu thích 1 khóa học 1 lần

---

### Bảng 12: UuDai (Ưu Đãi)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maUuDai | INT | PRIMARY KEY, IDENTITY | Mã ưu đãi (tự tăng) |
| maCode | VARCHAR(50) | UNIQUE, NOT NULL | Mã code ưu đãi |
| tenUuDai | NVARCHAR(200) | NOT NULL | Tên ưu đãi |
| moTa | NVARCHAR(MAX) | | Mô tả ưu đãi |
| loaiGiam | NVARCHAR(20) | NOT NULL | Loại giảm (PhanTram, SoTien) |
| giaTriGiam | DECIMAL(10,2) | NOT NULL | Giá trị giảm |
| giamToiDa | DECIMAL(10,2) | | Giảm tối đa (nếu theo %) |
| soLuong | INT | | Số lượng mã |
| soLuongDaSuDung | INT | DEFAULT 0 | Số lượng đã sử dụng |
| loaiUuDai | NVARCHAR(50) | | Loại ưu đãi (Nhóm, CaNhan) |
| dieuKienSoLuong | INT | | Điều kiện số người tối thiểu |
| ngayBatDau | DATE | NOT NULL | Ngày bắt đầu |
| ngayKetThuc | DATE | NOT NULL | Ngày kết thúc |
| hinhAnh | VARCHAR(255) | | Hình ảnh ưu đãi |
| trangThai | NVARCHAR(20) | DEFAULT 'Hoạt Động' | Trạng thái |
| ngayTao | DATETIME | DEFAULT GETDATE() | Ngày tạo |

---

### Bảng 13: DatLich (Đặt Lịch)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maDatLich | INT | PRIMARY KEY, IDENTITY | Mã đặt lịch (tự tăng) |
| maHocVien | INT | NOT NULL, FK | Mã học viên |
| maLichTrinh | INT | NOT NULL, FK | Mã lịch trình |
| ngayThamGia | DATE | NOT NULL | Ngày tham gia lớp học |
| soLuongNguoi | INT | DEFAULT 1 | Số lượng người đăng ký |
| tongTien | DECIMAL(10,2) | NOT NULL | Tổng tiền |
| tenNguoiDat | NVARCHAR(100) | | Tên người đặt |
| emailNguoiDat | VARCHAR(100) | | Email người đặt |
| sdtNguoiDat | VARCHAR(15) | | SĐT người đặt |
| maUuDai | INT | FK | Mã ưu đãi (nếu có) |
| soTienGiam | DECIMAL(10,2) | | Số tiền được giảm |
| ngayDat | DATETIME | DEFAULT GETDATE() | Ngày đặt |
| thoiGianHetHan | DATETIME | | Hết hạn thanh toán (10 phút) |
| thoiGianHuy | DATETIME | | Thời gian hủy đơn |
| trangThai | NVARCHAR(30) | DEFAULT 'Đặt trước' | Trạng thái (Đặt trước, Đã hoàn thành, Đã huỷ) |
| ghiChu | NVARCHAR(MAX) | | Ghi chú |

---

### Bảng 14: DanhGia (Đánh Giá)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maDanhGia | INT | PRIMARY KEY, IDENTITY | Mã đánh giá (tự tăng) |
| maDatLich | INT | FK | Mã đặt lịch |
| maHocVien | INT | NOT NULL, FK | Mã học viên |
| maKhoaHoc | INT | NOT NULL, FK | Mã khóa học |
| diemDanhGia | INT | CHECK (1-5) | Điểm đánh giá (1-5 sao) |
| binhLuan | NVARCHAR(MAX) | | Bình luận |
| ngayDanhGia | DATETIME | DEFAULT GETDATE() | Ngày đánh giá |

---

### Bảng 15: HinhAnhDanhGia (Hình Ảnh Đánh Giá)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maHinhAnh | INT | PRIMARY KEY, IDENTITY | Mã hình ảnh (tự tăng) |
| maDanhGia | INT | NOT NULL, FK (CASCADE) | Mã đánh giá |
| duongDan | VARCHAR(500) | NOT NULL | Đường dẫn file |
| loaiFile | NVARCHAR(20) | DEFAULT 'image' | Loại file (image, video) |
| thuTu | INT | DEFAULT 1 | Thứ tự hiển thị |
| ngayTao | DATETIME | DEFAULT GETDATE() | Ngày tạo |

---

### Bảng 16: ThanhToan (Thanh Toán)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maThanhToan | INT | PRIMARY KEY, IDENTITY | Mã thanh toán (tự tăng) |
| maDatLich | INT | NOT NULL, FK | Mã đặt lịch |
| soTien | DECIMAL(10,2) | NOT NULL | Số tiền thanh toán |
| phuongThuc | NVARCHAR(30) | NOT NULL | Phương thức (Momo) |
| requestId | VARCHAR(100) | | Request ID gửi Momo |
| orderId | VARCHAR(100) | UNIQUE | Order ID hệ thống |
| transId | VARCHAR(100) | | Transaction ID từ Momo |
| payUrl | TEXT | | URL thanh toán Momo |
| deeplink | TEXT | | Deep link mở app Momo |
| qrCodeUrl | TEXT | | QR Code URL |
| resultCode | INT | | Mã kết quả (0=thành công) |
| message | NVARCHAR(255) | | Thông báo từ Momo |
| trangThai | BIT | DEFAULT 0 | Trạng thái (0=Chưa TT, 1=Đã TT) |
| thoiGianTao | DATETIME | DEFAULT GETDATE() | Thời gian tạo giao dịch |
| ngayThanhToan | DATETIME | | Thời gian thanh toán thành công |
| thoiGianCapNhat | DATETIME | | Lần cập nhật cuối |
| signature | VARCHAR(255) | | Chữ ký từ Momo |
| ghiChu | NVARCHAR(MAX) | | Ghi chú |

---

### Bảng 17: LichSuUuDai (Lịch Sử Ưu Đãi)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maLichSu | INT | PRIMARY KEY, IDENTITY | Mã lịch sử (tự tăng) |
| maUuDai | INT | NOT NULL, FK | Mã ưu đãi |
| maDatLich | INT | NOT NULL, FK | Mã đặt lịch |
| soTienGiam | DECIMAL(10,2) | NOT NULL | Số tiền được giảm |
| ngaySuDung | DATETIME | DEFAULT GETDATE() | Ngày sử dụng |

---

### Bảng 18: HoaDon (Hóa Đơn)

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---------|--------------|-----------|-------|
| maHoaDon | INT | PRIMARY KEY, IDENTITY | Mã hóa đơn (tự tăng) |
| maThanhToan | INT | NOT NULL, FK | Mã thanh toán |
| soHoaDon | VARCHAR(50) | UNIQUE, NOT NULL | Số hóa đơn |
| ngayXuatHoaDon | DATETIME | DEFAULT GETDATE() | Ngày xuất hóa đơn |
| tongTien | DECIMAL(10,2) | NOT NULL | Tổng tiền |
| VAT | DECIMAL(10,2) | DEFAULT 0 | Thuế VAT |
| thanhTien | DECIMAL(10,2) | NOT NULL | Thành tiền (sau VAT) |
| trangThai | NVARCHAR(20) | DEFAULT 'Đã Xuất' | Trạng thái |
| filePDF | VARCHAR(255) | | Đường dẫn file PDF |

---

### Sơ đồ quan hệ giữa các bảng

```
NguoiDung (1) ──────< (N) OTP
     │
     │ (1)
     ▼
GiaoVien (1) ──────< (N) LichTrinhLopHoc >────── (1) KhoaHoc
                              │                        │
                              │                        │ (1)
                              ▼                        ▼
                         DatLich (N) >────── (1) MonAn >────── (1) DanhMucMonAn
                              │                   │
                              │                   ▼
                              │              HinhAnhMonAn
                              │
                              ▼
                    ┌─────────┴─────────┐
                    │                   │
               ThanhToan            DanhGia
                    │                   │
                    ▼                   ▼
                 HoaDon          HinhAnhDanhGia

NguoiDung (1) ──────< (N) YeuThich >────── (1) KhoaHoc
NguoiDung (1) ──────< (N) ThongBao
DatLich (N) >────── (1) UuDai
UuDai (1) ──────< (N) LichSuUuDai
```

---

## VII. TÍNH NĂNG CHÍNH

### 1. Ứng dụng Android (Học viên)

#### Xác thực & Tài khoản
- Đăng ký tài khoản mới với xác thực OTP qua email
- Đăng nhập bằng email/mật khẩu hoặc Google Sign-In
- Quên mật khẩu với OTP
- Đổi mật khẩu có xác thực OTP

#### Tìm kiếm & Khám phá
- Tìm kiếm khóa học theo địa điểm (Hà Nội, Huế, Đà Nẵng, Cần Thơ...)
- Lọc theo ngày học
- Xem chi tiết khóa học (mô tả, giá, đánh giá, món ăn)
- Xem lịch trình và số chỗ còn trống

#### Đặt lịch & Thanh toán
- Chọn lịch trình, ngày tham gia, số người
- Áp dụng mã giảm giá
- Thanh toán qua Momo
- Xem lịch sử đặt lịch
- Hủy đơn (trước 15 phút khai giảng)

#### Đánh giá & Thông báo
- Đánh giá khóa học sau khi hoàn thành (1-5 sao)
- Upload hình ảnh/video đánh giá
- Nhận thông báo nhắc nhở trước 1 ngày và 30 phút
- Thông báo đặt lịch thành công

#### Yêu thích
- Thêm/xóa khóa học yêu thích
- Xem danh sách yêu thích

### 2. WebAdmin (Quản trị viên)

#### Dashboard
- Thống kê tổng quan (người dùng, khóa học, đặt lịch, doanh thu)
- Hoạt động gần đây
- Đơn hàng mới nhất

#### Quản lý Người dùng
- Xem danh sách người dùng
- Thêm/sửa/xóa người dùng
- Phân quyền (Admin, Giáo viên, Học viên)

#### Quản lý Giáo viên
- Thông tin giáo viên
- Chuyên môn, kinh nghiệm
- Lịch sử công tác

#### Quản lý Khóa học
- CRUD khóa học
- Upload hình ảnh
- Cài đặt giá, ưu đãi

#### Quản lý Lịch trình
- Tạo lịch trình cho khóa học
- Gán giáo viên
- Cài đặt thời gian, địa điểm, số lượng tối đa

#### Quản lý Đặt lịch
- Xem danh sách đặt lịch
- Cập nhật trạng thái (Đặt trước, Đã hoàn thành, Đã hủy)
- Xem chi tiết thanh toán

#### Quản lý Món ăn
- CRUD món ăn
- Gán món ăn vào khóa học và danh mục
- Upload hình ảnh món ăn

---

## VIII. API ENDPOINTS

### Base URL: `http://localhost:8080/api`

| Module | Endpoints chính |
|--------|-----------------|
| Người dùng | `/nguoidung`, `/nguoidung/login`, `/nguoidung/register` |
| OTP | `/otp/send`, `/otp/verify` |
| Quên mật khẩu | `/quenmatkhau/forgot-password`, `/quenmatkhau/reset-password` |
| Giáo viên | `/giaovien` |
| Khóa học | `/khoahoc`, `/khoahoc/search` |
| Lịch trình | `/lichtrinh`, `/lichtrinh/check-seats` |
| Danh mục món ăn | `/danhmucmonan` |
| Món ăn | `/monan` |
| Đặt lịch | `/datlich`, `/datlich/dashboard` |
| Thanh toán | `/thanhtoan` |
| Đánh giá | `/danhgia`, `/danhgia/tao`, `/danhgia/kiemtra/{maDatLich}` |
| Thông báo | `/thongbao` |
| Ưu đãi | `/uudai`, `/uudai/apply` |
| Yêu thích | `/yeuthich` |

---

## IX. HƯỚNG DẪN CÀI ĐẶT

### 1. Yêu cầu hệ thống
- Java JDK 21
- SQL Server 2019+
- Android Studio (cho FE)
- Node.js (cho Live Server WebAdmin)

### 2. Cài đặt Database
```sql
-- Chạy file DatLichHocNauAnDiaPhuong.sql để tạo database
```

### 3. Chạy Backend
```bash
cd BE
.\gradlew clean build -x test
.\gradlew bootRun
```
Backend chạy tại: `http://localhost:8080`

### 4. Chạy WebAdmin
- Mở folder WebAdmin bằng VS Code
- Cài extension Live Server
- Click "Go Live" để chạy tại `http://localhost:5500`

### 5. Chạy Android App
- Mở folder FE bằng Android Studio
- Sync Gradle
- Chạy trên emulator hoặc thiết bị thật
- Cấu hình IP Backend trong file config

---

## X. DEMO & SCREENSHOTS

### 1. Màn hình đăng nhập
[Chèn ảnh]

### 2. Trang chủ - Tìm kiếm khóa học
[Chèn ảnh]

### 3. Chi tiết khóa học
[Chèn ảnh]

### 4. Đặt lịch & Thanh toán
[Chèn ảnh]

### 5. WebAdmin Dashboard
[Chèn ảnh]

---

## XI. KẾT LUẬN

### Kết quả đạt được
- Hoàn thành đầy đủ các tính năng theo yêu cầu
- Ứng dụng Android hoạt động ổn định
- WebAdmin quản lý hiệu quả
- Tích hợp thanh toán Momo thành công
- Hệ thống thông báo và đánh giá hoạt động tốt

### Hướng phát triển
- Thêm tính năng chat giữa học viên và giáo viên
- Tích hợp thêm phương thức thanh toán (VNPay, ZaloPay)
- Phát triển phiên bản iOS
- Thêm tính năng livestream lớp học
- Tối ưu hiệu năng và bảo mật

---

**Ngày hoàn thành:** [Ngày/Tháng/Năm]

**Nhóm thực hiện:** [Tên nhóm]
