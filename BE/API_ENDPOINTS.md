# API Endpoints - Đặt Lịch Học Nấu Ăn

## Base URL
```
http://localhost:8080/api
```

## 1. Người Dùng (NguoiDung)
- **GET** `/nguoidung` - Lấy tất cả người dùng
- **GET** `/nguoidung/{id}` - Lấy người dùng theo ID
- **POST** `/nguoidung` - Tạo người dùng mới
- **PUT** `/nguoidung/{id}` - Cập nhật người dùng
- **DELETE** `/nguoidung/{id}` - Xóa người dùng
- **POST** `/nguoidung/login` - Đăng nhập
- **POST** `/nguoidung/register` - Đăng ký
- **POST** `/nguoidung/change-password/send-otp` - Gửi OTP để đổi mật khẩu
- **POST** `/nguoidung/change-password/verify` - Xác thực OTP và đổi mật khẩu
- **POST** `/nguoidung/login` - Đăng nhập
- **POST** `/nguoidung/register` - Đăng ký tài khoản mới

## 2. OTP (Xác thực)
- **POST** `/otp/send` - Gửi mã OTP đến email
- **POST** `/otp/verify` - Xác thực mã OTP

## 3. Quên Mật Khẩu (PasswordReset)
- **POST** `/quenmatkhau/forgot-password` - Gửi OTP quên mật khẩu
- **POST** `/quenmatkhau/verify-reset-otp` - Xác thực OTP và nhận reset token
- **POST** `/quenmatkhau/reset-password` - Đặt mật khẩu mới

## 4. Giáo Viên (GiaoVien)
- **GET** `/giaovien` - Lấy tất cả giáo viên
- **GET** `/giaovien/{id}` - Lấy giáo viên theo ID
- **POST** `/giaovien` - Tạo giáo viên mới
- **PUT** `/giaovien/{id}` - Cập nhật giáo viên
- **DELETE** `/giaovien/{id}` - Xóa giáo viên

## 5. Khóa Học (KhoaHoc)
- **GET** `/khoahoc` - Lấy tất cả khóa học (trả về KhoaHocDTO)
- **GET** `/khoahoc/{id}` - Lấy khóa học theo ID (trả về KhoaHocDTO)
- **GET** `/khoahoc/search?diaDiem={diaDiem}&ngayTimKiem={ngayTimKiem}` - Tìm kiếm khóa học theo địa điểm và ngày (trả về KhoaHocDTO)
    - `diaDiem` (required): Tên thành phố hoặc địa điểm (ví dụ: "Hà Nội", "Đà Nẵng", "Huế", "Cần Thơ")
    - `ngayTimKiem` (optional): Ngày tìm kiếm theo định dạng YYYY-MM-DD (ví dụ: "2025-01-15")
    - **Lưu ý:** API tự động tính toán `phanTramGiam` và `giaSauGiam` cho các khóa học có `coUuDai = true`
- **POST** `/khoahoc` - Tạo khóa học mới
- **PUT** `/khoahoc/{id}` - Cập nhật khóa học
- **DELETE** `/khoahoc/{id}` - Xóa khóa học

## 5.1. Lớp Học (LopHoc)
- **GET** `/lophoc` - Lấy tất cả lớp học (trả về LopHocDTO)
- **GET** `/lophoc/{id}` - Lấy lớp học theo ID (trả về LopHocDTO)
- **GET** `/lophoc/search?diaDiem={diaDiem}&ngayTimKiem={ngayTimKiem}` - Tìm kiếm lớp học theo địa điểm và ngày (trả về LopHocDTO)
    - `diaDiem` (required): Tên thành phố hoặc địa điểm (ví dụ: "Hà Nội", "Đà Nẵng", "Huế", "Cần Thơ")
    - `ngayTimKiem` (optional): Ngày tìm kiếm theo định dạng YYYY-MM-DD (ví dụ: "2025-01-15")
    - Nếu không có `ngayTimKiem`, API sẽ trả về tất cả lớp học còn hiệu lực tại địa điểm đó
    - Nếu có `ngayTimKiem`, API sẽ kiểm tra lịch trình lặp lại (HangNgay, HangTuan, MotLan) để trả về các lớp học diễn ra vào ngày đó
