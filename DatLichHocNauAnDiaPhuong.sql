---------------------------------------------------------------------
-- PHẦN 1: KHỞI TẠO DATABASE
---------------------------------------------------------------------
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

---------------------------------------------------------------------
-- PHẦN 2: TẠO CẤU TRÚC BẢNG (ĐÃ TỐI ƯU)
---------------------------------------------------------------------

-- 1. NGƯỜI DÙNG
CREATE TABLE NguoiDung (
    maNguoiDung INT PRIMARY KEY IDENTITY(1,1),
    tenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    hoTen NVARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    soDienThoai VARCHAR(15),
    gioiTinh NVARCHAR(10) CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')) DEFAULT N'Nữ',
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
    lichSuKinhNghiem NVARCHAR(MAX),
    moTa NVARCHAR(MAX),
    hinhAnh VARCHAR(255),
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
);

-- 4. KHOA HỌC (Chứa nội dung món ăn)
CREATE TABLE KhoaHoc (
    maKhoaHoc INT PRIMARY KEY IDENTITY(1,1),
    tenKhoaHoc NVARCHAR(200) NOT NULL,
    moTa NVARCHAR(500),
    gioiThieu NVARCHAR(MAX),
    giaTriSauBuoiHoc NVARCHAR(MAX),
    giaTien DECIMAL(10,2) NOT NULL,
    hinhAnh VARCHAR(255), -- Ảnh banner khóa học
    
    soLuongDanhGia INT DEFAULT 0,
    saoTrungBinh FLOAT DEFAULT 0,
    coUuDai BIT DEFAULT 0,
    ngayTao DATETIME DEFAULT GETDATE()
);

-- 5. LỊCH TRÌNH LỚP HỌC (Chứa thời gian & phòng)
CREATE TABLE LichTrinhLopHoc (
    maLichTrinh INT PRIMARY KEY IDENTITY(1,1),
    maKhoaHoc INT NOT NULL,
    maGiaoVien INT NOT NULL,
    
    thuTrongTuan VARCHAR(50), 
    gioBatDau TIME(0),
    gioKetThuc TIME(0),
    
    diaDiem NVARCHAR(255),
    soLuongToiDa INT DEFAULT 20,
    trangThai BIT DEFAULT 1,
    
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc),
    FOREIGN KEY (maGiaoVien) REFERENCES GiaoVien(maGiaoVien)
);

-- 6. DANH MỤC MÓN ĂN
CREATE TABLE DanhMucMonAn (
    maDanhMuc INT PRIMARY KEY IDENTITY(1,1),
    tenDanhMuc NVARCHAR(100) NOT NULL,
    iconDanhMuc VARCHAR(255),
    thuTu INT DEFAULT 1
);

-- 7. MÓN ĂN
CREATE TABLE MonAn (
    maMonAn INT PRIMARY KEY IDENTITY(1,1),
    maKhoaHoc INT NOT NULL,
    maDanhMuc INT NOT NULL,
    tenMon NVARCHAR(200) NOT NULL,
    gioiThieu NVARCHAR(MAX),
    nguyenLieu NVARCHAR(MAX),
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucMonAn(maDanhMuc),
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc)
);

-- 8. HÌNH ẢNH MÓN ĂN (Slide ảnh chi tiết cho từng món)
CREATE TABLE HinhAnhMonAn (
    maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
    maMonAn INT NOT NULL,
    duongDan VARCHAR(255) NOT NULL,
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn) ON DELETE CASCADE
);

-- 9. HÌNH ẢNH KHÓA HỌC (Slide ảnh cho khóa học)
CREATE TABLE HinhAnhKhoaHoc (
    maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
    maKhoaHoc INT NOT NULL,
    duongDan VARCHAR(255) NOT NULL,
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc)
);

-- 10. ĐẶT LỊCH
CREATE TABLE DatLich (
    maDatLich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maLichTrinh INT NOT NULL,
    ngayThamGia DATE NOT NULL,
    
    soLuongNguoi INT DEFAULT 1,
    tongTien DECIMAL(10,2),
    tenNguoiDat NVARCHAR(100),
    emailNguoiDat VARCHAR(100),
    sdtNguoiDat VARCHAR(15),
    ngayDat DATETIME DEFAULT GETDATE(),
    trangThai NVARCHAR(30) DEFAULT N'Chờ Duyệt',
    ghiChu NVARCHAR(MAX),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maLichTrinh) REFERENCES LichTrinhLopHoc(maLichTrinh)
);

-- 11. THANH TOÁN
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

-- 12. ĐÁNH GIÁ
CREATE TABLE DanhGia (
    maDanhGia INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maKhoaHoc INT NOT NULL,
    diemDanhGia INT CHECK (diemDanhGia BETWEEN 1 AND 5),
    binhLuan NVARCHAR(MAX),
    ngayDanhGia DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc)
);

-- 13. THÔNG BÁO
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

-- 14. YÊU THÍCH
CREATE TABLE YeuThich (
    maYeuThich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maKhoaHoc INT NOT NULL,
    ngayThem DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc),
    CONSTRAINT UQ_YeuThich UNIQUE (maHocVien, maKhoaHoc)
);
-- 15. ƯU ĐÃI
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

-- 16. LỊCH SỬ ƯU ĐÃI
CREATE TABLE LichSuUuDai (
    maLichSu INT PRIMARY KEY IDENTITY(1,1),
    maUuDai INT NOT NULL,
    maDatLich INT NOT NULL,
    soTienGiam DECIMAL(10,2) NOT NULL,
    ngaySuDung DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maUuDai) REFERENCES UuDai(maUuDai),
    FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich)
);

-- 17. HÓA ĐƠN
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

---------------------------------------------------------------------
-- PHẦN 3: TRIGGER & PROCEDURE
---------------------------------------------------------------------
GO

-- Trigger: Thông báo đặt lịch
CREATE TRIGGER trg_ThongBaoDatLich
ON DatLich
AFTER INSERT
AS
BEGIN
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
    SELECT 
        i.maHocVien,
        N'Đặt lịch thành công',
        N'Bạn đã đặt lịch học lớp ' + kh.tenKhoaHoc + N' vào ngày ' + CONVERT(NVARCHAR, i.ngayThamGia, 103),
        N'DatLich'
    FROM inserted i
    JOIN LichTrinhLopHoc lt ON i.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc;
