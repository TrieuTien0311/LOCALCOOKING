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

### Mô tả Use Case theo Actor

#### 1. Actor: Học viên (Android App)

| STT | Use Case | Mô tả |
|-----|----------|-------|
| UC01 | Đăng ký tài khoản | Học viên tạo tài khoản mới bằng email, xác thực OTP qua email |
| UC02 | Đăng nhập | Học viên đăng nhập bằng email/mật khẩu hoặc Google Sign-In |
| UC03 | Quên mật khẩu | Học viên khôi phục mật khẩu qua OTP gửi về email |
| UC04 | Đổi mật khẩu | Học viên thay đổi mật khẩu với xác thực OTP |
| UC05 | Tìm kiếm khóa học | Học viên tìm kiếm khóa học theo địa điểm (Hà Nội, Huế, Đà Nẵng...) và ngày |
| UC06 | Xem chi tiết khóa học | Học viên xem thông tin khóa học: mô tả, giá, đánh giá, danh sách món ăn |
| UC07 | Xem lịch trình | Học viên xem các lịch trình của khóa học, số chỗ còn trống |
| UC08 | Đặt lịch học | Học viên chọn lịch trình, ngày tham gia, số người và xác nhận đặt lịch |
| UC09 | Áp dụng mã giảm giá | Học viên nhập mã ưu đãi để được giảm giá khi đặt lịch |
| UC10 | Thanh toán Momo | Học viên thanh toán đơn đặt lịch qua ví điện tử Momo |
| UC11 | Xem lịch sử đặt lịch | Học viên xem danh sách các đơn đặt lịch đã thực hiện |
| UC12 | Hủy đơn đặt lịch | Học viên hủy đơn đặt lịch (trước 15 phút khai giảng) |
| UC13 | Đánh giá khóa học | Học viên đánh giá sao (1-5) và viết bình luận sau khi hoàn thành khóa học |
| UC14 | Upload media đánh giá | Học viên tải lên hình ảnh/video kèm theo đánh giá |
| UC15 | Quản lý yêu thích | Học viên thêm/xóa khóa học vào danh sách yêu thích |
| UC16 | Xem thông báo | Học viên xem các thông báo từ hệ thống (nhắc nhở, đặt lịch thành công) |
| UC17 | Cập nhật thông tin cá nhân | Học viên chỉnh sửa họ tên, số điện thoại, địa chỉ |

#### 2. Actor: Quản trị viên - Admin (WebAdmin)

| STT | Use Case | Mô tả |
|-----|----------|-------|
| UC18 | Đăng nhập Admin | Admin đăng nhập vào hệ thống quản trị WebAdmin |
| UC19 | Xem Dashboard | Admin xem thống kê tổng quan: người dùng, khóa học, đặt lịch, doanh thu |
| UC20 | Quản lý người dùng | Admin xem, thêm, sửa, xóa thông tin người dùng |
| UC21 | Phân quyền người dùng | Admin gán vai trò cho người dùng (Admin, Giáo viên, Học viên) |
| UC22 | Quản lý giáo viên | Admin xem, thêm, sửa, xóa thông tin giáo viên (chuyên môn, kinh nghiệm) |
| UC23 | Quản lý khóa học | Admin xem, thêm, sửa, xóa khóa học (tên, mô tả, giá, hình ảnh) |
| UC24 | Quản lý lịch trình | Admin tạo lịch trình cho khóa học, gán giáo viên, cài đặt thời gian/địa điểm |
| UC25 | Quản lý danh mục món ăn | Admin xem, thêm, sửa, xóa danh mục món ăn (Khai vị, Món chính, Tráng miệng) |
| UC26 | Quản lý món ăn | Admin xem, thêm, sửa, xóa món ăn, gán vào khóa học và danh mục |
| UC27 | Quản lý đơn đặt lịch | Admin xem danh sách đặt lịch, cập nhật trạng thái (Đặt trước, Đã hoàn thành, Đã hủy) |
| UC28 | Quản lý ưu đãi | Admin tạo, sửa, xóa mã giảm giá, cài đặt điều kiện áp dụng |
| UC29 | Xem báo cáo doanh thu | Admin xem thống kê doanh thu theo ngày/tháng/năm |

#### 3. Actor: Hệ thống (System)

