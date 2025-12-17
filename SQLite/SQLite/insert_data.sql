-- =======================
-- DỮ LIỆU MẪU – ĐẶT LỊCH HỌC NẤU ĂN
-- =======================

-- ===== NGƯỜI DÙNG =====
INSERT INTO NguoiDung (tenDangNhap, matKhau, hoTen, email, soDienThoai, vaiTro)
VALUES
('giaovien_anna', '123456', 'Nguyễn Thị Anna', 'anna.teacher@gmail.com', '0905000001', 'GiaoVien'),
('giaovien_hung', '123456', 'Trần Văn Hùng', 'hung.teacher@gmail.com', '0905000002', 'GiaoVien'),
('hocvien_tan', '123456', 'Lê Văn Tân', 'tan.student@gmail.com', '0905000003', 'HocVien'),
('hocvien_mai', '123456', 'Trần Thị Mai', 'mai.student@gmail.com', '0905000004', 'HocVien'),
('admin01', '123456', 'Quản Trị Viên', 'admin@gmail.com', '0905000009', 'Admin');

-- ===== GIÁO VIÊN =====
INSERT INTO GiaoVien (maNguoiDung, chuyenMon, kinhNghiem, moTa, hinhAnh)
VALUES
(1, 'Ẩm thực Việt Nam truyền thống', '15 năm kinh nghiệm', 
 'Chuyên gia ẩm thực Việt Nam với hơn 15 năm kinh nghiệm giảng dạy và nghiên cứu các món ăn truyền thống.', 
 'avt_teacher_anna.jpg'),
(2, 'Ẩm thực Âu', '10 năm kinh nghiệm', 
 'Đầu bếp chuyên về món Âu, từng làm việc tại nhiều nhà hàng 5 sao.', 
 'avt_teacher_hung.jpg');

-- ===== LỚP HỌC =====
INSERT INTO LopHoc (
  maGiaoVien, tenGiaoVien, tenLopHoc, moTa, giaTien,
  soLuongToiDa, thoiGian, diaDiem, trangThai,
  ngayDienRa, gioBatDau, gioKetThuc, hinhAnh, coUuDai
)
VALUES
(1, 'Nguyễn Thị Anna', 'Ẩm thực địa phương Huế',
 'Khám phá hương vị đặc trưng của ẩm thực cố đô Huế - nơi giao thoa giữa tinh hoa cung đình và phong vị dân gian.',
 715000, 20, '14:00 - 17:00', '23 Lê Duẩn - Đà Nẵng', 'Sắp diễn ra',
 '2025-02-10', '14:00', '17:00', 'hue.jpg', 1),

(1, 'Nguyễn Thị Anna', 'Học Nấu Món Việt Cơ Bản',
 'Hướng dẫn nấu các món ăn gia đình Việt Nam truyền thống',
 500000, 20, 'T2-4-6 18:00', '123 Lê Duẩn, Đà Nẵng', 'Đang diễn ra',
 '2025-01-15', '18:00', '20:00', 'comtam.png', 0),

(2, 'Trần Văn Hùng', 'Bếp Âu Căn Bản',
 'Làm quen các món Âu phổ biến: pasta, steak, soup',
 800000, 10, 'T7-CN 09:00', '88 Trần Phú, Đà Nẵng', 'Sắp diễn ra',
 '2025-02-01', '09:00', '11:30', 'phobo.png', 0);

-- ===== DANH MỤC MÓN ĂN =====
INSERT INTO DanhMucMonAn (maLopHoc, tenDanhMuc, thoiGian, thuTu)
VALUES
(1, 'Món khai vị', '14:00 - 15:00', 1),
(1, 'Món chính', '15:00 - 16:30', 2),
(1, 'Món tráng miệng', '16:30 - 17:00', 3);

-- ===== MÓN ĂN =====
INSERT INTO MonAn (maDanhMuc, tenMon, gioiThieu, nguyenLieu, hinhAnh)
VALUES
(1, 'Bánh bèo', 'Món khai vị truyền thống của Huế', 'Bột gạo, tôm khô, mỡ hành, nước mắm', 'banhbeo.jpg'),
(2, 'Bún bò Huế', 'Món chính đặc trưng', 'Bún, thịt bò, giò heo, sả, mắm ruốc', 'bunbo.jpg'),
(3, 'Chè Huế', 'Món tráng miệng ngọt mát', 'Đậu xanh, đậu đỏ, thạch, nước cốt dừa', 'chehue.jpg');

-- ===== HÌNH ẢNH LỚP HỌC =====
INSERT INTO HinhAnhLopHoc (maLopHoc, duongDan, thuTu)
VALUES
(1, 'hue_1.jpg', 1),
(1, 'hue_2.jpg', 2),
(1, 'hue_3.jpg', 3);

