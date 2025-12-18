IF EXISTS (SELECT * FROM sys.databases WHERE name = 'DatLichHocNauAn')
BEGIN
    USE master;
    ALTER DATABASE DatLichHocNauAn SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE DatLichHocNauAn;
END;
GO

CREATE DATABASE DatLichHocNauAn;
GO
USE DatLichHocNauAn;
GO

-- 1. NGƯỜI DÙNG
CREATE TABLE NguoiDung (
    maNguoiDung INT PRIMARY KEY IDENTITY(1,1),
    tenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    hoTen NVARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    soDienThoai VARCHAR(15),
    diaChi NVARCHAR(255),
    vaiTro NVARCHAR(20) DEFAULT N'HocVien',
    trangThai NVARCHAR(20) DEFAULT N'HoatDong',
    ngayTao DATETIME DEFAULT GETDATE(),
    lanCapNhatCuoi DATETIME DEFAULT GETDATE()
);

-- 2. OTP
CREATE TABLE OTP (
    maOTP INT PRIMARY KEY IDENTITY(1,1),
    maNguoiDung INT,
    maXacThuc VARCHAR(6) NOT NULL,
    loaiOTP NVARCHAR(30) NOT NULL,
    thoiGianTao DATETIME DEFAULT GETDATE(),
    thoiGianHetHan DATETIME NOT NULL,
    daSuDung BIT DEFAULT 0,
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
);

-- 3. GIÁO VIÊN
CREATE TABLE GiaoVien (
    maGiaoVien INT PRIMARY KEY IDENTITY(1,1),
    maNguoiDung INT UNIQUE NOT NULL,
    chuyenMon NVARCHAR(200),
    kinhNghiem NVARCHAR(MAX),
    lichSuKinhNghiem NVARCHAR(MAX),  -- *** MỚI THÊM *** Lịch sử kinh nghiệm chi tiết
    moTa NVARCHAR(MAX),
    hinhAnh VARCHAR(255),
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
);

-- 4. LỚP HỌC - HỖ TRỢ LỊCH TRÌNH LẶP LẠI
CREATE TABLE LopHoc (
    maLopHoc INT PRIMARY KEY IDENTITY(1,1),
    tenLopHoc NVARCHAR(200) NOT NULL,
    moTa NVARCHAR(500),  -- Mô tả ngắn hiển thị ở ngoài
    gioiThieu NVARCHAR(MAX),  -- Giới thiệu chi tiết khi xem detail
    giaTriSauBuoiHoc NVARCHAR(MAX),  -- Giá trị sau buổi học
    maGiaoVien INT,
    soLuongToiDa INT DEFAULT 20,
    soLuongHienTai INT DEFAULT 0,
    giaTien DECIMAL(10,2) NOT NULL,
    thoiGian NVARCHAR(100),
    diaDiem NVARCHAR(255),
    trangThai NVARCHAR(30) DEFAULT N'Sắp diễn ra',
    
    -- *** LỊCH TRÌNH LẶP LẠI ***
    loaiLich NVARCHAR(20) DEFAULT N'HangNgay',  -- 'HangNgay', 'HangTuan', 'MotLan'
    ngayBatDau DATE NOT NULL,  -- Ngày bắt đầu lịch trình
    ngayKetThuc DATE,  -- Ngày kết thúc (NULL = không giới hạn)
    cacNgayTrongTuan VARCHAR(50),  -- '2,3,4,5,6' (1=CN, 2=T2, ..., 7=T7)
    
    gioBatDau TIME,
    gioKetThuc TIME,
    hinhAnh VARCHAR(255),
    soLuongDanhGia INT DEFAULT 0,
    saoTrungBinh FLOAT DEFAULT 0,
    coUuDai BIT DEFAULT 0,
    ngayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maGiaoVien) REFERENCES GiaoVien(maGiaoVien)
);

-- 5. DANH MỤC MÓN ĂN
CREATE TABLE DanhMucMonAn (
    maDanhMuc INT PRIMARY KEY IDENTITY(1,1),
    tenDanhMuc NVARCHAR(100) NOT NULL,
    iconDanhMuc VARCHAR(255),
    thuTu INT DEFAULT 1
);

-- 6. MÓN ĂN
CREATE TABLE MonAn (
    maMonAn INT PRIMARY KEY IDENTITY(1,1),
    maLopHoc INT NOT NULL,
    maDanhMuc INT NOT NULL,
    tenMon NVARCHAR(200) NOT NULL,
    gioiThieu NVARCHAR(MAX),
    nguyenLieu NVARCHAR(MAX),
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucMonAn(maDanhMuc),
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) 
);

-- 6.1. HÌNH ẢNH MÓN ĂN - *** BẢNG MỚI *** (cho slideshow)
CREATE TABLE HinhAnhMonAn (
    maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
    maMonAn INT NOT NULL,
    duongDan VARCHAR(255) NOT NULL,
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn) ON DELETE CASCADE
);

-- 7. HÌNH ẢNH LỚP HỌC
CREATE TABLE HinhAnhLopHoc (
    maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
    maLopHoc INT NOT NULL,
    duongDan VARCHAR(255) NOT NULL,
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- 9. ĐẶT LỊCH
CREATE TABLE DatLich (
    maDatLich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maLopHoc INT NOT NULL,
    soLuongNguoi INT DEFAULT 1,
    tongTien DECIMAL(10,2),
    tenNguoiDat NVARCHAR(100),
    emailNguoiDat VARCHAR(100),
    sdtNguoiDat VARCHAR(15),
    ngayDat DATETIME DEFAULT GETDATE(),
    trangThai NVARCHAR(30) DEFAULT N'Chờ Duyệt',
    ghiChu NVARCHAR(MAX),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- 10. THANH TOÁN
CREATE TABLE ThanhToan (
    maThanhToan INT PRIMARY KEY IDENTITY(1,1),
    maDatLich INT NOT NULL,
    soTien DECIMAL(10,2) NOT NULL,
    phuongThuc NVARCHAR(30) NOT NULL,
    trangThai NVARCHAR(30) DEFAULT N'Chưa Thanh Toán',
    ngayThanhToan DATETIME,
    maGiaoDich VARCHAR(100),
    ghiChu NVARCHAR(MAX),
    FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich)
);

-- 11. ĐÁNH GIÁ
CREATE TABLE DanhGia (
    maDanhGia INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maLopHoc INT NOT NULL,
    diemDanhGia INT CHECK (diemDanhGia BETWEEN 1 AND 5),
    binhLuan NVARCHAR(MAX),
    ngayDanhGia DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- 12. THÔNG BÁO
CREATE TABLE ThongBao (
    maThongBao INT PRIMARY KEY IDENTITY(1,1),
    maNguoiNhan INT,
    tieuDe NVARCHAR(255) NOT NULL,
    noiDung NVARCHAR(MAX) NOT NULL,
    loaiThongBao NVARCHAR(30) DEFAULT N'Hệ Thống',
    hinhAnh VARCHAR(255),
    daDoc BIT DEFAULT 0,
    ngayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maNguoiNhan) REFERENCES NguoiDung(maNguoiDung)
);

-- 13. YÊU THÍCH
CREATE TABLE YeuThich (
    maYeuThich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maLopHoc INT NOT NULL,
    ngayThem DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc),
    CONSTRAINT UQ_YeuThich UNIQUE (maHocVien, maLopHoc)
);