| STT | Use Case | Mô tả |
|-----|----------|-------|
| UC30 | Gửi OTP qua email | Hệ thống tự động gửi mã OTP đến email người dùng khi đăng ký/quên mật khẩu |
| UC31 | Gửi thông báo nhắc nhở | Hệ thống tự động gửi thông báo trước 1 ngày và 30 phút khi lớp học sắp diễn ra |
| UC32 | Cập nhật trạng thái đơn | Hệ thống tự động cập nhật trạng thái đơn đặt lịch khi hết hạn thanh toán |
| UC33 | Tính toán đánh giá | Hệ thống tự động tính điểm sao trung bình và số lượng đánh giá cho khóa học |
| UC34 | Xử lý callback Momo | Hệ thống nhận và xử lý kết quả thanh toán từ Momo |

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
| 6 | Spring Boot DevToo trong quá trình phát triển |
| 7 | SQL Server | 2019+ | Hệ quản trị cơ sở dữ liệu |
| 8 | MSSQL JDBC Driver | - | Kết nối Java với SQL Server |
| 9 | Lombok | - | Giảm boilerplate code (getter, setter, constructor) |
| 10 | H2 Database | - | Database backup/testing |
ls | - | Hot reload
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

| STT | Dịch vụ | Nhà cung cấp | Mục đích sử dụng |
|-----|---------|--------------|------------------|
| 1 | Google Sign-In | Google | Đăng nhập bằng tài khoản Google, xác thực người dùng nhanh chóng |
| 2 | Google Play Services Location | Google | Lấy vị trí hiện tại của người dùng để tìm kiếm khóa học gần đây |
| 3 | Momo Payment | Momo (M_Service) | Thanh toán trực tuyến cho đơn đặt lịch học |
| 4 | Glide | Bumptech | Tải, hiển thị và cache hình ảnh từ server |
| 5 | ExoPlayer (Media3) | Google/AndroidX | Phát video đánh giá của học viên |
| 6 | Retrofit | Square | Gọi RESTful API từ Backend |
| 7 | OkHttp | Square | HTTP Client, xử lý request/response |
| 8 | PhotoView | Chris Banes | Zoom phóng to/thu nhỏ hình ảnh chi tiết |
| 9 | CircleImageView | Henning Dodenhof | Hiển thị avatar người dùng dạng tròn |

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

## VII. CÁC CHỨC NĂNG CHI TIẾT

### 1. Module Xác thực (Authentication)

####    

**Mục đích:** Cho phép người dùng mới tạo tài khoản để sử dụng ứng dụng.

**Quy trình đăng ký:**
1. Người dùng nhập thông tin: email, mật khẩu, họ tên, số điện thoại
2. Hệ thống kiểm tra email đã tồn tại chưa
3. Gửi mã OTP 6 số đến email người dùng
4. Người dùng nhập mã OTP để xác thực
5. Mã hóa mật khẩu và lưu thông tin vào database
6. Trả về thông tin user đã tạo

**Validation:**
- Email phải đúng định dạng và chưa được sử dụng
- Mật khẩu đủ độ dài (tối thiểu 6 ký tự)
- Các trường bắt buộc không được để trống

#### 1.2. Đăng nhập

**Mục đích:** Xác thực người dùng và cấp quyền truy cập hệ thống.

**Quy trình đăng nhập:**
1. Người dùng nhập email và mật khẩu
2. Hệ thống tìm user theo email
3. So sánh mật khẩu với hash đã lưu
4. Nếu đúng: trả về thông tin user
5. Client lưu thông tin để sử dụng cho các request sau

**Đăng nhập Google:**
1. Người dùng chọn "Đăng nhập bằng Google"
2. Google trả về ID Token
3. Backend xác thực token với Google API
4. Tạo tài khoản mới hoặc đăng nhập nếu đã tồn tại

**Xử lý lỗi:**
- Email không tồn tại: Thông báo "Email hoặc mật khẩu không đúng"
- Mật khẩu sai: Thông báo "Email hoặc mật khẩu không đúng"
- Tài khoản bị khóa: Thông báo "Tài khoản đã bị vô hiệu hóa"

---

### 2. Module OTP & Quên mật khẩu

#### 2.1. Gửi OTP

**Mục đích:** Gửi mã xác thực đến email người dùng để đăng ký hoặc đặt lại mật khẩu.