END;
GO

-- Trigger: Cập nhật đánh giá
CREATE TRIGGER trg_CapNhatDanhGiaKhoaHoc
ON DanhGia
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE KhoaHoc
    SET 
        soLuongDanhGia = (SELECT COUNT(*) FROM DanhGia WHERE maKhoaHoc = KhoaHoc.maKhoaHoc),
        saoTrungBinh = ISNULL((SELECT AVG(CAST(diemDanhGia AS FLOAT)) FROM DanhGia WHERE maKhoaHoc = KhoaHoc.maKhoaHoc), 0)
    WHERE maKhoaHoc IN (SELECT DISTINCT maKhoaHoc FROM inserted)
       OR maKhoaHoc IN (SELECT DISTINCT maKhoaHoc FROM deleted);
END;
GO

-- Trigger: Cập nhật ưu đãi
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

-- Trigger: OTP hết hạn
CREATE TRIGGER trg_OTP_HetHan
ON OTP
INSTEAD OF INSERT
AS
BEGIN
    INSERT INTO OTP (maNguoiDung, maXacThuc, loaiOTP, thoiGianTao, thoiGianHetHan, daSuDung)
    SELECT maNguoiDung, maXacThuc, loaiOTP, GETDATE(), DATEADD(MINUTE, 5, GETDATE()), daSuDung
    FROM inserted;
END;
GO

-- Procedure: Lấy danh sách lớp theo ngày (API Android gọi cái này)
CREATE PROCEDURE sp_LayDanhSachLopTheoNgay
    @NgayCanXem DATE
AS
BEGIN
    DECLARE @ThuCuaNgay INT = DATEPART(DW, @NgayCanXem);
    
    SELECT 
        k.maKhoaHoc,
        k.tenKhoaHoc,
        k.hinhAnh,
        k.giaTien,
        k.saoTrungBinh,
        k.soLuongDanhGia,
        lt.maLichTrinh,
        lt.gioBatDau,
        lt.gioKetThuc,
        lt.diaDiem,
        lt.soLuongToiDa AS TongCho,
        ISNULL(SUM(d.soLuongNguoi), 0) AS DaDat,
        (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) AS ConTrong,
        CASE 
            WHEN (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) <= 0 THEN N'Hết Chỗ'
            ELSE N'Còn Nhận'
        END AS TrangThaiHienThi
    FROM LichTrinhLopHoc lt
    JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
    LEFT JOIN DatLich d ON lt.maLichTrinh = d.maLichTrinh 
                        AND d.ngayThamGia = @NgayCanXem 
                        AND d.trangThai <> N'Đã Hủy'
    WHERE 
        (
            (@ThuCuaNgay = 1 AND CHARINDEX('CN', lt.thuTrongTuan) > 0)
            OR
            (@ThuCuaNgay = 1 AND CHARINDEX('1', lt.thuTrongTuan) > 0)
            OR
            CHARINDEX(CAST(@ThuCuaNgay AS VARCHAR), lt.thuTrongTuan) > 0
        )
        AND lt.trangThai = 1
    GROUP BY 
        k.maKhoaHoc, k.tenKhoaHoc, k.hinhAnh, k.giaTien, k.saoTrungBinh, k.soLuongDanhGia,
        lt.maLichTrinh, lt.gioBatDau, lt.gioKetThuc, lt.diaDiem, lt.soLuongToiDa;
END;
GO

-- Procedure: Kiểm tra chỗ trống cho lớp đang chọn ngày (API Android gọi)
USE DatLichHocNauAn;
GO

-- Xóa procedure cũ nếu tồn tại để tạo mới
IF OBJECT_ID('sp_LayDanhSachLopTheoNgay', 'P') IS NOT NULL
    DROP PROCEDURE sp_LayDanhSachLopTheoNgay;
GO

CREATE PROCEDURE sp_LayDanhSachLopTheoNgay
    @NgayCanXem DATE -- Android gửi ngày vào đây (VD: '2025-12-24')
AS
BEGIN
    -- 1. Xác định thứ của ngày khách chọn (SQL Server mặc định: 1=Chủ Nhật, 2=Thứ 2,...)
    DECLARE @ThuCuaNgay INT = DATEPART(DW, @NgayCanXem);
    
    SELECT 
        k.maKhoaHoc,
        k.tenKhoaHoc,
        k.hinhAnh,
        k.giaTien,
        k.saoTrungBinh, -- Để hiển thị đánh giá sao
        k.soLuongDanhGia,
        
        lt.maLichTrinh,
        lt.gioBatDau,
        lt.gioKetThuc,
        lt.diaDiem,
        lt.soLuongToiDa AS TongCho,
        
        -- Tổng số người đã đặt trong ngày hôm đó (Bỏ qua đơn đã hủy)
        ISNULL(SUM(d.soLuongNguoi), 0) AS DaDat,
        
        -- Tính số chỗ còn trống = Tổng chỗ - Tổng số người đã đặt
        (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) AS ConTrong,
        
        -- Trả về trạng thái text để Android dễ hiển thị màu sắc
        CASE 
            WHEN (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) <= 0 THEN N'Hết Chỗ'
            ELSE N'Còn Nhận'
        END AS TrangThaiHienThi

    FROM LichTrinhLopHoc lt
    JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
    
    -- LEFT JOIN quan trọng: Chỉ join những đơn đặt lịch TRÙNG NGÀY KHÁCH CHỌN
    LEFT JOIN DatLich d ON lt.maLichTrinh = d.maLichTrinh 
                        AND d.ngayThamGia = @NgayCanXem 
                        AND d.trangThai <> N'Đã Hủy'
    WHERE 
        (
            -- Kiểm tra xem thứ của ngày chọn có nằm trong lịch học không
            -- Nếu là Chủ Nhật (1), tìm chuỗi 'CN' hoặc '1'
            (@ThuCuaNgay = 1 AND (CHARINDEX('CN', lt.thuTrongTuan) > 0 OR CHARINDEX('1', lt.thuTrongTuan) > 0))
            OR
            -- Các ngày thường: Tìm ký tự số (2,3,4,5,6,7)
            CHARINDEX(CAST(@ThuCuaNgay AS VARCHAR), lt.thuTrongTuan) > 0
        )
        AND lt.trangThai = 1 -- Chỉ lấy các lớp đang hoạt động
        
    GROUP BY 
        k.maKhoaHoc, k.tenKhoaHoc, k.hinhAnh, k.giaTien, k.saoTrungBinh, k.soLuongDanhGia,
        lt.maLichTrinh, lt.gioBatDau, lt.gioKetThuc, lt.diaDiem, lt.soLuongToiDa;