-- 14. ƯU ĐÃI
CREATE TABLE UuDai (
    maUuDai INT PRIMARY KEY IDENTITY(1,1),
    maCode VARCHAR(50) UNIQUE NOT NULL,
    tenUuDai NVARCHAR(200) NOT NULL,
    moTa NVARCHAR(MAX),
    loaiGiam NVARCHAR(20) NOT NULL,
    giaTriGiam DECIMAL(10,2) NOT NULL,
    giamToiDa DECIMAL(10,2),
    soLuong INT,
    soLuongDaSuDung INT DEFAULT 0,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    hinhAnh VARCHAR(255),
    trangThai NVARCHAR(20) DEFAULT N'Hoạt Động',
    ngayTao DATETIME DEFAULT GETDATE()
);

-- 15. LỊCH SỬ ƯU ĐÃI
CREATE TABLE LichSuUuDai (
    maLichSu INT PRIMARY KEY IDENTITY(1,1),
    maUuDai INT NOT NULL,
    maDatLich INT NOT NULL,
    soTienGiam DECIMAL(10,2) NOT NULL,
    ngaySuDung DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maUuDai) REFERENCES UuDai(maUuDai),
    FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich)
);

-- 16. HÓA ĐƠN
CREATE TABLE HoaDon (
    maHoaDon INT PRIMARY KEY IDENTITY(1,1),
    maThanhToan INT NOT NULL,
    soHoaDon VARCHAR(50) UNIQUE NOT NULL,
    ngayXuatHoaDon DATETIME DEFAULT GETDATE(),
    tongTien DECIMAL(10,2) NOT NULL,
    VAT DECIMAL(10,2) DEFAULT 0,
    thanhTien DECIMAL(10,2) NOT NULL,
    trangThai NVARCHAR(20) DEFAULT N'Đã Xuất',
    filePDF VARCHAR(255),
    FOREIGN KEY (maThanhToan) REFERENCES ThanhToan(maThanhToan)
);

---------------------------------------------------------------
-- TRIGGERS
---------------------------------------------------------------
GO
CREATE TRIGGER trg_CapNhatSoLuongHocVien_Insert
ON DatLich
AFTER INSERT
AS
BEGIN
    UPDATE LopHoc
    SET soLuongHienTai = soLuongHienTai + 1
    FROM LopHoc lh
    JOIN inserted i ON lh.maLopHoc = i.maLopHoc
    WHERE i.trangThai = N'Đã Duyệt';
END;
GO

CREATE TRIGGER trg_CapNhatSoLuongHocVien_Update
ON DatLich
AFTER UPDATE
AS
BEGIN
    UPDATE LopHoc
    SET soLuongHienTai = soLuongHienTai - 1
    FROM LopHoc lh
    JOIN deleted d ON lh.maLopHoc = d.maLopHoc
    JOIN inserted i ON d.maDatLich = i.maDatLich
    WHERE d.trangThai = N'Đã Duyệt' AND i.trangThai = N'Đã Hủy';

    UPDATE LopHoc
    SET soLuongHienTai = soLuongHienTai + 1
    FROM LopHoc lh
    JOIN deleted d ON lh.maLopHoc = d.maLopHoc
    JOIN inserted i ON d.maDatLich = i.maDatLich
    WHERE d.trangThai <> N'Đã Duyệt' AND i.trangThai = N'Đã Duyệt';
END;
GO

CREATE TRIGGER trg_ThongBaoDatLich
ON DatLich
AFTER INSERT
AS
BEGIN
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
    SELECT 
        i.maHocVien,
        N'Đặt lịch thành công',
        N'Bạn đã đặt lịch học lớp ' + lh.tenLopHoc,
        N'LopHoc'
    FROM inserted i
    JOIN LopHoc lh ON i.maLopHoc = lh.maLopHoc;
END;
GO

CREATE TRIGGER trg_CapNhatUuDai
ON LichSuUuDai
AFTER INSERT
AS
BEGIN
    UPDATE UuDai
    SET soLuongDaSuDung = soLuongDaSuDung + 1
    FROM UuDai u
    JOIN inserted i ON u.maUuDai = i.maUuDai;
END;
GO

CREATE TRIGGER trg_OTP_HetHan
ON OTP
INSTEAD OF INSERT
AS
BEGIN
    INSERT INTO OTP (
        maNguoiDung, maXacThuc, loaiOTP,
        thoiGianTao, thoiGianHetHan, daSuDung
    )
    SELECT
        maNguoiDung, maXacThuc, loaiOTP,
        GETDATE(), DATEADD(MINUTE, 5, GETDATE()), daSuDung
    FROM inserted;
END;
GO