**Quy trình:**
1. Người dùng nhập email
2. Hệ thống kiểm tra email có tồn tại không (với quên mật khẩu)
3. Sinh mã OTP 6 chữ số ngẫu nhiên
4. Lưu OTP vào database với thời hạn 5 phút
5. Gửi email chứa mã OTP đến người dùng

**Đặc điểm:**
- OTP có thời hạn 5 phút
- Mỗi email chỉ có 1 OTP valid tại một thời điểm
- Gửi lại OTP sẽ invalidate OTP cũ

#### 2.2. Xác thực OTP

**Mục đích:** Kiểm tra mã OTP người dùng nhập có đúng không.

**Quy trình:**
1. Người dùng nhập email và mã OTP đã nhận
2. Hệ thống so sánh với OTP đã lưu
3. Kiểm tra OTP còn hạn hay không
4. Trả về kết quả: hợp lệ hoặc không hợp lệ

#### 2.3. Đặt lại mật khẩu

**Mục đích:** Cho phép người dùng tạo mật khẩu mới sau khi xác thực OTP.

**Yêu cầu:** Phải xác thực OTP thành công trước đó.

**Quy trình:**
1. Người dùng nhập mật khẩu mới
2. Hệ thống mã hóa mật khẩu
3. Cập nhật vào database
4. Xóa OTP đã sử dụng

---

### 3. Module Quản lý người dùng

#### 3.1. Xem thông tin cá nhân

**Mục đích:** Lấy thông tin chi tiết của người dùng đang đăng nhập.

**Thông tin trả về:**
- Thông tin cơ bản: id, email, họ tên, số điện thoại
- Vai trò và trạng thái tài khoản
- Thông tin bổ sung: địa chỉ, giới tính

#### 3.2. Cập nhật thông tin cá nhân

**Mục đích:** Cho phép người dùng chỉnh sửa thông tin profile.

**Thông tin có thể cập nhật:**
- Họ tên, số điện thoại
- Địa chỉ, giới tính

**Thông tin KHÔNG thể tự thay đổi:**
- Email (định danh duy nhất)
- Vai trò (chỉ Admin mới thay đổi được)

#### 3.3. Đổi mật khẩu

**Mục đích:** Cho phép người dùng thay đổi mật khẩu khi đã đăng nhập.

**Yêu cầu:**
- Phải nhập mật khẩu cũ đúng
- Mật khẩu mới phải khác mật khẩu cũ
- Xác nhận mật khẩu mới phải khớp
- Xác thực OTP gửi về email

#### 3.4. Quản lý danh sách người dùng (Admin)

**Mục đích:** Admin có thể quản lý tất cả tài khoản trong hệ thống.

**Chức năng:**
- Xem danh sách tất cả người dùng
- Xem chi tiết từng người dùng
- Cập nhật thông tin người dùng
- Thay đổi trạng thái tài khoản (HoatDong/BiKhoa)
- Xóa tài khoản
- Phân quyền vai trò (Admin, GiaoVien, HocVien)

---

### 4. Module Quản lý Khóa học

#### 4.1. Xem danh sách khóa học

**Mục đích:** Hiển thị tất cả khóa học cho người dùng duyệt.

**Tính năng:**
- Tìm kiếm theo địa điểm
- Lọc theo ngày học
- Sắp xếp theo giá, đánh giá

**Bộ lọc khóa học:**

| Tiêu chí | Mô tả |
|----------|-------|
| diaDiem | Tìm theo địa điểm (Hà Nội, Huế, Đà Nẵng, Cần Thơ...) |
| ngayTimKiem | Lọc theo ngày học (YYYY-MM-DD) |
| coUuDai | Chỉ khóa học có ưu đãi |

#### 4.2. Xem chi tiết khóa học

**Mục đích:** Hiển thị đầy đủ thông tin một khóa học cụ thể.
**Thông tin hiển thị:**
- Tiêu đề, mô tả chi tiết, giới thiệu
- Hình ảnh khóa học
- Giá tiền, giá sau ưu đãi (nếu có)
- Số lượng đánh giá, điểm sao trung bình
- Danh sách món ăn theo danh mục
- Danh sách lịch trình với số chỗ còn trống
- Thông tin giáo viên

#### 4.3. Tạo khóa học mới (Admin)

**Mục đích:** Cho phép Admin tạo khóa học mới.

**Thông tin cần nhập:**