- **POST** `/lophoc` - Tạo lớp học mới
- **PUT** `/lophoc/{id}` - Cập nhật lớp học
- **DELETE** `/lophoc/{id}` - Xóa lớp học

## 6. Lịch Học (LichHoc)
- **GET** `/lichhoc` - Lấy tất cả lịch học
- **GET** `/lichhoc/{id}` - Lấy lịch học theo ID
- **POST** `/lichhoc` - Tạo lịch học mới
- **PUT** `/lichhoc/{id}` - Cập nhật lịch học
- **DELETE** `/lichhoc/{id}` - Xóa lịch học

## 6.1. Lịch Trình Lớp Học (LichTrinhLopHoc)
- **GET** `/lichtrinh` - Lấy tất cả lịch trình
- **GET** `/lichtrinh/{id}` - Lấy lịch trình theo ID
- **GET** `/lichtrinh/khoahoc/{maKhoaHoc}` - Lấy lịch trình theo khóa học
- **GET** `/lichtrinh/giaovien/{maGiaoVien}` - Lấy lịch trình theo giáo viên
- **GET** `/lichtrinh/diadiem?diaDiem={diaDiem}` - Lấy lịch trình theo địa điểm
- **GET** `/lichtrinh/active` - Lấy lịch trình đang hoạt động
- **GET** `/lichtrinh/check-seats?maLichTrinh={maLichTrinh}&ngayThamGia={ngayThamGia}` - Kiểm tra chỗ trống
- **POST** `/lichtrinh` - Tạo lịch trình mới
- **PUT** `/lichtrinh/{id}` - Cập nhật lịch trình
- **DELETE** `/lichtrinh/{id}` - Xóa lịch trình

## 7. Danh Mục Món Ăn (DanhMucMonAn)
- **GET** `/danhmucmonan` - Lấy tất cả danh mục món ăn
- **GET** `/danhmucmonan/{id}` - Lấy danh mục món ăn theo ID
- **GET** `/danhmucmonan/lophoc/{maLopHoc}` - Lấy danh mục món ăn kèm món ăn theo lớp học (trả về DanhMucMonAnDTO)
- **POST** `/danhmucmonan` - Tạo danh mục món ăn mới
- **PUT** `/danhmucmonan/{id}` - Cập nhật danh mục món ăn
- **DELETE** `/danhmucmonan/{id}` - Xóa danh mục món ăn

## 8. Món Ăn (MonAn)
- **GET** `/monan` - Lấy tất cả món ăn
- **GET** `/monan/{id}` - Lấy món ăn theo ID
- **GET** `/monan/lophoc/{maLopHoc}` - Lấy món ăn theo lớp học
- **GET** `/monan/danhmuc/{maDanhMuc}` - Lấy món ăn theo danh mục
- **POST** `/monan` - Tạo món ăn mới
- **PUT** `/monan/{id}` - Cập nhật món ăn
- **DELETE** `/monan/{id}` - Xóa món ăn

## 9. Hình Ảnh Món Ăn (HinhAnhMonAn)
- **GET** `/hinhanh-monan/monan/{maMonAn}` - Lấy tất cả hình ảnh của món ăn (slideshow)
- **POST** `/hinhanh-monan` - Tạo hình ảnh món ăn mới
- **DELETE** `/hinhanh-monan/{maHinhAnh}` - Xóa hình ảnh món ăn

## 10. Hình Ảnh Lớp Học (HinhAnhLopHoc)
- **GET** `/hinhanhlophoc` - Lấy tất cả hình ảnh lớp học
- **GET** `/hinhanhlophoc/{id}` - Lấy hình ảnh lớp học theo ID
- **POST** `/hinhanhlophoc` - Tạo hình ảnh lớp học mới
- **PUT** `/hinhanhlophoc/{id}` - Cập nhật hình ảnh lớp học
- **DELETE** `/hinhanhlophoc/{id}` - Xóa hình ảnh lớp học