-- TRIGGER 6: Tự động cập nhật số lượng đánh giá và sao trung bình
CREATE TRIGGER trg_CapNhatDanhGiaLopHoc
ON DanhGia
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    -- Cập nhật cho các lớp học bị ảnh hưởng bởi INSERT hoặc UPDATE
    UPDATE LopHoc
    SET 
        soLuongDanhGia = (SELECT COUNT(*) FROM DanhGia WHERE maLopHoc = LopHoc.maLopHoc),
        saoTrungBinh = (SELECT AVG(CAST(diemDanhGia AS FLOAT)) FROM DanhGia WHERE maLopHoc = LopHoc.maLopHoc)
    WHERE maLopHoc IN (SELECT DISTINCT maLopHoc FROM inserted);

    -- Cập nhật cho các lớp học bị ảnh hưởng bởi DELETE
    UPDATE LopHoc
    SET 
        soLuongDanhGia = (SELECT COUNT(*) FROM DanhGia WHERE maLopHoc = LopHoc.maLopHoc),
        saoTrungBinh = ISNULL((SELECT AVG(CAST(diemDanhGia AS FLOAT)) FROM DanhGia WHERE maLopHoc = LopHoc.maLopHoc), 0)
    WHERE maLopHoc IN (SELECT DISTINCT maLopHoc FROM deleted);
END;
GO

---------------------------------------------------------------
-- INSERT DATA MẪU
---------------------------------------------------------------