| Trường | Bắt buộc | Mô tả |
|--------|----------|-------|
| tenKhoaHoc | Có | Tên khóa học |
| moTa | Không | Mô tả ngắn (tối đa 500 ký tự) |
| gioiThieu | Không | Giới thiệu chi tiết |
| giaTriSauBuoiHoc | Không | Giá trị học viên nhận được |
| giaTien | Có | Giá tiền (VNĐ) |
| hinhAnh | Không | Ảnh banner khóa học |
| coUuDai | Không | Có ưu đãi không |

#### 4.4. Cập nhật khóa học

**Mục đích:** Chỉnh sửa thông tin khóa học đã tạo.

**Điều kiện:** Chỉ Admin mới có quyền sửa.

#### 4.5. Xóa khóa học

**Mục đích:** Xóa khóa học khỏi hệ thống.

**Lưu ý:** Xóa khóa học sẽ xóa cascade tất cả lịch trình, món ăn liên quan.

---

### 5. Module Đặt lịch học

#### 5.1. Đặt lịch tham gia khóa học

**Mục đích:** Cho phép học viên đăng ký tham gia một khóa học.

**Quy trình:**
1. Học viên xem chi tiết khóa học
2. Chọn lịch trình phù hợp
3. Chọn ngày tham gia
4. Nhập số lượng người
5. Hệ thống kiểm tra:
   - Lịch trình còn chỗ không?
   - Ngày tham gia hợp lệ không?
6. Nhập thông tin liên hệ (tên, email, SĐT)
7. Áp dụng mã giảm giá (nếu có)
8. Tạo đơn đặt lịch với status = "Đặt trước"
9. Chuyển đến thanh toán Momo

**Trạng thái đặt lịch:**

| Trạng thái | Mô tả |
|------------|-------|
| Đặt trước | Đã đặt, chờ thanh toán hoặc đã thanh toán nhưng lớp chưa diễn ra |
| Đã hoàn thành | Đã thanh toán và lớp học đã diễn ra |
| Đã huỷ | Đã hủy đơn |

#### 5.2. Thanh toán Momo

**Mục đích:** Cho phép học viên thanh toán đơn đặt lịch qua ví Momo.

**Quy trình:**
1. Hệ thống tạo request thanh toán đến Momo
2. Momo trả về payUrl và deeplink
3. Mở app Momo hoặc trang thanh toán
4. Học viên xác nhận thanh toán
5. Momo gọi callback về Backend
6. Cập nhật trạng thái thanh toán
7. Gửi thông báo đặt lịch thành công

**Thông tin thanh toán:**
- requestId: ID request gửi Momo
- orderId: Mã đơn hàng hệ thống
- transId: Mã giao dịch Momo
- resultCode: 0 = thành công

#### 5.3. Xem lịch sử đặt lịch

**Mục đích:** Học viên xem danh sách các đơn đặt lịch đã thực hiện.

**Phân loại:**
- Tất cả đơn
- Đặt trước (chờ diễn ra)
- Đã hoàn thành
- Đã hủy

#### 5.4. Hủy đơn đặt lịch

**Mục đích:** Cho phép học viên hủy đơn nếu không thể tham gia.

**Điều kiện:** Chỉ hủy được khi:
- Đơn đã thanh toán
- Trước thời gian lớp học diễn ra 15 phút

---

### 6. Module Đánh giá

#### 6.1. Kiểm tra trạng thái đánh giá

**Mục đích:** Kiểm tra học viên có thể đánh giá khóa học không.

**Trạng thái:**

| Trạng thái | Mô tả |
|------------|-------|
| CÓ THỂ ĐÁNH GIÁ | Đơn đã hoàn thành, chưa đánh giá |
| ĐÃ ĐÁNH GIÁ | Đã đánh giá rồi |
| KHÔNG THỂ ĐÁNH GIÁ | Đơn chưa hoàn thành |

#### 6.2. Tạo đánh giá

**Mục đích:** Cho phép học viên đánh giá khóa học sau khi hoàn thành.

**Thông tin đánh giá:**
- Điểm sao (1-5)
- Bình luận
- Hình ảnh/video đính kèm (tùy chọn)

**Quy trình:**
1. Kiểm tra đơn đã hoàn thành chưa
2. Kiểm tra chưa đánh giá
3. Lưu đánh giá vào database
4. Upload hình ảnh/video (nếu có)
5. Cập nhật điểm sao trung bình của khóa học

#### 6.3. Xem đánh giá khóa học