## 11. Đặt Lịch (DatLich)

### Luồng đặt lịch mới:
1. User tìm kiếm khóa học theo địa điểm và ngày: `GET /api/khoahoc/search`
2. User chọn khóa học và nhấn "Đặt lịch"
3. Lấy lịch trình của khóa học: `GET /api/lichtrinh/khoahoc/{maKhoaHoc}`
4. User chọn lịch trình (thứ, giờ học)
5. Kiểm tra chỗ trống: `GET /api/lichtrinh/check-seats`
6. User điều chỉnh số người và xác nhận
7. Tạo đặt lịch: `POST /api/datlich`

### Endpoints:
- **GET** `/datlich` - Lấy tất cả đặt lịch
- **GET** `/datlich/{id}` - Lấy đặt lịch theo ID
- **GET** `/datlich/hocvien/{maHocVien}` - Lấy đặt lịch theo học viên
- **GET** `/datlich/hocvien/{maHocVien}/trangthai/{trangThai}` - Lấy đặt lịch theo học viên và trạng thái
- **POST** `/datlich` - Tạo đặt lịch mới
- **PUT** `/datlich/{id}` - Cập nhật đặt lịch
- **DELETE** `/datlich/{id}` - Xóa đặt lịch

## 12. Thanh Toán (ThanhToan)
- **GET** `/thanhtoan` - Lấy tất cả thanh toán
- **GET** `/thanhtoan/{id}` - Lấy thanh toán theo ID
- **POST** `/thanhtoan` - Tạo thanh toán mới
- **PUT** `/thanhtoan/{id}` - Cập nhật thanh toán
- **DELETE** `/thanhtoan/{id}` - Xóa thanh toán

## 13. Đánh Giá (DanhGia)

### Luồng đánh giá:
1. Khi xem lịch sử đặt lịch, gọi API kiểm tra trạng thái đánh giá: `GET /api/danhgia/kiemtra/{maDatLich}`
2. Nếu `trangThaiDanhGia = "CÓ THỂ ĐÁNH GIÁ"` → Hiển thị nút "Đánh giá"
3. Nếu `trangThaiDanhGia = "ĐÃ ĐÁNH GIÁ"` → Hiển thị nút "Xem đánh giá"
4. Nếu `trangThaiDanhGia = "KHÔNG THỂ ĐÁNH GIÁ"` → Ẩn nút đánh giá (đơn chưa hoàn thành)

### Endpoints:
- **GET** `/danhgia` - Lấy tất cả đánh giá
- **GET** `/danhgia/{id}` - Lấy đánh giá theo ID
- **GET** `/danhgia/kiemtra/{maDatLich}` - Kiểm tra trạng thái đánh giá của đơn đặt lịch
- **GET** `/danhgia/datlich/{maDatLich}` - Lấy đánh giá theo mã đặt lịch
- **GET** `/danhgia/khoahoc/{maKhoaHoc}` - Lấy danh sách đánh giá theo khóa học
- **POST** `/danhgia/tao` - Tạo đánh giá mới (kèm hình ảnh/video)
- **DELETE** `/danhgia/{id}` - Xóa đánh giá

## 14. Thông Báo (ThongBao)
- **GET** `/thongbao` - Lấy tất cả thông báo (trả về ThongBaoDTO)
- **GET** `/thongbao/{id}` - Lấy thông báo theo ID (trả về ThongBaoDTO)
- **GET** `/thongbao/user/{maNguoiNhan}` - Lấy tất cả thông báo của người dùng
- **GET** `/thongbao/user/{maNguoiNhan}/unread` - Lấy thông báo chưa đọc của người dùng
- **GET** `/thongbao/user/{maNguoiNhan}/unread-count` - Đếm số thông báo chưa đọc
- **GET** `/thongbao/user/{maNguoiNhan}/type/{loaiThongBao}` - Lấy thông báo theo loại
- **POST** `/thongbao` - Tạo thông báo mới
- **PUT** `/thongbao/{id}` - Cập nhật thông báo
- **PUT** `/thongbao/{id}/mark-read` - Đánh dấu thông báo đã đọc
- **PUT** `/thongbao/user/{maNguoiNhan}/mark-all-read` - Đánh dấu tất cả đã đọc
- **DELETE** `/thongbao/{id}` - Xóa thông báo
- **DELETE** `/thongbao/user/{maNguoiNhan}/delete-read` - Xóa tất cả thông báo đã đọc

