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
- **POST** `/nguoidung/register` - Đăng ký tài khoản mới

## 2. Giáo Viên (GiaoVien)
- **GET** `/giaovien` - Lấy tất cả giáo viên
- **GET** `/giaovien/{id}` - Lấy giáo viên theo ID
- **POST** `/giaovien` - Tạo giáo viên mới
- **PUT** `/giaovien/{id}` - Cập nhật giáo viên
- **DELETE** `/giaovien/{id}` - Xóa giáo viên

## 3. Lớp Học (LopHoc)
- **GET** `/lophoc` - Lấy tất cả lớp học
- **GET** `/lophoc/{id}` - Lấy lớp học theo ID
- **POST** `/lophoc` - Tạo lớp học mới
- **PUT** `/lophoc/{id}` - Cập nhật lớp học
- **DELETE** `/lophoc/{id}` - Xóa lớp học

## 4. Lịch Học (LichHoc)
- **GET** `/lichhoc` - Lấy tất cả lịch học
- **GET** `/lichhoc/{id}` - Lấy lịch học theo ID
- **POST** `/lichhoc` - Tạo lịch học mới
- **PUT** `/lichhoc/{id}` - Cập nhật lịch học
- **DELETE** `/lichhoc/{id}` - Xóa lịch học

## 5. Danh Mục Món Ăn (DanhMucMonAn)
- **GET** `/danhmucmonan` - Lấy tất cả danh mục món ăn
- **GET** `/danhmucmonan/{id}` - Lấy danh mục món ăn theo ID
- **POST** `/danhmucmonan` - Tạo danh mục món ăn mới
- **PUT** `/danhmucmonan/{id}` - Cập nhật danh mục món ăn
- **DELETE** `/danhmucmonan/{id}` - Xóa danh mục món ăn

## 6. Món Ăn (MonAn)
- **GET** `/monan` - Lấy tất cả món ăn
- **GET** `/monan/{id}` - Lấy món ăn theo ID
- **GET** `/monan/lophoc/{maLopHoc}` - Lấy món ăn theo lớp học
- **GET** `/monan/danhmuc/{maDanhMuc}` - Lấy món ăn theo danh mục
- **POST** `/monan` - Tạo món ăn mới
- **PUT** `/monan/{id}` - Cập nhật món ăn
- **DELETE** `/monan/{id}` - Xóa món ăn

## 6.1. Hình Ảnh Món Ăn (HinhAnhMonAn)
- **GET** `/hinhanh-monan/monan/{maMonAn}` - Lấy tất cả hình ảnh của món ăn (slideshow)
- **POST** `/hinhanh-monan` - Tạo hình ảnh món ăn mới
- **DELETE** `/hinhanh-monan/{maHinhAnh}` - Xóa hình ảnh món ăn

## 7. Hình Ảnh Lớp Học (HinhAnhLopHoc)
- **GET** `/hinhanhlophoc` - Lấy tất cả hình ảnh lớp học
- **GET** `/hinhanhlophoc/{id}` - Lấy hình ảnh lớp học theo ID
- **POST** `/hinhanhlophoc` - Tạo hình ảnh lớp học mới
- **PUT** `/hinhanhlophoc/{id}` - Cập nhật hình ảnh lớp học
- **DELETE** `/hinhanhlophoc/{id}` - Xóa hình ảnh lớp học

## 8. Đặt Lịch (DatLich)
- **GET** `/datlich` - Lấy tất cả đặt lịch
- **GET** `/datlich/{id}` - Lấy đặt lịch theo ID
- **POST** `/datlich` - Tạo đặt lịch mới
- **PUT** `/datlich/{id}` - Cập nhật đặt lịch
- **DELETE** `/datlich/{id}` - Xóa đặt lịch

## 9. Thanh Toán (ThanhToan)
- **GET** `/thanhtoan` - Lấy tất cả thanh toán
- **GET** `/thanhtoan/{id}` - Lấy thanh toán theo ID
- **POST** `/thanhtoan` - Tạo thanh toán mới
- **PUT** `/thanhtoan/{id}` - Cập nhật thanh toán
- **DELETE** `/thanhtoan/{id}` - Xóa thanh toán

## 10. Đánh Giá (DanhGia)
- **GET** `/danhgia` - Lấy tất cả đánh giá
- **GET** `/danhgia/{id}` - Lấy đánh giá theo ID
- **POST** `/danhgia` - Tạo đánh giá mới
- **PUT** `/danhgia/{id}` - Cập nhật đánh giá
- **DELETE** `/danhgia/{id}` - Xóa đánh giá

## 11. Thông Báo (ThongBao)
- **GET** `/thongbao` - Lấy tất cả thông báo
- **GET** `/thongbao/{id}` - Lấy thông báo theo ID
- **POST** `/thongbao` - Tạo thông báo mới
- **PUT** `/thongbao/{id}` - Cập nhật thông báo
- **DELETE** `/thongbao/{id}` - Xóa thông báo

## 12. Ưu Đãi (UuDai)
- **GET** `/uudai` - Lấy tất cả ưu đãi
- **GET** `/uudai/{id}` - Lấy ưu đãi theo ID
- **POST** `/uudai` - Tạo ưu đãi mới
- **PUT** `/uudai/{id}` - Cập nhật ưu đãi
- **DELETE** `/uudai/{id}` - Xóa ưu đãi

## Ví dụ Request Body

### Đăng Ký (Register)
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

### Đăng Nhập (Login)
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

### Tạo Người Dùng
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

### Tạo Lớp Học
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

### Tạo Món Ăn
```json
{
  "maLopHoc": 1,
  "maDanhMuc": 2,
  "tenMon": "Phở bò Hà Nội",
  "gioiThieu": "Món phở truyền thống với nước dùng trong, thơm, thịt bò mềm",
  "nguyenLieu": "Bánh phở, thịt bò, xương bò, hành, gừng, gia vị"
}
```

### Tạo Hình Ảnh Món Ăn
```json
{
  "maMonAn": 1,
  "duongDan": "phobo_1.png",
  "thuTu": 1
}
```

### Tạo Đặt Lịch
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

## Lưu ý
- Tất cả endpoints đều hỗ trợ CORS với `origins = "*"`
- Response thành công: HTTP 200 (GET, PUT, DELETE) hoặc 201 (POST)
- Response lỗi: HTTP 404 (Not Found) hoặc 400 (Bad Request)