**Mục đích:** Hiển thị danh sách đánh giá của một khóa học.

**Thông tin hiển thị:**
- Tên học viên
- Điểm sao
- Bình luận
- Hình ảnh/video
- Ngày đánh giá

---

### 7. Module Thông báo

#### 7.1. Xem danh sách thông báo

**Mục đích:** Người dùng xem các thông báo từ hệ thống.

**Phân loại thông báo:**

| Loại | Mô tả |
|------|-------|
| Hệ Thống | Thông báo chung từ hệ thống |
| DatLich | Thông báo đặt lịch thành công |
| NhacNho | Nhắc nhở trước khi lớp học diễn ra |

**Tính năng:**
- Đánh dấu đã đọc
- Đánh dấu tất cả là đã đọc
- Xóa thông báo đã đọc
- Đếm số thông báo chưa đọc

#### 7.2. Thông báo tự động

**Mục đích:** Hệ thống tự động gửi thông báo nhắc nhở.

**Loại thông báo tự động:**
- Đặt lịch thành công: Ngay sau khi thanh toán
- Nhắc nhở trước 1 ngày: Lớp học sắp diễn ra vào ngày mai
- Nhắc nhở trước 30 phút: Lớp học sắp bắt đầu

---

### 8. Module Ưu đãi

#### 8.1. Xem danh sách ưu đãi

**Mục đích:** Hiển thị các mã giảm giá khả dụng cho học viên.

**Thông tin hiển thị:**
- Mã code, tên ưu đãi
- Loại giảm (phần trăm/số tiền)
- Giá trị giảm, giảm tối đa
- Thời hạn sử dụng
- Điều kiện áp dụng (số người tối thiểu)

#### 8.2. Áp dụng mã ưu đãi

**Mục đích:** Cho phép học viên sử dụng mã giảm giá khi đặt lịch.

**Quy trình:**
1. Học viên nhập mã ưu đãi
2. Hệ thống kiểm tra:
   - Mã có tồn tại không?
   - Mã còn hạn không?
   - Mã còn số lượng không?
   - Đủ điều kiện số người không?
3. Tính toán số tiền được giảm
4. Trả về giá sau giảm

#### 8.3. Quản lý ưu đãi (Admin)

**Mục đích:** Admin quản lý danh sách mã giảm giá trong hệ thống.

**Chức năng:**
- Thêm mã ưu đãi mới
- Cập nhật thông tin ưu đãi
- Thay đổi trạng thái (Hoạt Động/Hết Hạn)
- Xóa ưu đãi

---

### 9. Module Yêu thích

#### 9.1. Thêm vào yêu thích

**Mục đích:** Cho phép học viên lưu khóa học để theo dõi.

**Quy trình:**
1. Học viên nhấn nút "Yêu thích" trên khóa học
2. Hệ thống kiểm tra đã yêu thích chưa
3. Thêm vào danh sách yêu thích

#### 9.2. Xóa khỏi yêu thích

**Mục đích:** Xóa khóa học khỏi danh sách yêu thích.

#### 9.3. Xem danh sách yêu thích

**Mục đích:** Hiển thị tất cả khóa học đã lưu.

---

## VIII. GIAO DIỆN NGƯỜI DÙNG

### 1. Tổng quan giao diện

Ứng dụng được thiết kế theo Material Design với giao diện hiện đại, thân thiện người dùng. Cấu trúc navigation chính sử dụng Bottom Navigation Bar kết hợp với Fragments.

### 2. Các màn hình theo vai trò

#### 2.1. Màn hình Xác thực (Chung)

| Màn hình | Mô tả chức năng |
|----------|-----------------|
| Đăng nhập | Form nhập email/password, nút đăng nhập Google, link đăng ký, link quên mật khẩu |
| Đăng ký | Form nhập đầy đủ thông tin: email, mật khẩu, họ tên, SĐT |
| Quên mật khẩu | Nhập email để nhận OTP |
| Xác thực OTP | Nhập 6 số OTP, có nút gửi lại |
| Đặt lại mật khẩu | Nhập mật khẩu mới và xác nhận |

#### 2.2. Màn hình Học viên (Android App)