-- 1. NGƯỜI DÙNG
INSERT INTO NguoiDung (tenDangNhap, matKhau, hoTen, email, soDienThoai, diaChi, vaiTro, trangThai) VALUES
(N'admin', N'admin123', N'Quản Trị Viên', N'admin@localcooking.vn', N'0901234567', N'123 Nguyễn Huệ, Q1, TP.HCM', N'Admin', N'HoatDong'),
(N'VanAn', N'gv123', N'Nguyễn Văn An', N'nguyenvanan@gmail.com', N'0912345678', N'456 Lê Lợi, Q1, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'ThiBinh', N'gv123', N'Trần Thị Bình', N'tranthibinh@gmail.com', N'0923456789', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'VanCuong', N'hv123', N'Lê Văn Cường', N'levancuong@gmail.com', N'0934567890', N'321 Võ Văn Tần, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'ThiDung', N'hv123', N'Phạm Thị Dung', N'phamthidung@gmail.com', N'0945678901', N'654 Hai Bà Trưng, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'VanEm', N'hv123', N'Hoàng Văn Em', N'hoangvanem@gmail.com', N'0956789012', N'987 Cách Mạng Tháng 8, Q10, TP.HCM', N'HocVien', N'HoatDong');

-- 2. GIÁO VIÊN
INSERT INTO GiaoVien (maNguoiDung, chuyenMon, kinhNghiem, lichSuKinhNghiem, moTa, hinhAnh) VALUES
(2, 
 N'Ẩm thực Việt Nam truyền thống', 
 N'20 năm kinh nghiệm trong lĩnh vực ẩm thực Việt Nam, từng làm việc tại nhiều nhà hàng cao cấp và khách sạn 5 sao', 
 N'• 2005-2008: Bếp phó tại Nhà hàng Cung Đình Huế, Huế - Chuyên về các món ăn cung đình truyền thống

• 2008-2012: Bếp trưởng tại Khách sạn Saigon Morin, Huế - Phụ trách bộ phận ẩm thực Việt Nam, phục vụ khách quốc tế

• 2012-2015: Sous Chef tại Nhà hàng Món Huế, Hà Nội - Giới thiệu ẩm thực Huế đến thực khách miền Bắc

• 2015-2018: Executive Chef tại Resort Angsana Lăng Cô - Phát triển thực đơn fusion kết hợp ẩm thực Việt và quốc tế

• 2018-2020: Giảng viên thỉnh giảng tại Trường Cao đẳng Du lịch Huế - Giảng dạy môn Ẩm thực Việt Nam

• 2020-nay: Chuyên gia ẩm thực độc lập, tổ chức các lớp dạy nấu ăn và tư vấn thực đơn cho nhà hàng',
 N'Chuyên gia về các món ăn Việt Nam cổ truyền, đặc biệt là món Huế và Hà Nội. Từng đạt giải Nhất cuộc thi "Vua đầu bếp Việt Nam 2016" hạng mục Ẩm thực truyền thống. Có kinh nghiệm đào tạo hơn 500 học viên về nghề bếp.', 
 N'giaovien1.jpg'),
(3, 
 N'Bánh ngọt và tráng miệng', 
 N'10 năm kinh nghiệm làm bánh Pháp và bánh Âu, tốt nghiệp Le Cordon Bleu Paris với bằng Diplôme de Pâtisserie', 
 N'• 2010-2013: Học viên tại Le Cordon Bleu Paris, Pháp - Tốt nghiệp loại Xuất sắc chuyên ngành Pâtisserie

• 2013-2015: Pastry Chef tại Pâtisserie Ladurée, Paris - Chuyên làm macaron và các loại bánh Pháp cao cấp

• 2015-2017: Head Pastry Chef tại Khách sạn Park Hyatt Saigon - Phụ trách bộ phận bánh ngọt, phục vụ tiệc cưới và sự kiện

• 2017-2019: Pastry Chef tại Nhà hàng L''Usine, TP.HCM - Sáng tạo thực đơn bánh ngọt theo mùa

• 2019-2021: Mở cửa hàng bánh riêng "Sweet Dreams Bakery" - Chuyên bánh Pháp và bánh sinh nhật cao cấp

• 2021-nay: Giảng viên dạy làm bánh và tổ chức workshop, chia sẻ kiến thức về nghệ thuật làm bánh Pháp',
 N'Chuyên về bánh Pháp, bánh Âu và các món tráng miệng hiện đại. Từng đoạt huy chương Vàng tại cuộc thi "Pastry Chef of the Year 2018" khu vực Đông Nam Á. Đam mê sáng tạo các món bánh kết hợp hương vị Á - Âu.', 
 N'giaovien2.jpg');

-- 3. DANH MỤC MÓN ĂN
INSERT INTO DanhMucMonAn (tenDanhMuc, iconDanhMuc, thuTu) VALUES
(N'Món khai vị', N'ic_appetizer.png', 1),
(N'Món chính', N'ic_main_dish.png', 2),
(N'Món tráng miệng', N'ic_dessert.png', 3);

-- 4. LỚP HỌC - 16 LỚP (4 địa phương x 4 chủ đề) - HỖ TRỢ LỊCH TRÌNH LẶP LẠI
INSERT INTO LopHoc (
    tenLopHoc, moTa, gioiThieu, giaTriSauBuoiHoc, maGiaoVien, 
    soLuongToiDa, soLuongHienTai, giaTien, 
    thoiGian, diaDiem, trangThai, 
    loaiLich, ngayBatDau, ngayKetThuc, cacNgayTrongTuan,
    gioBatDau, gioKetThuc, 
    hinhAnh, soLuongDanhGia, saoTrungBinh, coUuDai
) VALUES
-- === HÀ NỘI (4 lớp) ===
-- Lớp 1: Hàng ngày
(N'Ẩm thực phố cổ Hà Nội', N'Khám phá hương vị đặc trưng của ẩm thực phố cổ với phở, bún chả, chả cá', 
N'Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội', 
N'• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc
• Hiểu về văn hóa ẩm thực phố cổ
• Tự tin làm bún chả và chả cá Lã Vọng', 
1, 20, 0, 650000, N'09:00 - 12:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', N'Đang mở', 
N'HangNgay', '2025-01-01', '2025-12-31', NULL,
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 2: Thứ 2, 4, 6
(N'Bún và miến Hà Nội', N'Học cách làm các món bún đặc sản: bún thang, bún ốc, bún riêu', 
N'Khóa học chuyên sâu về các món bún truyền thống Hà Nội', 
N'• Làm chủ nghệ thuật nấu nước dùng trong
• Kỹ thuật chế biến ốc và cua đồng
• Bí quyết làm bún thang chuẩn vị', 
1, 18, 0, 580000, N'14:00 - 17:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '2,4,6',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- Lớp 3: Thứ 3, 5, 7
(N'Bánh dân gian Hà Nội', N'Học làm bánh cuốn, bánh đúc, bánh khúc - đặc sản làng nghề', 
N'Khám phá nghệ thuật làm bánh truyền thống của người Hà Nội', 
N'• Kỹ thuật tráng bánh cuốn mỏng như giấy
• Bí quyết làm nhân thơm ngon
• Hiểu về nguồn gốc các loại bánh', 
2, 15, 0, 520000, N'09:00 - 12:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '3,5,7',
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 4: Cuối tuần (Thứ 7, CN)
(N'Món nhậu Hà Nội', N'Các món nhậu đặc trưng: nem chua rán, chả rươi, bún đậu mắm tôm', 
N'Trải nghiệm văn hóa nhậu nhẹt đặc trưng của người Hà Nội', 
N'• Làm nem chua rán giòn tan
• Kỹ thuật chế biến rươi đồng
• Pha chế mắm tôm chuẩn vị', 
1, 20, 0, 600000, N'14:00 - 17:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '7,1',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- === HUẾ (4 lớp) ===
-- Lớp 5: Hàng ngày
(N'Ẩm thực cung đình Huế', N'Khám phá tinh hoa ẩm thực hoàng gia với bún bò, cơm hến, bánh bèo', 
N'Trải nghiệm nấu các món ăn cung đình tinh tế và cầu kỳ', 
N'• Nắm vững kỹ thuật nấu bún bò Huế chính gốc
• Hiểu về văn hóa ẩm thực cung đình
• Làm chủ nghệ thuật trang trí món ăn', 
1, 20, 0, 715000, N'09:00 - 12:00', N'23 Lê Duẩn, Huế', N'Đang mở', 
N'HangNgay', '2025-01-01', '2025-12-31', NULL,
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 6: Thứ 2, 4, 6
(N'Bánh Huế truyền thống', N'Học làm bánh bèo, bánh nậm, bánh lọc - đặc sản xứ Huế', 
N'Khóa học chuyên sâu về các loại bánh đặc trưng Huế', 
N'• Kỹ thuật tráng bánh bèo mịn màng
• Bí quyết gói bánh nậm đẹp mắt
• Làm bánh lọc trong vắt', 
2, 18, 0, 550000, N'14:00 - 17:00', N'23 Lê Duẩn, Huế', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '2,4,6',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- Lớp 7: Thứ 3, 5, 7
(N'Món chay Huế', N'Ẩm thực chay tinh tế: bún chay, bánh bột lọc chay, chè sen', 
N'Khám phá nghệ thuật nấu món chay theo phong cách Huế', 
N'• Nấu món chay đậm đà không thua món mặn
• Kỹ thuật chế biến rau củ sáng tạo
• Làm nước dùng chay thơm ngon', 
1, 15, 0, 480000, N'09:00 - 12:00', N'23 Lê Duẩn, Huế', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '3,5,7',
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 8: Cuối tuần
(N'Chè và tráng miệng Huế', N'Học làm chè Huế, bánh ít, bánh ram - món ngọt đặc sản', 
N'Trải nghiệm làm các món tráng miệng truyền thống Huế', 
N'• Nấu chè Huế thanh mát
• Làm bánh ít nhân đậu xanh
• Kỹ thuật ram bánh giòn rụm', 
2, 15, 0, 520000, N'14:00 - 17:00', N'23 Lê Duẩn, Huế', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '7,1',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- === ĐÀ NẴNG (4 lớp) ===
-- Lớp 9: Hàng ngày
(N'Hải sản Đà Nẵng', N'Chế biến hải sản tươi sống: mì Quảng, bánh tráng cuốn thịt heo', 
N'Học cách chế biến hải sản theo phong cách miền Trung', 
N'• Kỹ thuật chọn và sơ chế hải sản tươi
• Nấu mì Quảng đậm đà
• Làm bánh tráng cuốn đặc biệt', 
1, 20, 0, 680000, N'09:00 - 12:00', N'78 Trần Phú, Đà Nẵng', N'Đang mở', 
N'HangNgay', '2025-01-01', '2025-12-31', NULL,
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 10: Thứ 2, 4, 6
(N'Bánh xèo và nem lụi Đà Nẵng', N'Học làm bánh xèo giòn tan và nem lụi thơm lừng', 
N'Khám phá món ăn đường phố nổi tiếng Đà Nẵng', 
N'• Bí quyết làm bánh xèo giòn rụm
• Kỹ thuật nướng nem lụi thơm phức
• Pha chế nước chấm chuẩn vị', 
2, 18, 0, 590000, N'14:00 - 17:00', N'78 Trần Phú, Đà Nẵng', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '2,4,6',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- Lớp 11: Thứ 3, 5, 7
(N'Bún mắm và bún cá Đà Nẵng', N'Nấu bún mắm nêm đậm đà và bún cá ngọt thanh', 
N'Học cách nấu các món bún đặc trưng miền Trung', 
N'• Nấu nước mắm nêm thơm ngon
• Kỹ thuật làm chả cá tươi
• Bí quyết nấu nước dùng ngọt', 
1, 20, 0, 620000, N'09:00 - 12:00', N'78 Trần Phú, Đà Nẵng', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '3,5,7',
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 12: Cuối tuần
(N'Bánh canh và cao lầu', N'Học làm bánh canh cua và cao lầu Hội An', 
N'Khám phá món ăn đặc sản Đà Nẵng - Hội An', 
N'• Làm bánh canh dai ngon
• Nấu nước dùng cua đậm đà
• Kỹ thuật làm cao lầu truyền thống', 
1, 18, 0, 650000, N'14:00 - 17:00', N'78 Trần Phú, Đà Nẵng', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '7,1',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- === CẦN THƠ (4 lớp) ===
-- Lớp 13: Hàng ngày
(N'Ẩm thực miệt vườn Cần Thơ', N'Món ăn đồng quê: lẩu mắm, cá lóc nướng trui, gỏi cá', 
N'Trải nghiệm nấu các món ăn đặc trưng miệt vườn', 
N'• Nấu lẩu mắm chuẩn vị miền Tây
• Kỹ thuật nướng cá lóc thơm phức
• Làm gỏi cá tươi ngon', 
1, 20, 0, 580000, N'09:00 - 12:00', N'56 Mậu Thân, Cần Thơ', N'Đang mở', 
N'HangNgay', '2025-01-01', '2025-12-31', NULL,
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 14: Thứ 2, 4, 6
(N'Bánh và bún miền Tây', N'Học làm bánh xèo, bánh khọt, bún riêu cua đồng', 
N'Khám phá món ăn dân dã miền sông nước', 
N'• Làm bánh xèo miền Tây to và giòn
• Kỹ thuật làm bánh khọt nhỏ xinh
• Nấu bún riêu cua đồng đậm đà', 
2, 18, 0, 550000, N'14:00 - 17:00', N'56 Mậu Thân, Cần Thơ', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '2,4,6',
'14:00', '17:00', N'phobo.png', 0, 0, 0),

-- Lớp 15: Thứ 3, 5, 7
(N'Hủ tiếu và hủ tiếu Nam Vang', N'Nấu hủ tiếu Sa Đéc và hủ tiếu Nam Vang', 
N'Học cách nấu món hủ tiếu đặc trưng miền Tây', 
N'• Nấu nước dùng hủ tiếu ngọt thanh
• Kỹ thuật chế biến sườn, gan, tim
• Bí quyết làm hủ tiếu Nam Vang', 
1, 20, 0, 600000, N'09:00 - 12:00', N'56 Mậu Thân, Cần Thơ', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '3,5,7',
'09:00', '12:00', N'phobo.png', 0, 0, 1),

-- Lớp 16: Cuối tuần
(N'Chè và tráng miệng miền Tây', N'Học làm chè chuối, chè đậu xanh, bánh chuối nướng', 
N'Trải nghiệm làm món ngọt dân dã miền sông nước', 
N'• Nấu chè chuối thơm ngon
• Làm chè đậu xanh mát lạnh
• Kỹ thuật nướng bánh chuối', 
2, 15, 0, 480000, N'14:00 - 17:00', N'56 Mậu Thân, Cần Thơ', N'Đang mở', 
N'HangTuan', '2025-01-01', '2025-12-31', '7,1',
'14:00', '17:00', N'phobo.png', 0, 0, 0);

-- 5. MÓN ĂN - 64 món (16 lớp x 4 món)
INSERT INTO MonAn (maLopHoc, maDanhMuc, tenMon, gioiThieu, nguyenLieu) VALUES
-- === LỚP 1: Ẩm thực phố cổ Hà Nội ===
(1, 1, N'Nem rán Hà Nội', N'Nem rán giòn rụm với nhân thịt, miến, mộc nhĩ thơm ngon', N'Bánh đa nem, thịt lợn, miến, mộc nhĩ, cà rốt, hành tây'),
(1, 2, N'Phở bò Hà Nội', N'Món phở truyền thống với nước dùng trong, thơm, thịt bò mềm', N'Bánh phở, thịt bò, xương bò, hành, gừng, gia vị'),
(1, 2, N'Bún chả Hà Nội', N'Đặc sản Hà Nội với chả nướng thơm lừng, bún tươi', N'Bún, thịt ba chỉ, thịt nạc, nước mắm, rau sống'),
(1, 3, N'Chè ba màu', N'Món tráng miệng mát lạnh với đậu đỏ, đậu xanh, thạch', N'Đậu đỏ, đậu xanh, thạch, nước cốt dừa, đá bào'),
-- === LỚP 2: Bún và miến Hà Nội ===
(2, 1, N'Gỏi cuốn Hà Nội', N'Gỏi cuốn tươi mát với tôm, thịt, bún và rau sống', N'Bánh tráng, tôm, thịt, bún, rau sống, nước chấm'),
(2, 2, N'Bún thang', N'Món bún tinh tế với nước dùng trong, trứng, giò lụa', N'Bún, gà, trứng, giò lụa, nấm hương, hành phi'),
(2, 2, N'Bún ốc', N'Bún ốc chua cay đặc trưng Hà Nội', N'Bún, ốc, cà chua, dấm, ớt, rau thơm'),
(2, 3, N'Chè hạt sen', N'Chè hạt sen thanh mát, bổ dưỡng', N'Hạt sen, long nhãn, đường phèn, nước cốt dừa'),
-- === LỚP 3: Bánh dân gian Hà Nội ===
(3, 1, N'Bánh cuốn', N'Bánh cuốn mỏng như giấy với nhân thịt, mộc nhĩ', N'Bột gạo, thịt lợn, mộc nhĩ, hành khô, nước mắm'),
(3, 2, N'Bánh đúc', N'Bánh đúc trắng mềm mịn với nước mắm chua ngọt', N'Bột gạo, tôm khô, mỡ hành, nước mắm, đường'),
(3, 2, N'Bánh khúc', N'Bánh khúc nhân đậu xanh thơm ngon', N'Gạo nếp, lá khúc, đậu xanh, thịt ba chỉ'),
(3, 3, N'Bánh trôi nước', N'Bánh trôi nhân đậu xanh trong nước gừng ngọt', N'Bột nếp, đậu xanh, gừng, đường'),
-- === LỚP 4: Món nhậu Hà Nội ===
(4, 1, N'Nem chua rán', N'Nem chua rán giòn tan, chua ngọt hấp dẫn', N'Nem chua, bột chiên giòn, dầu ăn'),
(4, 2, N'Chả rươi', N'Chả rươi thơm phức, đặc sản mùa thu Hà Nội', N'Rươi, thịt lợn, trứng, bì heo, lá chanh'),
(4, 2, N'Bún đậu mắm tôm', N'Bún đậu với mắm tôm đậm đà, chả cốm', N'Bún, đậu rán, chả cốm, mắm tôm, rau sống'),
(4, 3, N'Chè lam', N'Chè lam nếp thơm, ngọt dịu', N'Nếp than, đậu xanh, nước cốt dừa, đường'),
-- === LỚP 5: Ẩm thực cung đình Huế ===
(5, 1, N'Bánh bèo', N'Bánh bèo Huế mềm mịn với tôm khô, mỡ hành', N'Bột gạo, bột năng, tôm khô, mỡ hành, nước cốt dừa'),
(5, 2, N'Bún bò Huế', N'Món bún cay nồng đặc trưng xứ Huế', N'Bún, thịt bò, giò heo, sả, ớt, mắm ruốc'),
(5, 2, N'Cơm hến', N'Cơm hến Huế với hến xào thơm lừng', N'Cơm nguội, hến, đậu phộng, tóp mỡ, rau thơm'),
(5, 3, N'Chè Huế', N'Chè Huế thanh mát với nhiều loại hạt', N'Đậu đỏ, đậu xanh, hạt sen, thạch, nước cốt dừa'),
-- === LỚP 6: Bánh Huế truyền thống ===
(6, 1, N'Bánh nậm', N'Bánh nậm gói lá chuối với nhân tôm thịt', N'Bột gạo, tôm, thịt ba chỉ, lá chuối, nước mắm'),
(6, 2, N'Bánh lọc', N'Bánh lọc trong vắt với nhân tôm thịt', N'Bột năng, bột sắn, tôm, thịt ba chỉ, mộc nhĩ'),
(6, 2, N'Bánh ít', N'Bánh ít nhân đậu xanh thơm ngon', N'Bột gạo, đậu xanh, dừa nạo, đường'),
(6, 3, N'Bánh ram', N'Bánh ram giòn rụm với nhân đậu xanh', N'Bột gạo, đậu xanh, đường, dừa nạo'),
-- === LỚP 7: Món chay Huế ===
(7, 1, N'Gỏi cuốn chay', N'Gỏi cuốn chay với rau củ tươi ngon', N'Bánh tráng, rau sống, bún, nấm, đậu hũ'),
(7, 2, N'Bún chay Huế', N'Bún chay với nước dùng thơm ngon', N'Bún, nấm, đậu hũ, rau củ, sả, ớt'),
(7, 2, N'Bánh bột lọc chay', N'Bánh bột lọc chay với nhân nấm', N'Bột năng, nấm, đậu hũ, mộc nhĩ'),
(7, 3, N'Chè sen', N'Chè sen thanh mát, bổ dưỡng', N'Hạt sen, long nhãn, đường phèn'),
-- === LỚP 8: Chè và tráng miệng Huế ===
(8, 1, N'Bánh lọc lá', N'Bánh lọc gói lá chuối thơm ngon', N'Bột năng, tôm, thịt, lá chuối'),
(8, 2, N'Bánh ít lá gai', N'Bánh ít lá gai nhân đậu xanh', N'Bột nếp, lá gai, đậu xanh, đường'),
(8, 2, N'Bánh ram ít', N'Bánh ram ít giòn tan thơm ngon', N'Bột gạo, đậu xanh, dừa, đường'),
(8, 3, N'Chè bưởi', N'Chè bưởi thanh mát đặc sản Huế', N'Bưởi, đường phèn, nước cốt dừa'),
-- === LỚP 9: Hải sản Đà Nẵng ===
(9, 1, N'Gỏi cá mai', N'Gỏi cá mai tươi ngon với rau thơm', N'Cá mai, rau thơm, đậu phộng, bánh tráng'),
(9, 2, N'Mì Quảng', N'Mì Quảng đặc sản với tôm, thịt, trứng', N'Mì Quảng, tôm, thịt, trứng, đậu phộng, rau sống'),
(9, 2, N'Bánh tráng cuốn thịt heo', N'Bánh tráng cuốn thịt heo nướng thơm lừng', N'Bánh tráng, thịt heo, rau sống, nước chấm'),
(9, 3, N'Chè bắp', N'Chè bắp ngọt thanh, mát lạnh', N'Bắp, đường, nước cốt dừa'),
-- === LỚP 10: Bánh xèo và nem lụi Đà Nẵng ===
(10, 1, N'Nem lụi', N'Nem lụi nướng thơm phức đặc sản Đà Nẵng', N'Thịt lợn, mỡ, tỏi, hành, gia vị'),
(10, 2, N'Bánh xèo', N'Bánh xèo giòn tan với tôm, thịt, giá', N'Bột gạo, tôm, thịt, giá, trứng'),
(10, 2, N'Bánh tráng thịt heo', N'Bánh tráng cuốn thịt heo nướng', N'Bánh tráng, thịt heo, rau sống'),
(10, 3, N'Chè đậu xanh', N'Chè đậu xanh mát lạnh, bổ dưỡng', N'Đậu xanh, đường, nước cốt dừa'),
-- === LỚP 11: Bún mắm và bún cá Đà Nẵng ===
(11, 1, N'Chả cá', N'Chả cá tươi ngon đặc sản miền Trung', N'Cá thu, bột năng, gia vị'),
(11, 2, N'Bún mắm nêm', N'Bún mắm nêm đậm đà hương vị miền Trung', N'Bún, mắm nêm, thịt heo, tôm, rau sống'),
(11, 2, N'Bún cá', N'Bún cá ngọt thanh với chả cá tươi', N'Bún, cá, chả cá, cà chua, rau thơm'),
(11, 3, N'Chè khoai môn', N'Chè khoai môn béo ngậy thơm ngon', N'Khoai môn, đường, nước cốt dừa'),
-- === LỚP 12: Bánh canh và cao lầu ===
(12, 1, N'Gỏi tôm', N'Gỏi tôm tươi ngon với rau thơm', N'Tôm, rau thơm, đậu phộng, nước mắm'),
(12, 2, N'Bánh canh cua', N'Bánh canh cua đậm đà hương vị biển', N'Bánh canh, cua, thịt heo, rau thơm'),
(12, 2, N'Cao lầu Hội An', N'Cao lầu đặc sản Hội An với sợi bánh dai', N'Bánh cao lầu, thịt heo, giá, rau thơm'),
(12, 3, N'Chè thái', N'Chè thái mát lạnh với nhiều loại trái cây', N'Thạch, trái cây, nước cốt dừa, đá bào'),
-- === LỚP 13: Ẩm thực miệt vườn Cần Thơ ===
(13, 1, N'Gỏi cá lóc', N'Gỏi cá lóc tươi ngon với rau thơm', N'Cá lóc, rau thơm, dừa nạo, đậu phộng'),
(13, 2, N'Lẩu mắm', N'Lẩu mắm đậm đà hương vị miền Tây', N'Cá lóc, tôm, rau muống, mắm, ớt'),
(13, 2, N'Cá lóc nướng trui', N'Cá lóc nướng trui thơm phức', N'Cá lóc, sả, ớt, muối, tiêu'),
(13, 3, N'Chè chuối', N'Chè chuối nóng thơm ngon', N'Chuối, đường, nước cốt dừa, bột năng'),
-- === LỚP 14: Bánh và bún miền Tây ===
(14, 1, N'Bánh khọt', N'Bánh khọt nhỏ xinh với tôm tươi', N'Bột gạo, tôm, dừa nạo, hành lá'),
(14, 2, N'Bánh xèo miền Tây', N'Bánh xèo to giòn rụm đặc sản miền Tây', N'Bột gạo, tôm, thịt, giá, trứng'),
(14, 2, N'Bún riêu cua đồng', N'Bún riêu cua đồng đậm đà', N'Bún, cua đồng, cà chua, đậu hũ, rau thơm'),
(14, 3, N'Chè đậu đỏ', N'Chè đậu đỏ ngọt thanh mát lạnh', N'Đậu đỏ, đường, nước cốt dừa'),
-- === LỚP 15: Hủ tiếu và hủ tiếu Nam Vang ===
(15, 1, N'Gỏi sứa', N'Gỏi sứa giòn ngon với rau thơm', N'Sứa, rau thơm, đậu phộng, nước mắm'),
(15, 2, N'Hủ tiếu Sa Đéc', N'Hủ tiếu Sa Đéc ngọt thanh đặc sản', N'Hủ tiếu, thịt heo, tôm, gan, tim'),
(15, 2, N'Hủ tiếu Nam Vang', N'Hủ tiếu Nam Vang đậm đà hương vị', N'Hủ tiếu, thịt heo, tôm, gan, tim, mực'),
(15, 3, N'Chè thập cẩm', N'Chè thập cẩm với nhiều loại đậu', N'Đậu đỏ, đậu xanh, thạch, nước cốt dừa'),
-- === LỚP 16: Chè và tráng miệng miền Tây ===
(16, 1, N'Bánh chuối hấp', N'Bánh chuối hấp thơm ngon dân dã', N'Chuối, bột gạo, dừa nạo, đường'),
(16, 2, N'Bánh chuối nướng', N'Bánh chuối nướng giòn tan thơm phức', N'Chuối, bột mì, trứng, đường, bơ'),
(16, 2, N'Chè chuối nướng', N'Chè chuối nướng ấm nóng ngọt ngào', N'Chuối nướng, nước cốt dừa, đường, bột năng'),
(16, 3, N'Chè bưởi', N'Chè bưởi thanh mát giải nhiệt', N'Bưởi, đường phèn, nước cốt dừa');
-- 6. HÌNH ẢNH MÓN ĂN - 128 ảnh (64 món x 2 ảnh)
INSERT INTO HinhAnhMonAn (maMonAn, duongDan, thuTu) VALUES
-- Mỗi món có 2 ảnh
(1, N'phobo.png', 1), (1, N'phobo.png', 2),
(2, N'phobo.png', 1), (2, N'phobo.png', 2),
(3, N'phobo.png', 1), (3, N'phobo.png', 2),
(4, N'phobo.png', 1), (4, N'phobo.png', 2),
(5, N'phobo.png', 1), (5, N'phobo.png', 2),
(6, N'phobo.png', 1), (6, N'phobo.png', 2),
(7, N'phobo.png', 1), (7, N'phobo.png', 2),
(8, N'phobo.png', 1), (8, N'phobo.png', 2),
(9, N'phobo.png', 1), (9, N'phobo.png', 2),
(10, N'phobo.png', 1), (10, N'phobo.png', 2),
(11, N'phobo.png', 1), (11, N'phobo.png', 2),
(12, N'phobo.png', 1), (12, N'phobo.png', 2),
(13, N'phobo.png', 1), (13, N'phobo.png', 2),
(14, N'phobo.png', 1), (14, N'phobo.png', 2),
(15, N'phobo.png', 1), (15, N'phobo.png', 2),
(16, N'phobo.png', 1), (16, N'phobo.png', 2),
(17, N'phobo.png', 1), (17, N'phobo.png', 2),
(18, N'phobo.png', 1), (18, N'phobo.png', 2),
(19, N'phobo.png', 1), (19, N'phobo.png', 2),
(20, N'phobo.png', 1), (20, N'phobo.png', 2),
(21, N'phobo.png', 1), (21, N'phobo.png', 2),
(22, N'phobo.png', 1), (22, N'phobo.png', 2),
(23, N'phobo.png', 1), (23, N'phobo.png', 2),
(24, N'phobo.png', 1), (24, N'phobo.png', 2),
(25, N'phobo.png', 1), (25, N'phobo.png', 2),
(26, N'phobo.png', 1), (26, N'phobo.png', 2),
(27, N'phobo.png', 1), (27, N'phobo.png', 2),
(28, N'phobo.png', 1), (28, N'phobo.png', 2),
(29, N'phobo.png', 1), (29, N'phobo.png', 2),
(30, N'phobo.png', 1), (30, N'phobo.png', 2),
(31, N'phobo.png', 1), (31, N'phobo.png', 2),
(32, N'phobo.png', 1), (32, N'phobo.png', 2),
(33, N'phobo.png', 1), (33, N'phobo.png', 2),
(34, N'phobo.png', 1), (34, N'phobo.png', 2),
(35, N'phobo.png', 1), (35, N'phobo.png', 2),
(36, N'phobo.png', 1), (36, N'phobo.png', 2),
(37, N'phobo.png', 1), (37, N'phobo.png', 2),
(38, N'phobo.png', 1), (38, N'phobo.png', 2),
(39, N'phobo.png', 1), (39, N'phobo.png', 2),
(40, N'phobo.png', 1), (40, N'phobo.png', 2),
(41, N'phobo.png', 1), (41, N'phobo.png', 2),
(42, N'phobo.png', 1), (42, N'phobo.png', 2),
(43, N'phobo.png', 1), (43, N'phobo.png', 2),
(44, N'phobo.png', 1), (44, N'phobo.png', 2),
(45, N'phobo.png', 1), (45, N'phobo.png', 2),
(46, N'phobo.png', 1), (46, N'phobo.png', 2),
(47, N'phobo.png', 1), (47, N'phobo.png', 2),
(48, N'phobo.png', 1), (48, N'phobo.png', 2),
(49, N'phobo.png', 1), (49, N'phobo.png', 2),
(50, N'phobo.png', 1), (50, N'phobo.png', 2),
(51, N'phobo.png', 1), (51, N'phobo.png', 2),
(52, N'phobo.png', 1), (52, N'phobo.png', 2),
(53, N'phobo.png', 1), (53, N'phobo.png', 2),
(54, N'phobo.png', 1), (54, N'phobo.png', 2),
(55, N'phobo.png', 1), (55, N'phobo.png', 2),
(56, N'phobo.png', 1), (56, N'phobo.png', 2),
(57, N'phobo.png', 1), (57, N'phobo.png', 2),
(58, N'phobo.png', 1), (58, N'phobo.png', 2),
(59, N'phobo.png', 1), (59, N'phobo.png', 2),
(60, N'phobo.png', 1), (60, N'phobo.png', 2),
(61, N'phobo.png', 1), (61, N'phobo.png', 2),
(62, N'phobo.png', 1), (62, N'phobo.png', 2),
(63, N'phobo.png', 1), (63, N'phobo.png', 2),
(64, N'phobo.png', 1), (64, N'phobo.png', 2);

-- 7. HÌNH ẢNH LỚP HỌC - 48 ảnh (16 lớp x 3 ảnh)
INSERT INTO HinhAnhLopHoc (maLopHoc, duongDan, thuTu) VALUES
-- Mỗi lớp có 3 ảnh
(1, N'phobo.png', 1), (1, N'phobo.png', 2), (1, N'phobo.png', 3),
(2, N'phobo.png', 1), (2, N'phobo.png', 2), (2, N'phobo.png', 3),
(3, N'phobo.png', 1), (3, N'phobo.png', 2), (3, N'phobo.png', 3),
(4, N'phobo.png', 1), (4, N'phobo.png', 2), (4, N'phobo.png', 3),
(5, N'phobo.png', 1), (5, N'phobo.png', 2), (5, N'phobo.png', 3),
(6, N'phobo.png', 1), (6, N'phobo.png', 2), (6, N'phobo.png', 3),
(7, N'phobo.png', 1), (7, N'phobo.png', 2), (7, N'phobo.png', 3),
(8, N'phobo.png', 1), (8, N'phobo.png', 2), (8, N'phobo.png', 3),
(9, N'phobo.png', 1), (9, N'phobo.png', 2), (9, N'phobo.png', 3),
(10, N'phobo.png', 1), (10, N'phobo.png', 2), (10, N'phobo.png', 3),
(11, N'phobo.png', 1), (11, N'phobo.png', 2), (11, N'phobo.png', 3),
(12, N'phobo.png', 1), (12, N'phobo.png', 2), (12, N'phobo.png', 3),
(13, N'phobo.png', 1), (13, N'phobo.png', 2), (13, N'phobo.png', 3),
(14, N'phobo.png', 1), (14, N'phobo.png', 2), (14, N'phobo.png', 3),
(15, N'phobo.png', 1), (15, N'phobo.png', 2), (15, N'phobo.png', 3),
(16, N'phobo.png', 1), (16, N'phobo.png', 2), (16, N'phobo.png', 3);



PRINT N'✓ Đã insert data mẫu thành công!';
PRINT N'- 6 người dùng (1 admin, 2 giáo viên, 3 học viên)';
PRINT N'- 2 giáo viên với lịch sử kinh nghiệm chi tiết (cột lichSuKinhNghiem)';
PRINT N'- 4 lớp học với cột giaTriSauBuoiHoc';
PRINT N'- 6 món ăn với slideshow (bảng HinhAnhMonAn)';
PRINT N'- 4 đặt lịch';
PRINT N'- 6 đánh giá (trigger sẽ tự động tính soLuongDanhGia và saoTrungBinh)';
PRINT N'- 3 ưu đãi';
PRINT N'';
PRINT N'✓ Trigger tự động cập nhật đánh giá đã được tạo!';
PRINT N'  - soLuongDanhGia: Tự động đếm số lượng đánh giá';
PRINT N'  - saoTrungBinh: Tự động tính trung bình điểm';
PRINT N'';
PRINT N'✓ Bảng GiaoVien đã có cột lichSuKinhNghiem!';
PRINT N'  - Lưu trữ lịch sử làm việc chi tiết theo từng giai đoạn';
PRINT N'  - Format: Năm - Vị trí - Nơi làm việc - Mô tả công việc';
GO
select * from MonAn
select * from HinhAnhMonAn
select * from LopHoc
select * from UuDai
select * from NguoiDung