## 15. Ưu Đãi (UuDai)
- **GET** `/uudai` - Lấy tất cả ưu đãi (trả về UuDaiDTO)
- **GET** `/uudai/{id}` - Lấy ưu đãi theo ID (trả về UuDaiDTO)
- **GET** `/uudai/available?maHocVien={maHocVien}&soLuongNguoi={soLuongNguoi}` - Lấy danh sách ưu đãi khả dụng cho user
- **POST** `/uudai` - Tạo ưu đãi mới
- **POST** `/uudai/apply` - Áp dụng mã ưu đãi và tính toán giảm giá
- **POST** `/uudai/confirm/{maUuDai}` - Xác nhận sử dụng mã ưu đãi (gọi sau khi thanh toán thành công)
- **PUT** `/uudai/{id}` - Cập nhật ưu đãi
- **DELETE** `/uudai/{id}` - Xóa ưu đãi

---

## Ví dụ Request Body

### 1. Đăng Ký (Register)
```json
{
  "tenDangNhap": "user123",
  "matKhau": "password123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "soDienThoai": "0123456789"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đăng ký thành công",
  "maNguoiDung": 7,
  "tenDangNhap": "user123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "vaiTro": "HocVien"
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Email đã được sử dụng",
  "maNguoiDung": null,
  "tenDangNhap": null,
  "hoTen": null,
  "email": null,
  "vaiTro": null
}
```

### 2. Đăng Nhập (Login)
```json
{
  "email": "admin@localcooking.vn",
  "matKhau": "admin123"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "maNguoiDung": 1,
  "tenDangNhap": "admin",
  "hoTen": "Quản Trị Viên",
  "email": "admin@localcooking.vn",
  "vaiTro": "Admin"
}
```

### 3. Gửi OTP
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "OTP đã được gửi đến user@example.com"
}
```

### 4. Xác Thực OTP
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "valid": true,
  "message": "Xác thực thành công"
}
```

### 5. Quên Mật Khẩu - Gửi OTP
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "OTP đã được gửi đến user@example.com"
}
```

### 6. Quên Mật Khẩu - Xác Thực OTP
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "resetToken": "abc123xyz789",
  "message": "Xác thực thành công"
}
```

### 7. Quên Mật Khẩu - Đặt Mật Khẩu Mới
```json
{
  "resetToken": "abc123xyz789",
  "newPassword": "newpassword123"
}
```

**Response:**
```json
{
  "message": "Đổi mật khẩu thành công"
}
```

### 8. Tạo Người Dùng
```json
{
  "tenDangNhap": "user123",
  "matKhau": "password123",
  "hoTen": "Nguyễn Văn A",
  "email": "user@example.com",
  "soDienThoai": "0123456789",
  "diaChi": "123 Đường ABC",
  "vaiTro": "HocVien"
}
```


### 9. Tạo Lớp Học

### Đổi Mật Khẩu - Bước 1: Gửi OTP
**POST** `/api/nguoidung/change-password/send-otp`
```json
{
  "email": "user@example.com",
  "matKhauHienTai": "password123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Mã OTP đã được gửi đến email user@example.com",
  "email": "user@example.com"
}
```

### Đổi Mật Khẩu - Bước 2: Xác Thực OTP và Đổi Mật Khẩu
**POST** `/api/nguoidung/change-password/verify`
```json
{
  "email": "user@example.com",
  "matKhauHienTai": "password123",
  "matKhauMoi": "newpassword456",
  "xacNhanMatKhauMoi": "newpassword456",
  "otp": "123456"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đổi mật khẩu thành công"
}
```