| Màn hình | Mô tả chức năng |
|----------|-----------------|
| Trang chủ | Banner khóa học nổi bật, ô tìm kiếm theo địa điểm và ngày |
| Kết quả tìm kiếm | Danh sách khóa học theo bộ lọc |
| Chi tiết khóa học | Ảnh, thông tin đầy đủ, danh sách món ăn, lịch trình, nút đặt lịch |
| Đặt lịch | Chọn lịch trình, ngày, số người, nhập thông tin, áp dụng mã giảm giá |
| Thanh toán Momo | Chuyển đến app/web Momo để thanh toán |
| Lịch sử đặt lịch | Danh sách đơn đặt lịch, tab theo trạng thái |
| Chi tiết đơn | Thông tin đơn, khóa học, thanh toán, nút hủy/đánh giá |
| Đánh giá | Form đánh giá sao, bình luận, upload hình ảnh/video |
| Yêu thích | Danh sách khóa học đã lưu |
| Thông báo | Danh sách thông báo, badge số chưa đọc |
| Cá nhân | Avatar, thông tin user, nút cài đặt |
| Chỉnh sửa hồ sơ | Form cập nhật thông tin |
| Đổi mật khẩu | Nhập mật khẩu cũ, mới, xác thực OTP |

#### 2.3. Màn hình Admin (WebAdmin)

| Màn hình | Mô tả chức năng |
|----------|-----------------|
| Dashboard | Thống kê tổng quan, hoạt động gần đây, đơn hàng mới |
| Quản lý người dùng | Danh sách người dùng, CRUD, phân quyền |
| Quản lý giáo viên | Danh sách giáo viên, CRUD, thông tin chi tiết |
| Quản lý khóa học | Danh sách khóa học, CRUD, upload hình ảnh |
| Quản lý lịch trình | Danh sách lịch trình, CRUD, gán giáo viên |
| Quản lý món ăn | Danh sách món ăn, CRUD, gán khóa học & danh mục |
| Quản lý đặt lịch | Danh sách đơn, cập nhật trạng thái |

### 3. Đặc điểm UI/UX

| Tính năng | Mô tả |
|-----------|-------|
| Pull-to-refresh | Kéo xuống để làm mới dữ liệu |
| Loading states | Hiển thị loading khi tải dữ liệu |
| Error handling | Thông báo lỗi thân thiện, nút thử lại |
| Image caching | Glide tự động cache hình ảnh |
| Responsive | Giao diện tương thích nhiều kích thước màn hình |

---

## IX. TỔNG KẾT

### 1. Điểm nổi bật của dự án

| Điểm nổi bật | Mô tả |
|--------------|-------|
| Kiến trúc Clean | Phân tách rõ ràng Backend/Frontend/WebAdmin, dễ bảo trì và mở rộng |
| Bảo mật | Mã hóa mật khẩu, xác thực OTP, Google OAuth |
| Thanh toán tích hợp | Tích hợp Momo Payment API, xử lý callback realtime |
| Multi-platform | Android App cho học viên, WebAdmin cho quản trị |
| Multi-role | Hỗ trợ đầy đủ 3 vai trò với quyền hạn riêng biệt |
| Thông báo tự động | Hệ thống nhắc nhở trước 1 ngày và 30 phút |
| Đánh giá đa phương tiện | Hỗ trợ upload hình ảnh và video đánh giá |

### 2. Thống kê dự án

| Tiêu chí | Số lượng |
|----------|----------|
| Tổng số API endpoints | ~50+ |
| Số bảng database | 18 |
| Số màn hình Android | ~15+ |
| Số trang WebAdmin | 7 |

### 3. Công nghệ sử dụng tóm tắt

| Layer | Công nghệ |
|-------|-----------|
| Backend Framework | Spring Boot 4.0.0 |
| Backend Language | Java 21 |
| Database | SQL Server |
| ORM | Hibernate/JPA |
| Payment | Momo API |
| Authentication | Google OAuth 2.0 |
| Email Service | Spring Mail |
| Mobile Platform | Android (SDK 22-36) |
| Mobile Language | Java 11 |
| HTTP Client | Retrofit 2.9.0 + OkHttp 4.12.0 |
| Image Loading | Glide 4.16.0 |
| Video Player | ExoPlayer (Media3) 1.2.1 |
| UI Framework | Material Design 1.12.0 |
| WebAdmin | HTML5/CSS3/JavaScript |

---

## X. API ENDPOINTS

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

## XI. HƯỚNG DẪN CÀI ĐẶT

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

## XII. DEMO & SCREENSHOTS

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

## XIII. KẾT LUẬN

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