END;
GO

-- Procedure: Kiểm tra số chỗ trống cho một lịch trình cụ thể
CREATE PROCEDURE sp_KiemTraChoTrong
    @MaLichTrinh INT,
    @NgayThamGia DATE
AS
BEGIN
    SELECT 
        lt.maLichTrinh,
        k.maKhoaHoc,
        k.tenKhoaHoc,
        lt.soLuongToiDa AS TongCho,
        ISNULL(SUM(d.soLuongNguoi), 0) AS DaDat,
        (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) AS ConTrong,
        CASE 
            WHEN (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) <= 0 THEN N'Hết Chỗ'
            WHEN (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) <= 5 THEN N'Sắp Hết'
            ELSE N'Còn Nhiều'
        END AS TrangThai
    FROM LichTrinhLopHoc lt
    JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
    LEFT JOIN DatLich d ON lt.maLichTrinh = d.maLichTrinh 
                        AND d.ngayThamGia = @NgayThamGia
                        AND d.trangThai <> N'Đã Hủy'
    WHERE lt.maLichTrinh = @MaLichTrinh
    GROUP BY 
        lt.maLichTrinh, k.maKhoaHoc, k.tenKhoaHoc, lt.soLuongToiDa;
END;
GO

---------------------------------------------------------------------
-- PHẦN 4: INSERT DATA (DỮ LIỆU ĐẦY ĐỦ TỪ FILE GỐC)
---------------------------------------------------------------------