-- ===== LỊCH HỌC =====
INSERT INTO LichHoc (maLopHoc, ngayHoc, gioBatDau, gioKetThuc, noiDung)
VALUES
(1, '2025-02-10', '14:00', '17:00', 'Học nấu bánh bèo và bún bò Huế'),
(2, '2025-01-15', '18:00', '20:00', 'Canh chua – Cá kho tộ'),
(2, '2025-01-17', '18:00', '20:00', 'Thịt kho trứng – Rau xào');

-- ===== ĐẶT LỊCH =====
INSERT INTO DatLich (maHocVien, maLopHoc, soLuongNguoi, tongTien, tenNguoiDat, emailNguoiDat, sdtNguoiDat, ghiChu, trangThai)
VALUES
(3, 1, 1, 715000, 'Lê Văn Tân', 'tan.student@gmail.com', '0905000003', 'Muốn học nấu món Huế', 'Đã Duyệt'),
(4, 2, 2, 1000000, 'Trần Thị Mai', 'mai.student@gmail.com', '0905000004', 'Đặt cho 2 người', 'Đã Duyệt'),
(3, 3, 1, 800000, 'Lê Văn Tân', 'tan.student@gmail.com', '0905000003', 'Muốn học món Âu', 'Chờ Duyệt');

-- ===== THANH TOÁN =====
INSERT INTO ThanhToan (maDatLich, soTien, phuongThuc, trangThai, ngayThanhToan, maGiaoDich)
VALUES
(1, 715000, 'MoMo', 'Đã Thanh Toán', '2025-01-20 10:30:00', 'MM123456'),
(2, 1000000, 'ChuyenKhoan', 'Đã Thanh Toán', '2025-01-21 14:00:00', 'BANK7890');

-- ===== ĐÁNH GIÁ =====
INSERT INTO DanhGia (maHocVien, maLopHoc, diemDanhGia, binhLuan)
VALUES
(3, 1, 5, 'Giảng viên dạy rất dễ hiểu, món ăn ngon'),
(4, 2, 4, 'Lớp học vui, hơi đông nhưng ổn');

-- ===== YÊU THÍCH =====
INSERT INTO YeuThich (maHocVien, maLopHoc)
VALUES
(3, 1),
(4, 2),
(3, 3);

-- ===== ƯU ĐÃI =====
INSERT INTO UuDai (
  maCode, tenUuDai, moTa,
  loaiGiam, giaTriGiam, giamToiDa,
  soLuong, ngayBatDau, ngayKetThuc, hinhAnh
)
VALUES
('NAUAN10', 'Giảm 10%', 'Giảm 10% học phí cho học viên mới',
 'PhanTram', 10, 100000,
 50, '2025-01-01', '2025-12-31', 'voucher_1.png'),

('GIAM50K', 'Giảm 50K', 'Giảm trực tiếp 50.000đ',
 'SoTien', 50000, NULL,
 30, '2025-01-15', '2025-03-31', 'voucher_3.png'),

('HUE21', 'Giảm 21% lớp Huế', 'Giảm 21% cho lớp ẩm thực Huế',
 'PhanTram', 21, 200000,
 20, '2025-01-01', '2025-02-28', 'voucher_4.png');

-- ===== LỊCH SỬ ƯU ĐÃI =====
INSERT INTO LichSuUuDai (maUuDai, maDatLich, soTienGiam)
VALUES
(1, 1, 71500);

-- ===== THÔNG BÁO =====
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
VALUES
(3, 'Thanh toán thành công', 'Bạn đã thanh toán lớp Ẩm thực địa phương Huế', 'ThanhToan', 'ic_coin.png'),
(4, 'Lớp sắp khai giảng', 'Lớp Bếp Âu Căn Bản sẽ bắt đầu vào tuần tới', 'LopHoc', 'ic_calendar.png'),
(3, 'Đặt lịch thành công', 'Bạn đã đặt lịch học lớp Ẩm thực địa phương Huế', 'LopHoc', NULL);

-- ===== HÓA ĐƠN =====
INSERT INTO HoaDon (maThanhToan, soHoaDon, tongTien, VAT, thanhTien, filePDF)
VALUES
(1, 'HD2025010001', 715000, 0, 715000, 'hoadon_001.pdf'),
(2, 'HD2025010002', 1000000, 0, 1000000, 'hoadon_002.pdf');

-- ===== OTP (Ví dụ) =====
INSERT INTO OTP (maNguoiDung, maXacThuc, loaiOTP, daSuDung)
VALUES
(3, '123456', 'DangKy', 1),
(4, '789012', 'XacThuc', 0);