### Tạo Lớp Học
>>>>>>> d4b8036b042f315e9adadda293a16c6107cb0a14
```json
{
  "tenLopHoc": "Nấu ăn Việt Nam cơ bản",
  "moTa": "Học nấu các món ăn Việt Nam truyền thống",
  "gioiThieu": "Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội",
  "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc\n• Hiểu về văn hóa ẩm thực phố cổ\n• Tự tin làm bún chả và chả cá Lã Vọng",
  "maGiaoVien": 1,
  "soLuongToiDa": 20,
  "giaTien": 500000,
  "thoiGian": "14:00 - 17:00",
  "diaDiem": "123 Nguyễn Huệ, Q1",
  "ngayDienRa": "2024-01-15",
  "gioBatDau": "14:00:00",
  "gioKetThuc": "17:00:00"
}
```

### 10. Tạo Món Ăn
```json
{
  "maLopHoc": 1,
  "maDanhMuc": 2,
  "tenMon": "Phở bò Hà Nội",
  "gioiThieu": "Món phở truyền thống với nước dùng trong, thơm, thịt bò mềm",
  "nguyenLieu": "Bánh phở, thịt bò, xương bò, hành, gừng, gia vị"
}
```

### 11. Tạo Hình Ảnh Món Ăn
```json
{
  "maMonAn": 1,
  "duongDan": "phobo_1.png",
  "thuTu": 1
}
```

### 12. Tạo Đặt Lịch
```json
{
  "maHocVien": 1,
  "maLopHoc": 1,
  "soLuongNguoi": 1,
  "tongTien": 500000,
  "tenNguoiDat": "Nguyễn Văn A",
  "emailNguoiDat": "user@example.com",
  "sdtNguoiDat": "0123456789",
  "ghiChu": "Không có"
}
```

### 13. Kiểm tra trạng thái đánh giá
**GET** `/api/danhgia/kiemtra/{maDatLich}`

**Response khi chưa đánh giá (đơn đã hoàn thành):**
```json
{
  "maDatLich": 1,
  "trangThaiDon": "Đã hoàn thành",
  "daDanhGia": false,
  "maDanhGia": null,
  "trangThaiDanhGia": "CÓ THỂ ĐÁNH GIÁ",
  "danhGia": null
}
```

**Response khi đã đánh giá:**
```json
{
  "maDatLich": 1,
  "trangThaiDon": "Đã hoàn thành",
  "daDanhGia": true,
  "maDanhGia": 5,
  "trangThaiDanhGia": "ĐÃ ĐÁNH GIÁ",
  "danhGia": {
    "maDanhGia": 5,
    "maHocVien": 4,
    "tenHocVien": "Ngô Thị Thảo Vy",
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "maDatLich": 1,
    "diemDanhGia": 5,
    "binhLuan": "Lớp học rất tuyệt vời!",
    "ngayDanhGia": "22/12/2024 15:30",
    "hinhAnhList": [
      {
        "maHinhAnh": 1,
        "maDanhGia": 5,
        "duongDan": "https://example.com/image1.jpg",
        "loaiFile": "image",
        "thuTu": 1
      }
    ]
  }
}
```

**Response khi đơn chưa hoàn thành:**
```json
{
  "maDatLich": 2,
  "trangThaiDon": "Đặt trước",
  "daDanhGia": false,
  "maDanhGia": null,
  "trangThaiDanhGia": "KHÔNG THỂ ĐÁNH GIÁ",
  "danhGia": null
}
```

### 14. Tạo đánh giá mới
**POST** `/api/danhgia/tao`
```json
{
  "maDatLich": 1,
  "diemDanhGia": 5,
  "binhLuan": "Không gian lớp thoải mái, đầy đủ dụng cụ và nguyên liệu. Giáo viên hướng dẫn chi tiết, luôn hỗ trợ khi gặp khó khăn.",
  "hinhAnhUrls": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/video1.mp4"
  ]
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đánh giá thành công",
  "data": {
    "maDanhGia": 5,
    "maHocVien": 4,
    "tenHocVien": "Ngô Thị Thảo Vy",
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "maDatLich": 1,
    "diemDanhGia": 5,
    "binhLuan": "Không gian lớp thoải mái...",
    "ngayDanhGia": "22/12/2024 15:30",
    "hinhAnhList": [...]
  }
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Chỉ được đánh giá khi đơn đã hoàn thành"
}
```