-- 1. NGƯỜI DÙNG (Kèm giới tính)
INSERT INTO NguoiDung (tenDangNhap, matKhau, hoTen, email, soDienThoai, gioiTinh, diaChi, vaiTro, trangThai) VALUES
(N'admin', N'admin123', N'Quản Trị Viên', N'admin@localcooking.vn', N'0901234567', N'Nam', N'123 Nguyễn Huệ, Q1, TP.HCM', N'Admin', N'HoatDong'),
(N'VanAn', N'gv123', N'Nguyễn Văn An', N'nguyenvanan@gmail.com', N'0912345678', N'Nam', N'456 Lê Lợi, Q1, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'ThiBinh', N'gv123', N'Trần Thị Bình', N'tranthibinh@gmail.com', N'0923456789', N'Nữ', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'ThaoVy', N'hv123', N'Ngô Thị Thảo Vy', N'thaovyn0312@gmail.com', N'0934567890', N'Nữ', N'321 Võ Văn Tần, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'TrieuTien', N'hv123', N'Nguyễn Triều Tiên', N'nguyentrieutien2005py@gmail.com', N'0945678901', N'Nam', N'654 Hai Bà Trưng, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'ThiThuong', N'hv123', N'Nguyễn Thị Thương', N'nguyenthithuong15112005@gmail.com', N'0956789012', N'Nữ', N'987 Cách Mạng Tháng 8, Q10, TP.HCM', N'HocVien', N'HoatDong');

-- 2. GIÁO VIÊN
INSERT INTO GiaoVien (maNguoiDung, chuyenMon, kinhNghiem, lichSuKinhNghiem, moTa, hinhAnh) VALUES
(2, 
 N'Ẩm thực Việt Nam truyền thống', 
 N'20 năm kinh nghiệm', 
 N'• 2005-2008: Bếp phó tại Nhà hàng Cung Đình Huế, Huế - Chuyên về các món ăn cung đình truyền thống

• 2008-2012: Bếp trưởng tại Khách sạn Saigon Morin, Huế - Phụ trách bộ phận ẩm thực Việt Nam, phục vụ khách quốc tế

• 2012-2015: Sous Chef tại Nhà hàng Món Huế, Hà Nội - Giới thiệu ẩm thực Huế đến thực khách miền Bắc

• 2015-2018: Executive Chef tại Resort Angsana Lăng Cô - Phát triển thực đơn fusion kết hợp ẩm thực Việt và quốc tế

• 2018-2020: Giảng viên thỉnh giảng tại Trường Cao đẳng Du lịch Huế - Giảng dạy môn Ẩm thực Việt Nam

• 2020-nay: Chuyên gia ẩm thực độc lập, tổ chức các lớp dạy nấu ăn và tư vấn thực đơn cho nhà hàng',
 N'Chuyên gia về các món ăn Việt Nam cổ truyền, đặc biệt là món Huế và Hà Nội. Từng đạt giải Nhất cuộc thi "Vua đầu bếp Việt Nam 2016" hạng mục Ẩm thực truyền thống. Có kinh nghiệm đào tạo hơn 500 học viên về nghề bếp.', 
 N'giaovien2.jpg'),
(3, 
 N'Bánh ngọt và tráng miệng', 
 N'10 năm kinh nghiệm', 
 N'• 2010-2013: Học viên tại Le Cordon Bleu Paris, Pháp - Tốt nghiệp loại Xuất sắc chuyên ngành Pâtisserie

• 2013-2015: Pastry Chef tại Pâtisserie Ladurée, Paris - Chuyên làm macaron và các loại bánh Pháp cao cấp

• 2015-2017: Head Pastry Chef tại Khách sạn Park Hyatt Saigon - Phụ trách bộ phận bánh ngọt, phục vụ tiệc cưới và sự kiện

• 2017-2019: Pastry Chef tại Nhà hàng L''Usine, TP.HCM - Sáng tạo thực đơn bánh ngọt theo mùa

• 2019-2021: Mở cửa hàng bánh riêng "Sweet Dreams Bakery" - Chuyên bánh Pháp và bánh sinh nhật cao cấp

• 2021-nay: Giảng viên dạy làm bánh và tổ chức workshop, chia sẻ kiến thức về nghệ thuật làm bánh Pháp',
 N'Chuyên về bánh Pháp, bánh Âu và các món tráng miệng hiện đại. Từng đoạt huy chương Vàng tại cuộc thi "Pastry Chef of the Year 2018" khu vực Đông Nam Á. Đam mê sáng tạo các món bánh kết hợp hương vị Á - Âu.', 
 N'giaovien1.png');

-- 3. KHÓA HỌC (Nội dung lớp học)
INSERT INTO KhoaHoc (tenKhoaHoc, moTa, gioiThieu, giaTriSauBuoiHoc, giaTien, hinhAnh, coUuDai) VALUES
-- HÀ NỘI
(N'Ẩm thực phố cổ Hà Nội', N'Khám phá hương vị đặc trưng của ẩm thực phố cổ với phở, bún chả, chả cá', N'Trải nghiệm nấu các món ăn đường phố nổi tiếng nhất Hà Nội', N'• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc
• Hiểu về văn hóa ẩm thực phố cổ
• Tự tin làm bún chả và chả cá Lã Vọng', 650000, N'phobo.png', 1),
(N'Bún và miến Hà Nội', N'Học cách làm các món bún đặc sản: bún thang, bún ốc, bún riêu', N'Khóa học chuyên sâu về các món bún truyền thống Hà Nội', N'• Làm chủ nghệ thuật nấu nước dùng trong
• Kỹ thuật chế biến ốc và cua đồng
• Bí quyết làm bún thang chuẩn vị', 580000, N'phobo.png', 0),
(N'Bánh dân gian Hà Nội', N'Học làm bánh cuốn, bánh đúc, bánh khúc - đặc sản làng nghề', N'Khám phá nghệ thuật làm bánh truyền thống của người Hà Nội', N'• Kỹ thuật tráng bánh cuốn mỏng như giấy
• Bí quyết làm nhân thơm ngon
• Hiểu về nguồn gốc các loại bánh', 520000, N'phobo.png', 1),
(N'Món nhậu Hà Nội', N'Các món nhậu đặc trưng: nem chua rán, chả rươi, bún đậu mắm tôm', N'Trải nghiệm văn hóa nhậu nhẹt đặc trưng của người Hà Nội', N'• Làm nem chua rán giòn tan
• Kỹ thuật chế biến rươi đồng
• Pha chế mắm tôm chuẩn vị', 600000, N'phobo.png', 0),
-- HUẾ
(N'Ẩm thực cung đình Huế', N'Khám phá tinh hoa ẩm thực hoàng gia với bún bò, cơm hến, bánh bèo', N'Trải nghiệm nấu các món ăn cung đình tinh tế và cầu kỳ', N'• Nắm vững kỹ thuật nấu bún bò Huế chính gốc
• Hiểu về văn hóa ẩm thực cung đình
• Làm chủ nghệ thuật trang trí món ăn', 715000, N'phobo.png', 1),
(N'Bánh Huế truyền thống', N'Học làm bánh bèo, bánh nậm, bánh lọc - đặc sản xứ Huế', N'Khóa học chuyên sâu về các loại bánh đặc trưng Huế', N'• Kỹ thuật tráng bánh bèo mịn màng
• Bí quyết gói bánh nậm đẹp mắt
• Làm bánh lọc trong vắt', 550000, N'phobo.png', 0),
(N'Món chay Huế', N'Ẩm thực chay tinh tế: bún chay, bánh bột lọc chay, chè sen', N'Khám phá nghệ thuật nấu món chay theo phong cách Huế', N'• Nấu món chay đậm đà không thua món mặn
• Kỹ thuật chế biến rau củ sáng tạo
• Làm nước dùng chay thơm ngon', 480000, N'phobo.png', 1),
(N'Chè và tráng miệng Huế', N'Học làm chè Huế, bánh ít, bánh ram - món ngọt đặc sản', N'Trải nghiệm làm các món tráng miệng truyền thống Huế', N'• Nấu chè Huế thanh mát
• Làm bánh ít nhân đậu xanh
• Kỹ thuật ram bánh giòn rụm', 520000, N'phobo.png', 0),
-- ĐÀ NẴNG
(N'Hải sản Đà Nẵng', N'Chế biến hải sản tươi sống: mì Quảng, bánh tráng cuốn thịt heo', N'Học cách chế biến hải sản theo phong cách miền Trung', N'• Kỹ thuật chọn và sơ chế hải sản tươi
• Nấu mì Quảng đậm đà
• Làm bánh tráng cuốn đặc biệt', 680000, N'phobo.png', 1),
(N'Bánh xèo và nem lụi Đà Nẵng', N'Học làm bánh xèo giòn tan và nem lụi thơm lừng', N'Khám phá món ăn đường phố nổi tiếng Đà Nẵng', N'• Bí quyết làm bánh xèo giòn rụm
• Kỹ thuật nướng nem lụi thơm phức
• Pha chế nước chấm chuẩn vị', 590000, N'phobo.png', 0),
(N'Bún mắm và bún cá Đà Nẵng', N'Nấu bún mắm nêm đậm đà và bún cá ngọt thanh', N'Học cách nấu các món bún đặc trưng miền Trung', N'• Nấu nước mắm nêm thơm ngon
• Kỹ thuật làm chả cá tươi
• Bí quyết nấu nước dùng ngọt', 620000, N'phobo.png', 1),
(N'Bánh canh và cao lầu', N'Học làm bánh canh cua và cao lầu Hội An', N'Khám phá món ăn đặc sản Đà Nẵng - Hội An', N'• Làm bánh canh dai ngon
• Nấu nước dùng cua đậm đà
• Kỹ thuật làm cao lầu truyền thống', 650000, N'phobo.png', 0),
-- CẦN THƠ
(N'Ẩm thực miệt vườn Cần Thơ', N'Món ăn đồng quê: lẩu mắm, cá lóc nướng trui, gỏi cá', N'Trải nghiệm nấu các món ăn đặc trưng miệt vườn', N'• Nấu lẩu mắm chuẩn vị miền Tây
• Kỹ thuật nướng cá lóc thơm phức
• Làm gỏi cá tươi ngon', 580000, N'phobo.png', 1),
(N'Bánh và bún miền Tây', N'Học làm bánh xèo, bánh khọt, bún riêu cua đồng', N'Khám phá món ăn dân dã miền sông nước', N'• Làm bánh xèo miền Tây to và giòn
• Kỹ thuật làm bánh khọt nhỏ xinh
• Nấu bún riêu cua đồng đậm đà', 550000, N'phobo.png', 0),
(N'Hủ tiếu và hủ tiếu Nam Vang', N'Nấu hủ tiếu Sa Đéc và hủ tiếu Nam Vang', N'Học cách nấu món hủ tiếu đặc trưng miền Tây', N'• Nấu nước dùng hủ tiếu ngọt thanh
• Kỹ thuật chế biến sườn, gan, tim
• Bí quyết làm hủ tiếu Nam Vang', 600000, N'phobo.png', 1),
(N'Chè và tráng miệng miền Tây', N'Học làm chè chuối, chè đậu xanh, bánh chuối nướng', N'Trải nghiệm làm món ngọt dân dã miền sông nước', N'• Nấu chè chuối thơm ngon
• Làm chè đậu xanh mát lạnh
• Kỹ thuật nướng bánh chuối', 480000, N'phobo.png', 0);

-- 4. LỊCH TRÌNH LỚP HỌC (ĐÃ CHỈNH GIỜ)
INSERT INTO LichTrinhLopHoc (maKhoaHoc, maGiaoVien, thuTrongTuan, gioBatDau, gioKetThuc, diaDiem, soLuongToiDa) VALUES
-- HÀ NỘI
(1, 1, '2,3,4,5,6,7,CN', '17:30', '20:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 20),
(2, 1, '2,4,6',          '08:30', '11:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 18),
(3, 2, '3,5,7',          '08:30', '11:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 15),
(4, 1, '7,CN',            '14:00', '17:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 20),
-- HUẾ
(5, 1, '2,3,4,5,6,7,CN', '17:30', '20:30', N'23 Lê Duẩn, Huế', 20),
(6, 2, '2,4,6',          '08:30', '11:30', N'23 Lê Duẩn, Huế', 18),
(7, 1, '3,5,7',          '08:30', '11:30', N'23 Lê Duẩn, Huế', 15),
(8, 2, '7,CN',            '14:00', '17:00', N'23 Lê Duẩn, Huế', 15),
-- ĐÀ NẴNG
(9, 1, '2,3,4,5,6,7,CN', '17:30', '20:30', N'78 Trần Phú, Đà Nẵng', 20),
(10, 2, '2,4,6',         '08:30', '11:30', N'78 Trần Phú, Đà Nẵng', 18),
(11, 1, '3,5,7',         '08:30', '11:30', N'78 Trần Phú, Đà Nẵng', 20),
(12, 1, '7,CN',           '14:00', '17:00', N'78 Trần Phú, Đà Nẵng', 18),
-- CẦN THƠ
(13, 1, '2,3,4,5,6,7,CN', '17:30', '20:30', N'56 Mậu Thân, Cần Thơ', 20),
(14, 2, '2,4,6',          '08:30', '11:30', N'56 Mậu Thân, Cần Thơ', 18),
(15, 1, '3,5,7',          '08:30', '11:30', N'56 Mậu Thân, Cần Thơ', 20),
(16, 2, '7,CN',            '14:00', '17:00', N'56 Mậu Thân, Cần Thơ', 15);

-- 5. DANH MỤC
INSERT INTO DanhMucMonAn (tenDanhMuc, iconDanhMuc, thuTu) VALUES
(N'Món khai vị', N'ic_appetizer.png', 1),
(N'Món chính', N'ic_main_dish.png', 2),
(N'Món tráng miệng', N'ic_dessert.png', 3);

-- 6. MÓN ĂN (64 MÓN)
INSERT INTO MonAn (maKhoaHoc, maDanhMuc, tenMon, gioiThieu, nguyenLieu) VALUES
-- === LỚP 1 ===
(1, 1, N'Nem rán Hà Nội', N'Nem rán giòn rụm với nhân thịt, miến, mộc nhĩ thơm ngon', N'Bánh đa nem, thịt lợn, miến, mộc nhĩ, cà rốt, hành tây'),
(1, 2, N'Phở bò Hà Nội', N'Món phở truyền thống với nước dùng trong, thơm, thịt bò mềm', N'Bánh phở, thịt bò, xương bò, hành, gừng, gia vị'),
(1, 2, N'Bún chả Hà Nội', N'Đặc sản Hà Nội với chả nướng thơm lừng, bún tươi', N'Bún, thịt ba chỉ, thịt nạc, nước mắm, rau sống'),
(1, 3, N'Chè ba màu', N'Món tráng miệng mát lạnh với đậu đỏ, đậu xanh, thạch', N'Đậu đỏ, đậu xanh, thạch, nước cốt dừa, đá bào'),
-- === LỚP 2 ===
(2, 1, N'Gỏi cuốn Hà Nội', N'Gỏi cuốn tươi mát với tôm, thịt, bún và rau sống', N'Bánh tráng, tôm, thịt, bún, rau sống, nước chấm'),
(2, 2, N'Bún thang', N'Món bún tinh tế với nước dùng trong, trứng, giò lụa', N'Bún, gà, trứng, giò lụa, nấm hương, hành phi'),
(2, 2, N'Bún ốc', N'Bún ốc chua cay đặc trưng Hà Nội', N'Bún, ốc, cà chua, dấm, ớt, rau thơm'),
(2, 3, N'Chè hạt sen', N'Chè hạt sen thanh mát, bổ dưỡng', N'Hạt sen, long nhãn, đường phèn, nước cốt dừa'),
-- === LỚP 3 ===
(3, 1, N'Bánh cuốn', N'Bánh cuốn mỏng như giấy với nhân thịt, mộc nhĩ', N'Bột gạo, thịt lợn, mộc nhĩ, hành khô, nước mắm'),
(3, 2, N'Bánh đúc', N'Bánh đúc trắng mềm mịn với nước mắm chua ngọt', N'Bột gạo, tôm khô, mỡ hành, nước mắm, đường'),
(3, 2, N'Bánh khúc', N'Bánh khúc nhân đậu xanh thơm ngon', N'Gạo nếp, lá khúc, đậu xanh, thịt ba chỉ'),
(3, 3, N'Bánh trôi nước', N'Bánh trôi nhân đậu xanh trong nước gừng ngọt', N'Bột nếp, đậu xanh, gừng, đường'),
-- === LỚP 4 ===
(4, 1, N'Nem chua rán', N'Nem chua rán giòn tan, chua ngọt hấp dẫn', N'Nem chua, bột chiên giòn, dầu ăn'),
(4, 2, N'Chả rươi', N'Chả rươi thơm phức, đặc sản mùa thu Hà Nội', N'Rươi, thịt lợn, trứng, bì heo, lá chanh'),
(4, 2, N'Bún đậu mắm tôm', N'Bún đậu với mắm tôm đậm đà, chả cốm', N'Bún, đậu rán, chả cốm, mắm tôm, rau sống'),
(4, 3, N'Chè lam', N'Chè lam nếp thơm, ngọt dịu', N'Nếp than, đậu xanh, nước cốt dừa, đường'),
-- === LỚP 5 ===
(5, 1, N'Bánh bèo', N'Bánh bèo Huế mềm mịn với tôm khô, mỡ hành', N'Bột gạo, bột năng, tôm khô, mỡ hành, nước cốt dừa'),
(5, 2, N'Bún bò Huế', N'Món bún cay nồng đặc trưng xứ Huế', N'Bún, thịt bò, giò heo, sả, ớt, mắm ruốc'),
(5, 2, N'Cơm hến', N'Cơm hến Huế với hến xào thơm lừng', N'Cơm nguội, hến, đậu phộng, tóp mỡ, rau thơm'),
(5, 3, N'Chè Huế', N'Chè Huế thanh mát với nhiều loại hạt', N'Đậu đỏ, đậu xanh, hạt sen, thạch, nước cốt dừa'),
-- === LỚP 6 ===
(6, 1, N'Bánh nậm', N'Bánh nậm gói lá chuối với nhân tôm thịt', N'Bột gạo, tôm, thịt ba chỉ, lá chuối, nước mắm'),
(6, 2, N'Bánh lọc', N'Bánh lọc trong vắt với nhân tôm thịt', N'Bột năng, bột sắn, tôm, thịt ba chỉ, mộc nhĩ'),
(6, 2, N'Bánh ít', N'Bánh ít nhân đậu xanh thơm ngon', N'Bột gạo, đậu xanh, dừa nạo, đường'),
(6, 3, N'Bánh ram', N'Bánh ram giòn rụm với nhân đậu xanh', N'Bột gạo, đậu xanh, đường, dừa nạo'),
-- === LỚP 7 ===
(7, 1, N'Gỏi cuốn chay', N'Gỏi cuốn chay với rau củ tươi ngon', N'Bánh tráng, rau sống, bún, nấm, đậu hũ'),
(7, 2, N'Bún chay Huế', N'Bún chay với nước dùng thơm ngon', N'Bún, nấm, đậu hũ, rau củ, sả, ớt'),
(7, 2, N'Bánh bột lọc chay', N'Bánh bột lọc chay với nhân nấm', N'Bột năng, nấm, đậu hũ, mộc nhĩ'),
(7, 3, N'Chè sen', N'Chè sen thanh mát, bổ dưỡng', N'Hạt sen, long nhãn, đường phèn'),
-- === LỚP 8 ===
(8, 1, N'Bánh lọc lá', N'Bánh lọc gói lá chuối thơm ngon', N'Bột năng, tôm, thịt, lá chuối'),
(8, 2, N'Bánh ít lá gai', N'Bánh ít lá gai nhân đậu xanh', N'Bột nếp, lá gai, đậu xanh, đường'),
(8, 2, N'Bánh ram ít', N'Bánh ram ít giòn tan thơm ngon', N'Bột gạo, đậu xanh, dừa, đường'),
(8, 3, N'Chè bưởi', N'Chè bưởi thanh mát đặc sản Huế', N'Bưởi, đường phèn, nước cốt dừa'),
-- === LỚP 9 ===
(9, 1, N'Gỏi cá mai', N'Gỏi cá mai tươi ngon với rau thơm', N'Cá mai, rau thơm, đậu phộng, bánh tráng'),
(9, 2, N'Mì Quảng', N'Mì Quảng đặc sản với tôm, thịt, trứng', N'Mì Quảng, tôm, thịt, trứng, đậu phộng, rau sống'),
(9, 2, N'Bánh tráng cuốn thịt heo', N'Bánh tráng cuốn thịt heo nướng thơm lừng', N'Bánh tráng, thịt heo, rau sống, nước chấm'),
(9, 3, N'Chè bắp', N'Chè bắp ngọt thanh, mát lạnh', N'Bắp, đường, nước cốt dừa'),
-- === LỚP 10 ===
(10, 1, N'Nem lụi', N'Nem lụi nướng thơm phức đặc sản Đà Nẵng', N'Thịt lợn, mỡ, tỏi, hành, gia vị'),
(10, 2, N'Bánh xèo', N'Bánh xèo giòn tan với tôm, thịt, giá', N'Bột gạo, tôm, thịt, giá, trứng'),
(10, 2, N'Bánh tráng thịt heo', N'Bánh tráng cuốn thịt heo nướng', N'Bánh tráng, thịt heo, rau sống'),
(10, 3, N'Chè đậu xanh', N'Chè đậu xanh mát lạnh, bổ dưỡng', N'Đậu xanh, đường, nước cốt dừa'),
-- === LỚP 11 ===
(11, 1, N'Chả cá', N'Chả cá tươi ngon đặc sản miền Trung', N'Cá thu, bột năng, gia vị'),
(11, 2, N'Bún mắm nêm', N'Bún mắm nêm đậm đà hương vị miền Trung', N'Bún, mắm nêm, thịt heo, tôm, rau sống'),
(11, 2, N'Bún cá', N'Bún cá ngọt thanh với chả cá tươi', N'Bún, cá, chả cá, cà chua, rau thơm'),
(11, 3, N'Chè khoai môn', N'Chè khoai môn béo ngậy thơm ngon', N'Khoai môn, đường, nước cốt dừa'),
-- === LỚP 12 ===
(12, 1, N'Gỏi tôm', N'Gỏi tôm tươi ngon với rau thơm', N'Tôm, rau thơm, đậu phộng, nước mắm'),
(12, 2, N'Bánh canh cua', N'Bánh canh cua đậm đà hương vị biển', N'Bánh canh, cua, thịt heo, rau thơm'),
(12, 2, N'Cao lầu Hội An', N'Cao lầu đặc sản Hội An với sợi bánh dai', N'Bánh cao lầu, thịt heo, giá, rau thơm'),
(12, 3, N'Chè thái', N'Chè thái mát lạnh với nhiều loại trái cây', N'Thạch, trái cây, nước cốt dừa, đá bào'),
-- === LỚP 13 ===
(13, 1, N'Gỏi cá lóc', N'Gỏi cá lóc tươi ngon với rau thơm', N'Cá lóc, rau thơm, dừa nạo, đậu phộng'),
(13, 2, N'Lẩu mắm', N'Lẩu mắm đậm đà hương vị miền Tây', N'Cá lóc, tôm, rau muống, mắm, ớt'),
(13, 2, N'Cá lóc nướng trui', N'Cá lóc nướng trui thơm phức', N'Cá lóc, sả, ớt, muối, tiêu'),
(13, 3, N'Chè chuối', N'Chè chuối nóng thơm ngon', N'Chuối, đường, nước cốt dừa, bột năng'),
-- === LỚP 14 ===
(14, 1, N'Bánh khọt', N'Bánh khọt nhỏ xinh với tôm tươi', N'Bột gạo, tôm, dừa nạo, hành lá'),
(14, 2, N'Bánh xèo miền Tây', N'Bánh xèo to giòn rụm đặc sản miền Tây', N'Bột gạo, tôm, thịt, giá, trứng'),
(14, 2, N'Bún riêu cua đồng', N'Bún riêu cua đồng đậm đà', N'Bún, cua đồng, cà chua, đậu hũ, rau thơm'),
(14, 3, N'Chè đậu đỏ', N'Chè đậu đỏ ngọt thanh mát lạnh', N'Đậu đỏ, đường, nước cốt dừa'),
-- === LỚP 15 ===
(15, 1, N'Gỏi sứa', N'Gỏi sứa giòn ngon với rau thơm', N'Sứa, rau thơm, đậu phộng, nước mắm'),
(15, 2, N'Hủ tiếu Sa Đéc', N'Hủ tiếu Sa Đéc ngọt thanh đặc sản', N'Hủ tiếu, thịt heo, tôm, gan, tim'),
(15, 2, N'Hủ tiếu Nam Vang', N'Hủ tiếu Nam Vang đậm đà hương vị', N'Hủ tiếu, thịt heo, tôm, gan, tim, mực'),
(15, 3, N'Chè thập cẩm', N'Chè thập cẩm với nhiều loại đậu', N'Đậu đỏ, đậu xanh, thạch, nước cốt dừa'),
-- === LỚP 16 ===
(16, 1, N'Bánh chuối hấp', N'Bánh chuối hấp thơm ngon dân dã', N'Chuối, bột gạo, dừa nạo, đường'),
(16, 2, N'Bánh chuối nướng', N'Bánh chuối nướng giòn tan thơm phức', N'Chuối, bột mì, trứng, đường, bơ'),
(16, 2, N'Chè chuối nướng', N'Chè chuối nướng ấm nóng ngọt ngào', N'Chuối nướng, nước cốt dừa, đường, bột năng'),
(16, 3, N'Chè bưởi', N'Chè bưởi thanh mát giải nhiệt', N'Bưởi, đường phèn, nước cốt dừa');

-- 7. HÌNH ẢNH MÓN ĂN (128 dòng - Đầy đủ cho 64 món)
INSERT INTO HinhAnhMonAn (maMonAn, duongDan, thuTu) VALUES
(1, N'nem_ran_ha_noi_1.jpg', 1),				(1, N'nem_ran_ha_noi_2.jpg', 2),
(2, N'pho_bo_ha_noi_1.jpg', 1),					(2, N'pho_bo_ha_noi_2.jpg', 2),
(3, N'bun_cha_ha_noi_1.jpg', 1),				(3, N'bun_cha_ha_noi_2.jpg', 2),
(4, N'che_ba_mau_1.jpg', 1),					(4, N'che_ba_mau_2.jpg', 2),
(5, N'goi_cuon_ha_noi_1.jpg', 1),				(5, N'goi_cuon_ha_noi_2.jpg', 2),
(6, N'bun_thang_1.jpg', 1),						(6, N'bun_thang_2.jpg', 2),
(7, N'bun_oc_1.jpg', 1),						(7, N'bun_oc_2.jpg', 2),
(8, N'che_hat_sen_1.jpg', 1),					(8, N'che_hat_sen_2.jpg', 2),
(9, N'banh_cuon_1.jpg', 1),						(9, N'banh_cuon_2.jpg', 2),
(10, N'banh_duc_1.jpg', 1),						(10, N'banh_duc_2.jpg', 2),
(11, N'banh_khuc_1.jpg', 1),					(11, N'banh_khuc_2.jpg', 2),
(12, N'banh_troi_nuoc_1.jpg', 1),				(12, N'banh_troi_nuoc_2.jpg', 2),
(13, N'nem_chua_ran_1.jpg', 1),					(13, N'nem_chua_ran_2.jpg', 2),
(14, N'cha_ruoi_1.jpg', 1),						(14, N'cha_ruoi_2.jpg', 2),
(15, N'bun_dau_mam_tom_1.jpg', 1),				(15, N'bun_dau_mam_tom_2.jpg', 2),
(16, N'che_lam_1.jpg', 1),						(16, N'che_lam_2.jpg', 2),
(17, N'banh_beo_1.jpg', 1),						(17, N'banh_beo_2.jpg', 2),
(18, N'bun_bo_hue_1.jpg', 1),					(18, N'bun_bo_hue_2.jpg', 2),
(19, N'com_hen_1.jpg', 1),						(19, N'com_hen_2.jpg', 2),
(20, N'che_hue_1.jpg', 1),						(20, N'che_hue_2.jpg', 2),
(21, N'banh_nam_1.jpg', 1),						(21, N'banh_nam_2.jpg', 2),
(22, N'banh_loc_1.jpg', 1),						(22, N'banh_loc_2.jpg', 2),
(23, N'banh_it_1.jpg', 1),						(23, N'banh_it_2.jpg', 2),
(24, N'banh_ram_1.jpg', 1),						(24, N'banh_ram_2.jpg', 2),
(25, N'goi_cuon_chay_1.jpg', 1),				(25, N'goi_cuon_chay_2.jpg', 2),
(26, N'bun_chay_hue_1.jpg', 1),					(26, N'bun_chay_hue_2.jpg', 2),
(27, N'banh_bot_loc_chay_1.jpg', 1),			(27, N'banh_bot_loc_chay_2.jpg', 2),
(28, N'che_sen_1.jpg', 1),						(28, N'che_sen_2.jpg', 2),
(29, N'banh_loc_la_1.jpg', 1),					(29, N'banh_loc_la_2.jpg', 2),
(30, N'banh_it_la_gai_1.jpg', 1),				(30, N'banh_it_la_gai_2.jpg', 2),
(31, N'banh_ram_it_1.jpg', 1),					(31, N'banh_ram_it_2.jpg', 2),
(32, N'che_buoi_1.jpg', 1),						(32, N'che_buoi_2.jpg', 2),
(33, N'goi_ca_mai_1.jpg', 1),					(33, N'goi_ca_mai_2.jpg', 2),
(34, N'mi_quang_1.jpg', 1),						(34, N'mi_quang_2.jpg', 2),
(35, N'banh_trang_cuon_thit_heo_1.jpg', 1),		(35, N'banh_trang_cuon_thit_heo_2.jpg', 2),
(36, N'che_bap_1.jpg', 1),						(36, N'che_bap_2.jpg', 2),
(37, N'nem_lui_1.jpg', 1),						(37, N'nem_lui_2.jpg', 2),
(38, N'banh_xeo_1.jpg', 1),						(38, N'banh_xeo_2.jpg', 2),
(39, N'banh_trang_thit_heo_1.jpg', 1),			(39, N'banh_trang_thit_heo_2.jpg', 2),
(40, N'che_dau_xanh_1.jpg', 1),					(40, N'che_dau_xanh_2.jpg', 2),
(41, N'cha_ca_1.jpg', 1),						(41, N'cha_ca_2.jpg', 2),
(42, N'bun_mam_nem_1.jpg', 1),					(42, N'bun_mam_nem_2.jpg', 2),
(43, N'bun_ca_1.jpg', 1),						(43, N'bun_ca_2.jpg', 2),
(44, N'che_khoai_mon_1.jpg', 1),				(44, N'che_khoai_mon_2.jpg', 2),
(45, N'goi_tom_1.jpg', 1),						(45, N'goi_tom_2.jpg', 2),
(46, N'banh_canh_cua_1.jpg', 1),				(46, N'banh_canh_cua_2.jpg', 2),
(47, N'cao_lau_hoi_an_1.jpg', 1),				(47, N'cao_lau_hoi_an_2.jpg', 2),
(48, N'che_thai_1.jpg', 1),						(48, N'che_thai_2.jpg', 2),
(49, N'goi_ca_loc_1.jpg', 1),					(49, N'goi_ca_loc_2.jpg', 2),
(50, N'lau_mam_1.jpg', 1),						(50, N'lau_mam_2.jpg', 2),
(51, N'ca_loc_nuong_trui_1.jpg', 1),			(51, N'ca_loc_nuong_trui_2.jpg', 2),
(52, N'che_chuoi_1.jpg', 1),					(52, N'che_chuoi_2.jpg', 2),
(53, N'banh_khot_1.jpg', 1),					(53, N'banh_khot_2.jpg', 2),
(54, N'banh_xeo_mien_tay_1.jpg', 1),			(54, N'banh_xeo_mien_tay_2.jpg', 2),
(55, N'bun_rieu_cua_dong_1.jpg', 1),			(55, N'bun_rieu_cua_dong_2.jpg', 2),
(56, N'che_dau_do_1.jpg', 1),					(56, N'che_dau_do_2.jpg', 2),
(57, N'goi_sua_1.jpg', 1),						(57, N'goi_sua_2.jpg', 2),
(58, N'hu_tieu_sa_dec_1.jpg', 1),				(58, N'hu_tieu_sa_dec_2.jpg', 2),
(59, N'hu_tieu_nam_vang_1.jpg', 1),				(59, N'hu_tieu_nam_vang_2.jpg', 2),
(60, N'che_thap_cam_1.jpg', 1),					(60, N'che_thap_cam_2.jpg', 2),
(61, N'banh_chuoi_hap_1.jpg', 1),				(61, N'banh_chuoi_hap_2.jpg', 2),
(62, N'banh_chuoi_nuong_1.jpg', 1),				(62, N'banh_chuoi_nuong_2.jpg', 2),
(63, N'che_chuoi_nuong_1.jpg', 1),				(63, N'che_chuoi_nuong_2.jpg', 2),
(64, N'che_buoi_1.jpg', 1),						(64, N'che_buoi_2.jpg', 2);

-- 8. HÌNH ẢNH KHÓA HỌC (48 dòng)
INSERT INTO HinhAnhKhoaHoc (maKhoaHoc, duongDan, thuTu) VALUES
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

-- 9. ĐẶT LỊCH
INSERT INTO DatLich (maHocVien, maLichTrinh, ngayThamGia, soLuongNguoi, tongTien, tenNguoiDat, emailNguoiDat, sdtNguoiDat, trangThai) VALUES
(4, 1, '2025-12-22', 1, 650000, N'Ngô Thị Thảo Vy', N'thaovyn0312@gmail.com', N'0934567890', N'Đã Duyệt'),
(5, 5, '2025-12-24', 2, 1430000, N'Nguyễn Triều Tiên', N'nguyentrieutien2005py@gmail.com', N'0945678901', N'Chờ Duyệt'),
(6, 9, '2025-12-28', 1, 680000, N'Nguyễn Thị Thương', N'nguyenthithuong15112005@gmail.com', N'0956789012', N'Đã Duyệt');

-- 10. ƯU ĐÃI
INSERT INTO UuDai (maCode, tenUuDai, moTa, loaiGiam, giaTriGiam, giamToiDa, soLuong, ngayBatDau, ngayKetThuc, hinhAnh) VALUES
('GIAM50K', N'Giảm 50k cho thành viên mới', N'Áp dụng cho đơn hàng từ 500k', 'SoTien', 50000, 50000, 100, '2025-01-01', '2025-12-31', 'uudai1.jpg'),
('GIAM10%', N'Giảm 10% mùa lễ hội', N'Giảm tối đa 100k', 'PhanTram', 10, 100000, 50, '2025-12-01', '2025-12-31', 'uudai2.jpg'),
('FREESHIP', N'Miễn phí tài liệu', N'Tặng bộ tài liệu công thức', 'SoTien', 0, 0, 200, '2025-01-01', '2025-06-30', 'uudai3.jpg');
-- 11. YÊU THÍCH (Học viên lưu các khóa học yêu thích)

GO
select * from GiaoVien
select * from NguoiDung
select * from DanhMucMonAn
select * from HinhAnhMonAn
select * from KhoaHoc
select * from LichTrinhLopHoc
select * from YeuThich
select * from DatLich