### 17. Tìm kiếm khóa học theo địa điểm
**Ví dụ 1: Tìm tất cả khóa học ở Hà Nội**
```
GET /api/khoahoc/search?diaDiem=Hà Nội
```

**Ví dụ 2: Tìm khóa học ở Đà Nẵng vào ngày 15/01/2025**
```
GET /api/khoahoc/search?diaDiem=Đà Nẵng&ngayTimKiem=2025-01-15
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
    "soLuongDanhGia": 15,
    "saoTrungBinh": 4.5,
    "coUuDai": true,
    "phanTramGiam": 10.0,
    "giaSauGiam": 585000,
    "lichTrinhList": [
      {
        "maLichTrinh": 1,
        "maKhoaHoc": 1,
        "maGiaoVien": 1,
        "thuTrongTuan": "Thứ 2, Thứ 4, Thứ 6",
        "gioBatDau": "09:00:00",
        "gioKetThuc": "12:00:00",
        "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
        "soLuongToiDa": 20,
        "soLuongHienTai": 5,
        "conTrong": 15,
        "trangThai": true,
        "trangThaiHienThi": "Còn chỗ"
      }
    ],
    "danhMucMonAnList": [
      {
        "maDanhMuc": 1,
        "tenDanhMuc": "Khai vị",
        "monAnList": [...]
      }
    ]
  }
]
```

**Lưu ý về tính toán ưu đãi:**
- Khi `coUuDai = true`, backend tự động tính toán:
  - `phanTramGiam`: Cố định 10%
  - `giaSauGiam`: Giá sau khi giảm 10%
- Công thức: `giaSauGiam = giaTien - (giaTien * 10%)`

### 18. Tìm kiếm lớp học theo địa điểm
**Ví dụ 1: Tìm tất cả lớp học ở Hà Nội (còn hiệu lực)**
```
GET /api/lophoc/search?diaDiem=Hà Nội
```

**Ví dụ 2: Tìm lớp học ở Đà Nẵng vào ngày 15/01/2025**
```
GET /api/lophoc/search?diaDiem=Đà Nẵng&ngayTimKiem=2025-01-15
```

**Ví dụ 3: Tìm lớp học ở Huế vào Chủ nhật (ngày 19/01/2025)**
```
GET /api/lophoc/search?diaDiem=Huế&ngayTimKiem=2025-01-19
```

**Response:**
```json
[
  {
    "maLopHoc": 1,
    "tenLopHoc": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng của ẩm thực phố cổ",
    "gioiThieu": "Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội",
    "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc...",
    "tenGiaoVien": "Nguyễn Văn An",
    "soLuongToiDa": 20,
    "soLuongHienTai": 0,
    "giaTien": 650000,
    "thoiGian": "09:00 - 12:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "trangThai": "Đang mở",
    "loaiLich": "HangNgay",
    "ngayBatDau": "2025-01-01",
    "ngayKetThuc": "2025-12-31",
    "gioBatDau": "09:00:00",
    "gioKetThuc": "12:00:00",
    "hinhAnh": "phobo.png",
    "soLuongDanhGia": 0,
    "saoTrungBinh": 0.0,
    "coUuDai": true,
    "lichTrinhLopHoc": [...]
  }
]
```

## Lưu ý
- Tất cả endpoints đều hỗ trợ CORS với `origins = "*"`
- Response thành công: HTTP 200 (GET, PUT, DELETE) hoặc 201 (POST)
- Response lỗi: HTTP 404 (Not Found) hoặc 400 (Bad Request)
- Tìm kiếm địa điểm không phân biệt hoa thường và tìm kiếm theo chuỗi con (ví dụ: "Hà Nội", "hà nội", "Nội" đều cho kết quả giống nhau)
