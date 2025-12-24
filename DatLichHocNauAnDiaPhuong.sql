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
-- PHẦN 2: TẠO CẤU TRÚC BẢNG
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

-- 10. THÔNG BÁO
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

-- 11. YÊU THÍCH
CREATE TABLE YeuThich (
    maYeuThich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maKhoaHoc INT NOT NULL,
    ngayThem DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc),
    CONSTRAINT UQ_YeuThich UNIQUE (maHocVien, maKhoaHoc)
);

-- 12. ƯU ĐÃI
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
	loaiUuDai NVARCHAR(50) NULL,
	dieuKienSoLuong INT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    hinhAnh VARCHAR(255),
    trangThai NVARCHAR(20) DEFAULT N'Hoạt Động',
    ngayTao DATETIME DEFAULT GETDATE()
);

-- 13. ĐẶT LỊCH
CREATE TABLE DatLich (
    maDatLich INT PRIMARY KEY IDENTITY(1,1),
    maHocVien INT NOT NULL,
    maLichTrinh INT NOT NULL,
    ngayThamGia DATE NOT NULL,
    
    soLuongNguoi INT DEFAULT 1,
    tongTien DECIMAL(10,2) NOT NULL,
    tenNguoiDat NVARCHAR(100),
    emailNguoiDat VARCHAR(100),
    sdtNguoiDat VARCHAR(15),
    
    maUuDai INT NULL,
    soTienGiam DECIMAL(10,2) NULL,
    
    -- Thời gian
    ngayDat DATETIME DEFAULT GETDATE(),
    thoiGianHetHan DATETIME NULL, -- Hết hạn thanh toán (10 phút sau khi đặt)
    thoiGianHuy DATETIME NULL, -- Thời gian hủy đơn (nếu có)
    
    -- Trạng thái đơn
    -- 'Đặt trước' - Mới đặt, chưa thanh toán (giữ chỗ 10 phút), hoặc thanh toán rồi nhưng lớp học chưa diễn ra
    -- 'Đã hoàn thành'  - Đã thanh toán, và lớp học đã diễn ra
    -- 'Đã huỷ' - Đã hủy (chỉ được huỷ với đơn đã thanh toán và vào lúc trước thời gian lớp diễn ra 15 phút)
    trangThai NVARCHAR(30) DEFAULT N'Đặt trước',
    
    ghiChu NVARCHAR(MAX),
    
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maLichTrinh) REFERENCES LichTrinhLopHoc(maLichTrinh),
    FOREIGN KEY (maUuDai) REFERENCES UuDai(maUuDai),
    
    -- Constraint kiểm tra trạng thái hợp lệ
    CONSTRAINT CK_TrangThaiDatLich CHECK (
        trangThai IN (N'Đặt trước', N'Đã hoàn thành', N'Đã huỷ')
    )
);

-- 14. ĐÁNH GIÁ
CREATE TABLE DanhGia (
    maDanhGia INT PRIMARY KEY IDENTITY(1,1),
	maDatLich INT NULL,
    maHocVien INT NOT NULL,
    maKhoaHoc INT NOT NULL,
    diemDanhGia INT CHECK (diemDanhGia BETWEEN 1 AND 5),
    binhLuan NVARCHAR(MAX),
    ngayDanhGia DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maKhoaHoc) REFERENCES KhoaHoc(maKhoaHoc),
	FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich)
);

-- 15. HÌNH ẢNH ĐÁNH GIÁ
CREATE TABLE HinhAnhDanhGia (
        maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
        maDanhGia INT NOT NULL,
        duongDan VARCHAR(500) NOT NULL,
        loaiFile NVARCHAR(20) DEFAULT N'image',
        thuTu INT DEFAULT 1,
        ngayTao DATETIME DEFAULT GETDATE(),
        FOREIGN KEY (maDanhGia) REFERENCES DanhGia(maDanhGia) ON DELETE CASCADE
    );

-- 16. THANH TOÁN
CREATE TABLE ThanhToan (
    maThanhToan INT PRIMARY KEY IDENTITY(1,1),
    maDatLich INT NOT NULL,
    
    -- Thông tin thanh toán cơ bản
    soTien DECIMAL(10,2) NOT NULL,
    phuongThuc NVARCHAR(30) NOT NULL, -- 'Momo'
    
    -- Thông tin Momo
    requestId VARCHAR(100) NULL,        -- Request ID gửi đến Momo (unique mỗi lần)
    orderId VARCHAR(100) NULL,          -- Order ID từ hệ thống (unique)
    transId VARCHAR(100) NULL,          -- Transaction ID từ Momo (sau khi thanh toán thành công)
    
    -- URL và response
    payUrl TEXT NULL,                   -- URL thanh toán Momo trả về
    deeplink TEXT NULL,                 -- Deep link mở app Momo (nếu có)
    qrCodeUrl TEXT NULL,                -- QR Code URL (nếu có)
    
    -- Kết quả thanh toán
    resultCode INT NULL,                -- Mã kết quả từ Momo (0 = thành công)
    message NVARCHAR(255) NULL,         -- Thông báo từ Momo
    
    -- Trạng thái thanh toán
    -- 0 = Chưa thanh toán / Thất bại
    -- 1 = Đã thanh toán thành công
    trangThai BIT DEFAULT 0,
    
    -- Thời gian
    thoiGianTao DATETIME DEFAULT GETDATE(),      -- Thời gian tạo giao dịch
    ngayThanhToan DATETIME NULL,                 -- Thời gian thanh toán thành công
    thoiGianCapNhat DATETIME NULL,               -- Lần cập nhật cuối
    
    -- Thông tin bổ sungJSON)
    signature VARCHAR(255) NULL,         -- Chữ ký từ Momo (để verify)
    ghiChu NVARCHAR(MAX),
    
    FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich),
    
    -- Constraint đảm bảo orderId unique (không trùng)
    CONSTRAINT UQ_OrderId UNIQUE (orderId)
);

-- 17. LỊCH SỬ ƯU ĐÃI
CREATE TABLE LichSuUuDai (
    maLichSu INT PRIMARY KEY IDENTITY(1,1),
    maUuDai INT NOT NULL,
    maDatLich INT NOT NULL,
    soTienGiam DECIMAL(10,2) NOT NULL,
    ngaySuDung DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maUuDai) REFERENCES UuDai(maUuDai),
    FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich)
);

-- 18. HÓA ĐƠN
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


IF OBJECT_ID('sp_ThongBaoTruoc1Ngay', 'P') IS NOT NULL
    DROP PROCEDURE sp_ThongBaoTruoc1Ngay;
GO

CREATE PROCEDURE sp_ThongBaoTruoc1Ngay
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @NgayMai DATE = DATEADD(DAY, 1, CAST(GETDATE() AS DATE));
    
    -- Tạo thông báo cho những học viên có lịch học vào ngày mai
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
			SELECT DISTINCT
			d.maHocVien,
			N'🔔 Lớp học sắp diễn ra',
			N'Lớp "' + kh.tenKhoaHoc + N'" sẽ diễn ra vào ngày mai (' 
				+ CONVERT(NVARCHAR, d.ngayThamGia, 103) + N') lúc ' 
				-- Đảm bảo ép kiểu về TIME trước khi format
				+ LEFT(CAST(lt.gioBatDau AS TIME), 5) + N' tại ' + lt.diaDiem 
				+ N'. Hãy chuẩn bị sẵn sàng nhé!',
			N'NhacNho',
			kh.hinhAnh
		FROM DatLich d
		JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
		JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
		-- Sử dụng CAST để so sánh ngày chính xác hơn
		WHERE CAST(d.ngayThamGia AS DATE) = @NgayMai
		  AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')
      -- Kiểm tra chưa có thông báo nhắc nhở 1 ngày cho lịch này
      AND NOT EXISTS (
          SELECT 1 FROM ThongBao tb 
          WHERE tb.maNguoiNhan = d.maHocVien 
            AND tb.loaiThongBao = N'NhacNho'
            AND tb.tieuDe = N'🔔 Lớp học sắp diễn ra'
            AND tb.noiDung LIKE N'%' + kh.tenKhoaHoc + N'%' 
            AND tb.noiDung LIKE N'%' + CONVERT(NVARCHAR, d.ngayThamGia, 103) + N'%'
            AND CAST(tb.ngayTao AS DATE) = CAST(GETDATE() AS DATE)
      );
    
    SELECT @@ROWCOUNT AS SoThongBaoTao;
END;
GO

---------------------------------------------------------------------
-- STORED PROCEDURE: Tạo thông báo nhắc nhở trước 30 phút
---------------------------------------------------------------------
IF OBJECT_ID('sp_ThongBaoTruoc30Phut', 'P') IS NOT NULL
    DROP PROCEDURE sp_ThongBaoTruoc30Phut;
GO

CREATE PROCEDURE sp_ThongBaoTruoc30Phut
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @HomNay DATE = CAST(GETDATE() AS DATE);
    DECLARE @GioHienTai TIME = CAST(GETDATE() AS TIME);
    DECLARE @GioSau30Phut TIME = DATEADD(MINUTE, 30, @GioHienTai);
    
    -- Tạo thông báo cho những học viên có lớp học bắt đầu trong 30 phút tới
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
    SELECT DISTINCT
        d.maHocVien,
        N'⏰ Còn 30 phút nữa!',
        N'Lớp "' + kh.tenKhoaHoc + N'" sẽ bắt đầu lúc ' 
            + CONVERT(NVARCHAR(5), lt.gioBatDau, 108) + N' tại ' + lt.diaDiem 
            + N'. Hãy đến đúng giờ nhé!',
        N'NhacNho',
        kh.hinhAnh
    FROM DatLich d
    JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
    WHERE d.ngayThamGia = @HomNay
      AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')
      -- Lớp bắt đầu trong khoảng 25-35 phút tới (để có buffer)
      AND lt.gioBatDau >= @GioHienTai
      AND lt.gioBatDau <= DATEADD(MINUTE, 35, @GioHienTai)
      AND lt.gioBatDau >= DATEADD(MINUTE, 25, @GioHienTai)
      -- Kiểm tra chưa có thông báo 30 phút cho lịch này hôm nay
      AND NOT EXISTS (
          SELECT 1 FROM ThongBao tb 
          WHERE tb.maNguoiNhan = d.maHocVien 
            AND tb.loaiThongBao = N'NhacNho'
            AND tb.tieuDe = N'⏰ Còn 30 phút nữa!'
            AND tb.noiDung LIKE N'%' + kh.tenKhoaHoc + N'%'
            AND CAST(tb.ngayTao AS DATE) = @HomNay
      );
    
    SELECT @@ROWCOUNT AS SoThongBaoTao;
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
(N'AnhThu', N'gv123', N'Nguyễn Hoàng Anh Thư', N'nguyenthu2018dn@gmail.com', N'0923456789', N'Nữ', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'GiaoVien', N'HoatDong'),
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

 -- 3.KHÓA HỌC
INSERT INTO KhoaHoc (tenKhoaHoc, moTa, gioiThieu, giaTriSauBuoiHoc, giaTien, hinhAnh, coUuDai) VALUES
-- HÀ NỘI
(N'Ẩm thực phố cổ Hà Nội', 
 N'Khám phá hương vị đặc trưng của ẩm thực phố cổ với phở, bún chả, chè khúc bạch', 
 N'Đến với khóa học này, bạn sẽ được đắm mình trong không gian hoài cổ của Hà Nội. Không chỉ đơn thuần là nấu ăn, đây là hành trình tìm lại những nét tinh túy nhất. Bạn sẽ được hướng dẫn tỉ mỉ từ cách chọn xương bò nấu Phở, kỹ thuật nướng thịt Bún Chả bằng than hoa, và kết thúc bằng món tráng miệng Chè khúc bạch thanh mát đúng điệu người Hà Nội.', 
 N'• Nắm vững kỹ thuật nấu phở Hà Nội chính gốc
• Tự tin làm bún chả chấm nước mắm tỏi ớt
• Bí quyết nấu chè ngon', 
 650000, N'am_thuc_pho_co_ha_noi_1.jpg', 1),

(N'Bún và Món Cuốn Hà Thành', 
 N'Sự kết hợp giữa các món bún nước thanh tao và món cuốn tươi mát', 
 N'Khóa học này mang đến sự cân bằng hoàn hảo. Bạn sẽ học cách nấu Bún Thang cầu kỳ "đệ nhất bát trân", Bún Ốc chua dịu vị giấm bỗng. Đặc biệt, lớp học giới thiệu món Phở cuốn Hà Nội (biến tấu thanh cảnh) và Chè hạt sen long nhãn - thức quà quý tiến vua, giúp giải nhiệt và cân bằng vị giác.', 
 N'• Làm chủ nghệ thuật nấu nước dùng trong
• Kỹ thuật cuốn gỏi đẹp mắt, chặt tay
• Nấu chè hạt sen không bị nát', 
 580000, N'bun_va_mon_cuon_ha_thanh_1.jpg', 0),

(N'Bánh Dân Gian & Quà Quê Bắc Bộ', 
 N'Học làm bánh cuốn, bánh đúc, bánh khúc - những thức quà sáng trứ danh', 
 N'Tái hiện không khí những gánh hàng rong xưa cũ. Bạn sẽ tự tay tráng lớp Bánh cuốn mỏng tang, quấy nồi Bánh đúc lạc dẻo quánh chấm tương bần, và đồ xôi làm Bánh khúc thơm mùi lá. Kết thúc buổi học là bát Bánh trôi nước gừng ấm nóng, mang đậm hồn quê Bắc Bộ.', 
 N'• Kỹ thuật tráng bánh mỏng không bị rách
• Bí quyết xử lý vôi tôi cho bánh đúc
• Làm nhân đậu xanh thơm bùi', 
 520000, N'banh_dan_gian_va_qua_que_bac_bo_1.jpg', 1),

(N'Món Nhậu & Lai Rai Hà Nội', 
 N'Các món "mồi" bén đặc trưng: nem chua rán, chả rươi, bún đậu', 
 N'Văn hóa nhậu vỉa hè là một nét độc đáo. Khóa học hướng dẫn làm Nem chua rán giòn tan, Chả rươi (đặc sản mùa thu), và đặc biệt là mẹt Bún đậu mắm tôm đầy đặn. Kèm theo đó là món Chè lam nếp gừng để nhâm nhi cùng trà nóng sau bữa ăn.', 
 N'• Làm nem chua rán an toàn tại nhà
• Kỹ thuật chế biến rươi (nếu đúng mùa)
• Pha mắm tôm sủi bọt chuẩn vị', 
 600000, N'mon_nhau_va_lai_rai_ha_noi_1.jpg', 0),

-- HUẾ
(N'Tinh Hoa Cung Đình Huế', 
 N'Những món ăn tiến vua cầu kỳ: Bún bò, Cơm hến, Bánh bèo', 
 N'Ẩm thực Huế là đỉnh cao của sự tinh tế. Bạn sẽ học cách nấu Bún bò Huế chuẩn vị ruốc sả, làm Cơm hến với hàng chục loại gia vị, và tỉ mẩn đổ từng chén Bánh bèo tôm chấy. Tráng miệng bằng Chè Huế ngọt ngào để kết thúc một bữa ăn đậm chất hoàng gia.', 
 N'• Nấu nước dùng bún bò trong và đậm đà
• Xử lý hến và các loại rau sống
• Đổ bánh bèo chén mỏng và xoáy', 
 715000, N'tinh_hoa_cung_dinh_hue_1.jpg', 1),

(N'Bánh Huế Truyền Thống', 
 N'Bộ sưu tập các loại bánh gói lá: Nậm, Lọc, Ít, Ram', 
 N'Chuyên đề về các loại bánh bột nổi tiếng. Bạn sẽ phân biệt và thực hành làm Bánh nậm (bột gạo), Bánh lọc (bột năng), Bánh ít (bột nếp) và Bánh ram ít (kết hợp chiên và hấp). Kỹ thuật gói lá chuối/lá dong xanh mướt là trọng tâm của lớp học này.', 
 N'• Kỹ thuật pha các loại bột không bị cứng
• Gói bánh đẹp, đều tay
• Làm nhân tôm thịt đậm đà', 
 550000, N'banh_hue_truyen_thong_1.jpg', 0),

(N'Ẩm Thực Chay Xứ Huế', 
 N'Nghệ thuật nấu món chay giả mặn tinh tế và thanh tịnh', 
 N'Huế là cái nôi của Phật giáo, nên món chay ở đây rất phát triển. Bạn sẽ học làm Gỏi cuốn chay, Bún chay Huế với nước dùng rau củ ngọt tự nhiên, Bánh bột lọc chay nhân đậu xanh nấm mèo. Tráng miệng bằng Chè sen thanh khiết.', 
 N'• Nấu nước dùng chay ngọt lừ
• Kỹ thuật chế biến đồ chay giả mặn
• Trình bày món chay đẹp mắt', 
 480000, N'am_thuc_chay_xu_hue_1.jpg', 1),

(N'Bánh Trái & Quà Chiều Cố Đô', 
 N'Sự kết hợp độc đáo giữa các loại bánh mặn và chè ngọt ăn xế', 
 N'Người Huế có thói quen ăn quà chiều (ăn xế) rất phong phú. Khóa học này giới thiệu combo độc đáo: Bánh lọc lá và Bánh ram ít (bánh mặn) ăn kèm nước mắm cay, sau đó tráng miệng bằng Chè khoai tía dẻo thơm và Chè bột lọc heo quay độc đáo. Một trải nghiệm ẩm thực đa chiều thú vị.', 
 N'• Làm bánh lọc gói lá chuối chuẩn vị
• Bí quyết nấu chè khoai tía màu đẹp
• Làm heo quay rim ăn chè', 
 520000, N'banh_trai_va_qua_chieu_co_do_1.jpg', 0),

-- ĐÀ NẴNG
(N'Đặc Sản Biển Đà Nẵng', 
 N'Hương vị biển miền Trung: Gỏi cá, Mì Quảng, Bánh tráng cuốn', 
 N'Đà Nẵng nổi tiếng với hải sản và các món cuốn. Bạn sẽ học làm Gỏi cá Nam Ô (biến tấu an toàn), nấu Mì Quảng tôm thịt đậm đà ít nước, và Bánh tráng cuốn thịt heo hai đầu da chấm mắm nêm. Kết thúc bằng ly Chè bắp Cẩm Nam ngọt dẻo.', 
 N'• Kỹ thuật làm gỏi cá không tanh
• Nấu nước nhưn Mì Quảng đúng điệu
• Pha mắm nêm chấm bánh tráng cuốn', 
 680000, N'dac_san_bien_da_nang_1.jpg', 1),

(N'Bánh Xèo & Nem Lụi Đà Nẵng', 
 N'Cặp đôi hoàn hảo của ẩm thực đường phố Đà Nẵng', 
 N'Bánh xèo miền Trung nhỏ, vỏ giòn tan, ăn kèm Nem lụi nướng sả thơm phức. Điểm nhấn của lớp học là công thức pha nước chấm gan đậu phộng (nước lèo) béo ngậy thần thánh. Ngoài ra còn có Bánh tráng thịt heo và Chè xoa xoa hạt lựu mát lạnh.', 
 N'• Đổ bánh xèo giòn lâu, không ngấm dầu
• Quết thịt nem lụi dai ngon
• Nấu nước chấm gan đặc biệt', 
 590000, N'banh_xeo_va_nem_lui_da_nang_1.jpg', 0),

(N'Bún Mắm & Chả Cá Miền Trung', 
 N'Những món bún đậm đà hương vị biển: Bún chả cá, Bún mắm nêm', 
 N'Học cách quết Chả cá thu dai ngon không hàn the, nấu nước dùng Bún chả cá ngọt thanh từ bí đỏ. Bên cạnh đó là Bún mắm nêm thịt quay giòn bì đậm vị. Tráng miệng với Chè khoai môn dẻo bùi cốt dừa béo ngậy.', 
 N'• Kỹ thuật quết chả cá dai
• Làm heo quay giòn bì bằng chảo
• Nấu chè khoai môn dẻo quánh', 
 620000, N'bun_mam_va_cha_ca_mien_trung_1.jpg', 1),

(N'Sợi Bánh Thủ Công & Cao Lầu', 
 N'Khám phá Cao lầu Hội An, Bánh canh cua và các món sợi', 
 N'Đi sâu vào kỹ thuật làm sợi bánh. Bạn sẽ làm quen với Ram tôm đất giòn rụm, nấu Bánh canh cua với sợi bánh dai trong, và đặc biệt là món Cao lầu Hội An trứ danh với thịt xíu và sợi mì vàng ươm. Tráng miệng bằng Chè Thái sầu riêng thơm nức mũi.', 
 N'• Bí quyết làm thịt xíu Cao Lầu
• Nấu nước dùng bánh canh cua sánh sệt
• Pha trộn các loại bột làm sợi bánh', 
 650000, N'soi_banh_thu_cong_va_cao_lau_1.jpg', 0),

-- CẦN THƠ
(N'Hương Vị Miền Tây Sông Nước', 
 N'Đặc sản mùa nước nổi: Lẩu mắm, Cá lóc nướng trui', 
 N'Mang cả miền Tây vào bếp với Lẩu mắm đậm đà ăn kèm rau đồng nội. Học cách nướng Cá lóc trui thơm mùi rơm/khói, làm Gỏi xoài khô cá lóc lạ miệng. Kết thúc bữa ăn bằng món Chuối nếp nướng nước cốt dừa béo ngậy.', 
 N'• Nấu lẩu mắm không bị mặn chát
• Nướng cá lóc giữ được độ ngọt
• Kỹ thuật nấu nước cốt dừa béo', 
 580000, N'huong_vi_mien_tay_song_nuoc_1.png', 1),

(N'Bánh Xèo & Bánh Khọt Nam Bộ', 
 N'Cặp đôi bánh chiên giòn rụm, vàng ươm của miền Nam', 
 N'Phân biệt Bánh xèo miền Tây chảo lớn mỏng tang với Bánh khọt Vũng Tàu nhỏ xinh. Lớp học cũng hướng dẫn nấu Bún riêu cua kiểu miền Nam với huyết và chả lụa. Tráng miệng bằng Bánh tằm bì nước cốt dừa.', 
 N'• Pha bột bánh xèo giòn rụm, viền mỏng
• Đổ bánh khọt nhân tôm tươi
• Nấu riêu cua đóng tảng đẹp mắt', 
 550000, N'banh_xeo_va_banh_khot_nam_bo_1.jpg', 0),

(N'Hủ Tiếu & Món Ngon Phương Nam', 
 N'Chuyên đề về các món sợi và gỏi đặc trưng Nam Bộ', 
 N'Khám phá thế giới Hủ tiếu: Hủ tiếu Sa Đéc với nước sốt trộn khô đặc biệt, Hủ tiếu Nam Vang nước lèo ngọt xương ống. Kèm theo là món Gỏi củ hủ dừa tôm thịt giòn sần sật và Sâm bổ lượng giải nhiệt mùa hè.', 
 N'• Hầm nước lèo hủ tiếu trong vắt
• Làm sốt trộn hủ tiếu khô
• Sơ chế củ hủ dừa trắng giòn', 
 600000, N'hu_tieu_va_mon_ngon_phuong_nam_1.jpg', 1),

(N'Biến Tấu Chuối & Chè Nam Bộ', 
 N'Thế giới các món ngon từ Chuối: Hấp, Nướng, Chè', 
 N'Chuối là nguyên liệu vàng của bánh trái miền Tây. Khóa học này sẽ dạy bạn làm Bánh da lợn dẻo dai nhiều lớp, Bánh chuối nướng đỏ au thơm lừng, và Chè bà ba (chè thưng) béo ngậy. Thêm món Chè bưởi An Giang để cân bằng lại khẩu vị.', 
 N'• Đổ bánh da lợn tách lớp đẹp
• Nướng bánh chuối lên màu đỏ đẹp tự nhiên
• Nấu chè bà ba chuẩn vị', 
 480000, N'bien_tau_chuoi_va_che_nam_bo_1.jpg', 0);

-- 4. LỊCH TRÌNH LỚP HỌC (ĐÃ CHỈNH GIỜ)
INSERT INTO LichTrinhLopHoc (maKhoaHoc, maGiaoVien, thuTrongTuan, gioBatDau, gioKetThuc, diaDiem, soLuongToiDa) VALUES
-- HÀ NỘI
(1, 1, '2,3,4,5,6,7,CN', '17:30', '20:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 20),
(2, 1, '2,4,6',          '08:30', '11:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 18),
(3, 2, '3,5,7',          '08:30', '11:30', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 15),
(4, 1, '7,CN',            '14:00', '17:00', N'45 Hàng Bạc, Hoàn Kiếm, Hà Nội', 20),
-- HUẾ
(5, 1, '7,CN',					 '17:30', '20:30', N'23 Lê Duẩn, Huế', 20),
(6, 2, '2,3,4,5,6,7,CN',          '08:30', '11:30', N'23 Lê Duẩn, Huế', 18),
(7, 1, '3,5,7',          '08:30', '11:30', N'23 Lê Duẩn, Huế', 15),
(8, 2, '2,4,6',            '14:00', '17:00', N'23 Lê Duẩn, Huế', 15),
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

-- 8. HÌNH ẢNH KHÓA HỌC (48 dòng - 3 ảnh mỗi khóa học)
INSERT INTO HinhAnhKhoaHoc (maKhoaHoc, duongDan, thuTu) VALUES
-- Hà Nội
(1, N'am_thuc_pho_co_ha_noi_1.jpg', 1),           (1, N'am_thuc_pho_co_ha_noi_2.jpg', 2),           (1, N'am_thuc_pho_co_ha_noi_3.jpg', 3),
(2, N'bun_va_mon_cuon_ha_thanh_1.jpg', 1),        (2, N'bun_va_mon_cuon_ha_thanh_2.jpg', 2),        (2, N'bun_va_mon_cuon_ha_thanh_3.jpg', 3),
(3, N'banh_dan_gian_va_qua_que_bac_bo_1.jpg', 1), (3, N'banh_dan_gian_va_qua_que_bac_bo_2.jpg', 2), (3, N'banh_dan_gian_va_qua_que_bac_bo_3.jpg', 3),
(4, N'mon_nhau_va_lai_rai_ha_noi_1.jpg', 1),      (4, N'mon_nhau_va_lai_rai_ha_noi_2.jpg', 2),      (4, N'mon_nhau_va_lai_rai_ha_noi_3.jpg', 3),

-- Huế
(5, N'tinh_hoa_cung_dinh_hue_1.jpg', 1),          (5, N'tinh_hoa_cung_dinh_hue_2.jpg', 2),          (5, N'tinh_hoa_cung_dinh_hue_3.jpg', 3),
(6, N'banh_hue_truyen_thong_1.jpg', 1),           (6, N'banh_hue_truyen_thong_2.jpg', 2),           (6, N'banh_hue_truyen_thong_3.jpg', 3),
(7, N'am_thuc_chay_xu_hue_1.jpg', 1),             (7, N'am_thuc_chay_xu_hue_2.jpg', 2),             (7, N'am_thuc_chay_xu_hue_3.jpg', 3),
(8, N'banh_trai_va_qua_chieu_co_do_1.jpg', 1),    (8, N'banh_trai_va_qua_chieu_co_do_2.jpg', 2),    (8, N'banh_trai_va_qua_chieu_co_do_3.jpg', 3),

-- Đà Nẵng
(9, N'dac_san_bien_da_nang_1.jpg', 1),            (9, N'dac_san_bien_da_nang_2.jpg', 2),            (9, N'dac_san_bien_da_nang_3.jpg', 3),
(10, N'banh_xeo_va_nem_lui_da_nang_1.jpg', 1),    (10, N'banh_xeo_va_nem_lui_da_nang_2.jpg', 2),    (10, N'banh_xeo_va_nem_lui_da_nang_3.jpg', 3),
(11, N'bun_mam_va_cha_ca_mien_trung_1.jpg', 1),   (11, N'bun_mam_va_cha_ca_mien_trung_2.jpg', 2),   (11, N'bun_mam_va_cha_ca_mien_trung_3.jpg', 3),
(12, N'soi_banh_thu_cong_va_cao_lau_1.jpg', 1),   (12, N'soi_banh_thu_cong_va_cao_lau_2.jpg', 2),   (12, N'soi_banh_thu_cong_va_cao_lau_3.jpg', 3),

-- Cần Thơ (lưu ý khóa 13 ảnh 1 là .png)
(13, N'huong_vi_mien_tay_song_nuoc_1.png', 1),    (13, N'huong_vi_mien_tay_song_nuoc_2.jpg', 2),    (13, N'huong_vi_mien_tay_song_nuoc_3.jpg', 3),
(14, N'banh_xeo_va_banh_khot_nam_bo_1.jpg', 1),   (14, N'banh_xeo_va_banh_khot_nam_bo_2.jpg', 2),   (14, N'banh_xeo_va_banh_khot_nam_bo_3.jpg', 3),
(15, N'hu_tieu_va_mon_ngon_phuong_nam_1.jpg', 1), (15, N'hu_tieu_va_mon_ngon_phuong_nam_2.jpg', 2), (15, N'hu_tieu_va_mon_ngon_phuong_nam_3.jpg', 3),
(16, N'bien_tau_chuoi_va_che_nam_bo_1.jpg', 1),   (16, N'bien_tau_chuoi_va_che_nam_bo_2.jpg', 2),   (16, N'bien_tau_chuoi_va_che_nam_bo_3.jpg', 3);



-- 10. ƯU ĐÃI
INSERT INTO UuDai (maCode, tenUuDai, moTa, loaiGiam, giaTriGiam, ngayBatDau, ngayKetThuc, trangThai, loaiUuDai, dieuKienSoLuong, hinhAnh) VALUES 
('KHACHHANGMOI', N'Ưu đãi tài khoản mới', N'Giảm 30% cho đơn hàng đầu tiên', 'PhanTram', 30, '2024-01-01', '2025-12-31', N'Hoạt Động', 'NEWUSER', NULL, 'uudai1.jpg'),
('THAMGIANHOM', N'Ưu đãi nhóm', N'Giảm 20% khi đặt từ 5 người', 'PhanTram', 20, '2024-01-01', '2025-12-31', N'Hoạt Động', 'GROUP', 5, 'uudai2.jpg');

---------------------------------------------------------------------
-- THÔNG BÁO NHẮC NHỞ LỚP HỌC
-- 1. Trước 1 ngày: "Lớp học sắp diễn ra"
-- 2. Trước 30 phút: "Còn 30 phút nữa sẽ bắt đầu lớp học"
---------------------------------------------------------------------

---------------------------------------------------------------------
-- STORED PROCEDURE: Tạo thông báo nhắc nhở trước 1 ngày
---------------------------------------------------------------------
IF OBJECT_ID('sp_ThongBaoTruoc1Ngay', 'P') IS NOT NULL
    DROP PROCEDURE sp_ThongBaoTruoc1Ngay;
GO

CREATE PROCEDURE sp_ThongBaoTruoc1Ngay
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @NgayMai DATE = DATEADD(DAY, 1, CAST(GETDATE() AS DATE));
    
    -- Tạo thông báo cho những học viên có lịch học vào ngày mai
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
    SELECT DISTINCT
        d.maHocVien,
        N'🔔 Lớp học sắp diễn ra',
        N'Lớp "' + kh.tenKhoaHoc + N'" sẽ diễn ra vào ngày mai (' 
            + CONVERT(NVARCHAR, d.ngayThamGia, 103) + N') lúc ' 
            + CONVERT(NVARCHAR(5), lt.gioBatDau, 108) + N' tại ' + lt.diaDiem 
            + N'. Hãy chuẩn bị sẵn sàng nhé!',
        N'NhacNho',
        kh.hinhAnh
    FROM DatLich d
    JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
    WHERE d.ngayThamGia = @NgayMai
      AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')
      -- Kiểm tra chưa có thông báo nhắc nhở 1 ngày cho lịch này
      AND NOT EXISTS (
          SELECT 1 FROM ThongBao tb 
          WHERE tb.maNguoiNhan = d.maHocVien 
            AND tb.loaiThongBao = N'NhacNho'
            AND tb.tieuDe = N'🔔 Lớp học sắp diễn ra'
            AND tb.noiDung LIKE N'%' + kh.tenKhoaHoc + N'%' 
            AND tb.noiDung LIKE N'%' + CONVERT(NVARCHAR, d.ngayThamGia, 103) + N'%'
            AND CAST(tb.ngayTao AS DATE) = CAST(GETDATE() AS DATE)
      );
    
    SELECT @@ROWCOUNT AS SoThongBaoTao;
END;
GO

---------------------------------------------------------------------
-- STORED PROCEDURE: Tạo thông báo nhắc nhở trước 30 phút
---------------------------------------------------------------------
IF OBJECT_ID('sp_ThongBaoTruoc30Phut', 'P') IS NOT NULL
    DROP PROCEDURE sp_ThongBaoTruoc30Phut;
GO

CREATE PROCEDURE sp_ThongBaoTruoc30Phut
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @HomNay DATE = CAST(GETDATE() AS DATE);
    DECLARE @GioHienTai TIME = CAST(GETDATE() AS TIME);
    DECLARE @GioSau30Phut TIME = DATEADD(MINUTE, 30, @GioHienTai);
    
    -- Tạo thông báo cho những học viên có lớp học bắt đầu trong 30 phút tới
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
    SELECT DISTINCT
        d.maHocVien,
        N'⏰ Còn 30 phút nữa!',
        N'Lớp "' + kh.tenKhoaHoc + N'" sẽ bắt đầu lúc ' 
            + CONVERT(NVARCHAR(5), lt.gioBatDau, 108) + N' tại ' + lt.diaDiem 
            + N'. Hãy đến đúng giờ nhé!',
        N'NhacNho',
        kh.hinhAnh
    FROM DatLich d
    JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
    WHERE d.ngayThamGia = @HomNay
      AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')
      -- Lớp bắt đầu trong khoảng 25-35 phút tới (để có buffer)
      AND lt.gioBatDau >= @GioHienTai
      AND lt.gioBatDau <= DATEADD(MINUTE, 35, @GioHienTai)
      AND lt.gioBatDau >= DATEADD(MINUTE, 25, @GioHienTai)
      -- Kiểm tra chưa có thông báo 30 phút cho lịch này hôm nay
      AND NOT EXISTS (
          SELECT 1 FROM ThongBao tb 
          WHERE tb.maNguoiNhan = d.maHocVien 
            AND tb.loaiThongBao = N'NhacNho'
            AND tb.tieuDe = N'⏰ Còn 30 phút nữa!'
            AND tb.noiDung LIKE N'%' + kh.tenKhoaHoc + N'%'
            AND CAST(tb.ngayTao AS DATE) = @HomNay
      );
    
    SELECT @@ROWCOUNT AS SoThongBaoTao;
END;
GO

---------------------------------------------------------------------
-- HƯỚNG DẪN SỬ DỤNG
---------------------------------------------------------------------
-- Backend Spring Boot sẽ tự động gọi các stored procedure này:
-- - sp_ThongBaoTruoc1Ngay: Chạy mỗi ngày lúc 8:00 sáng
-- - sp_ThongBaoTruoc30Phut: Chạy mỗi 5 phút
-- 
-- Hoặc có thể test thủ công:
-- EXEC sp_ThongBaoTruoc1Ngay;
-- EXEC sp_ThongBaoTruoc30Phut;
---------------------------------------------------------------------

---------------------------- Trigger đặt lịch---------------------------
---------------------------------------------------------------------
-- TRIGGER 1: Tự động thêm thông báo khi bấm nút thanh toán
---------------------------------------------------------------------
CREATE TRIGGER trg_ThongBaoGiuChoTamThoi
ON DatLich
AFTER INSERT
AS
BEGIN
    INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
    SELECT
        i.maHocVien,
        N'⏳ Đang giữ chỗ cho bạn',
        N'Chúng tôi đang giữ chỗ cho lớp "' + kh.tenKhoaHoc + 
        N'" vào ngày ' + CONVERT(NVARCHAR, i.ngayThamGia, 103) +
        N'. Vui lòng hoàn tất thanh toán trong vòng 10 phút để xác nhận tham gia.',
        N'GiuCho'
    FROM inserted i
    JOIN LichTrinhLopHoc lt ON i.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc;
END;
GO

---------------------------------------------------------------------
-- TRIGGER 2: Thông báo khi thanh toán thành công
---------------------------------------------------------------------
CREATE TRIGGER trg_ThongBaoThanhToan
ON ThanhToan
AFTER UPDATE
AS
BEGIN
    -- Chỉ thông báo khi chuyển từ chưa thanh toán (0) sang đã thanh toán (1)
    IF UPDATE(trangThai)
    BEGIN
        INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh)
        SELECT 
            d.maHocVien,
            N'💳 Thanh toán thành công',
            N'Bạn đã thanh toán thành công cho lớp "' + kh.tenKhoaHoc + 
            N'" với số tiền ' + FORMAT(i.soTien, 'N0') + N'đ. ' +
            N'Lớp học sẽ diễn ra vào ngày ' + CONVERT(NVARCHAR, d.ngayThamGia, 103) +
            N' lúc ' + CONVERT(NVARCHAR(5), lt.gioBatDau, 108) + 
            N' tại ' + lt.diaDiem + N'. Hẹn gặp bạn!',
            N'ThanhToan',
            kh.hinhAnh
        FROM inserted i
        JOIN deleted del ON i.maThanhToan = del.maThanhToan
        JOIN DatLich d ON i.maDatLich = d.maDatLich
        JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
        JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
        WHERE i.trangThai = 1 AND del.trangThai = 0; -- Chuyển từ 0 -> 1
    END
END;
GO

---------------------------------------------------------------------
-- TRIGGER 3: Thông báo khi hủy đơn
---------------------------------------------------------------------
CREATE TRIGGER trg_ThongBaoHuyDon
ON DatLich
AFTER UPDATE
AS
BEGIN
    IF UPDATE(trangThai)
    BEGIN
        INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
        SELECT 
            i.maHocVien,
            N'❌ Đơn đặt lịch đã bị hủy',
            N'Đơn đặt lịch học lớp "' + kh.tenKhoaHoc + 
            N'" vào ngày ' + CONVERT(NVARCHAR, i.ngayThamGia, 103) + 
            N' đã bị hủy. ' +
            CASE 
                WHEN i.ghiChu IS NOT NULL THEN N'Lý do: ' + i.ghiChu
                ELSE N'Trường hợp đơn hàng đã hoàn tất thanh toán, khi hủy dịch vụ sẽ không áp dụng hoàn tiền.'
            END,
            N'HuyDon'
        FROM inserted i
        JOIN deleted d ON i.maDatLich = d.maDatLich
        JOIN LichTrinhLopHoc lt ON i.maLichTrinh = lt.maLichTrinh
        JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc
        WHERE i.trangThai = N'Đã huỷ' AND d.trangThai != N'Đã huỷ';
    END
END;
GO

---------------------------------------------------------------------
-- SP 2: Cập nhật thông tin Momo sau khi tạo payment request
---------------------------------------------------------------------
CREATE PROCEDURE sp_CapNhatThongTinMomo
    @maDatLich INT,
    @requestId VARCHAR(100),
    @orderId VARCHAR(100),
    @payUrl TEXT,
    @deeplink TEXT = NULL,
    @qrCodeUrl TEXT = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        UPDATE ThanhToan
        SET 
            requestId = @requestId,
            orderId = @orderId,
            payUrl = @payUrl,
            deeplink = @deeplink,
            qrCodeUrl = @qrCodeUrl,
            thoiGianCapNhat = GETDATE(),
            ghiChu = N'Đã tạo link thanh toán Momo'
        WHERE maDatLich = @maDatLich;
        
        IF @@ROWCOUNT > 0
            SELECT N'SUCCESS' AS ketQua, N'Cập nhật thông tin Momo thành công' AS thongBao;
        ELSE
            SELECT N'ERROR' AS ketQua, N'Không tìm thấy giao dịch' AS thongBao;
            
    END TRY
    BEGIN CATCH
        SELECT N'ERROR' AS ketQua, ERROR_MESSAGE() AS thongBao;
    END CATCH
END;
GO

---------------------------------------------------------------------
-- SP 3: Cập nhật kết quả thanh toán từ Momo callback
CREATE PROCEDURE sp_CapNhatKetQuaThanhToan  -- Dùng ALTER nếu đã tồn tại, hoặc CREATE nếu chưa
    @orderId VARCHAR(100),
    @transId VARCHAR(100) = NULL,
    @resultCode INT,
    @message NVARCHAR(255) = NULL,
    @signature VARCHAR(255) = NULL
AS
BEGIN
    SET NOCOUNT ON;
   
    DECLARE @maDatLich INT;
    DECLARE @maThanhToan INT;
    DECLARE @trangThaiMoi BIT;
    DECLARE @ketQua NVARCHAR(50);
    DECLARE @thongBao NVARCHAR(255);

    BEGIN TRY
        BEGIN TRANSACTION;
       
        -- 1. Tìm giao dịch theo orderId
        SELECT @maThanhToan = maThanhToan, @maDatLich = maDatLich
        FROM ThanhToan
        WHERE orderId = @orderId;
       
        IF @maThanhToan IS NULL
        BEGIN
            SET @ketQua = N'ERROR';
            SET @thongBao = N'Không tìm thấy giao dịch với orderId: ' + @orderId;
            ROLLBACK TRANSACTION;
            SELECT @ketQua AS ketQua, @thongBao AS thongBao;
            RETURN;
        END
       
        -- 2. Xác định trạng thái mới
        SET @trangThaiMoi = CASE WHEN @resultCode = 0 THEN 1 ELSE 0 END;
       
        -- 3. Cập nhật bảng ThanhToan
        UPDATE ThanhToan
        SET
            transId = @transId,
            resultCode = @resultCode,
            message = @message,
            signature = @signature,
            trangThai = @trangThaiMoi,
            ngayThanhToan = CASE WHEN @resultCode = 0 THEN GETDATE() ELSE NULL END,
            thoiGianCapNhat = GETDATE(),
            ghiChu = CASE
                WHEN @resultCode = 0 THEN N'✅ Thanh toán thành công qua Momo. TransID: ' + ISNULL(@transId, N'N/A')
                WHEN @resultCode = 1006 OR @resultCode = 1017 THEN N'❌ Người dùng đã hủy giao dịch'
                ELSE N'❌ Thanh toán thất bại. Mã lỗi: ' + CAST(@resultCode AS NVARCHAR) + N'. Chi tiết: ' + ISNULL(@message, N'N/A')
            END
        WHERE maThanhToan = @maThanhToan;
       
        -- 4. Cập nhật bảng DatLich nếu thành công
        IF @resultCode = 0
        BEGIN
            UPDATE DatLich
            SET thoiGianHetHan = NULL
            WHERE maDatLich = @maDatLich;
        END
       
        COMMIT TRANSACTION;

        -- 5. Trả về kết quả thành công
        SET @ketQua = N'SUCCESS';
        SET @thongBao = CASE
            WHEN @resultCode = 0 THEN N'Thanh toán thành công!'
            WHEN @resultCode = 1006 OR @resultCode = 1017 THEN N'Giao dịch đã bị hủy'
            ELSE N'Thanh toán thất bại: ' + ISNULL(@message, N'Lỗi không xác định')
        END;

        SELECT
            @maDatLich AS maDatLich,
            @maThanhToan AS maThanhToan,
            @trangThaiMoi AS trangThaiThanhToan,
            @resultCode AS resultCode,
            @ketQua AS ketQua,
            @thongBao AS thongBao;

    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
           
        SELECT 
            NULL AS maDatLich,
            NULL AS maThanhToan,
            NULL AS trangThaiThanhToan,
            NULL AS resultCode,
            N'ERROR' AS ketQua, 
            ERROR_MESSAGE() AS thongBao;
    END CATCH
END;
GO
---------------------------------------------------------------------
-- SP 4: Xóa đơn hết hạn (chưa thanh toán quá 10 phút)
---------------------------------------------------------------------
CREATE PROCEDURE sp_XoaDonHetHan
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @danhSachDonXoa TABLE (maDatLich INT, maHocVien INT, tenKhoaHoc NVARCHAR(200));
    
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- 1. Lưu danh sách đơn sẽ xóa để gửi thông báo
        INSERT INTO @danhSachDonXoa (maDatLich, maHocVien, tenKhoaHoc)
        SELECT 
            d.maDatLich,
            d.maHocVien,
            k.tenKhoaHoc
        FROM DatLich d
        JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
        JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
        WHERE d.trangThai = N'Đặt trước'
          AND d.thoiGianHetHan IS NOT NULL
          AND d.thoiGianHetHan < GETDATE()
          AND NOT EXISTS (
              SELECT 1 FROM ThanhToan tt 
              WHERE tt.maDatLich = d.maDatLich AND tt.trangThai = 1
          );
        
        -- 2. Gửi thông báo cho các đơn bị hủy
        INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
        SELECT 
            maHocVien,
            N'⏰ Đơn đặt lịch đã hết hạn',
            N'Đơn đặt lịch học lớp "' + tenKhoaHoc + 
            N'" đã bị hủy do quá thời gian thanh toán (10 phút). ' +
            N'Vui lòng đặt lại nếu bạn vẫn muốn tham gia.',
            N'HetHan'
        FROM @danhSachDonXoa;
        
        -- 3. Xóa các giao dịch thanh toán
        DELETE FROM ThanhToan
        WHERE maDatLich IN (SELECT maDatLich FROM @danhSachDonXoa);
        
        -- 4. Xóa các đơn hết hạn
        DELETE FROM DatLich
        WHERE maDatLich IN (SELECT maDatLich FROM @danhSachDonXoa);
        
        DECLARE @soLuongXoa INT = (SELECT COUNT(*) FROM @danhSachDonXoa);
        
        COMMIT TRANSACTION;
        
        SELECT 
            @soLuongXoa AS soLuongXoa, 
            N'SUCCESS' AS ketQua,
            N'Đã xóa ' + CAST(@soLuongXoa AS NVARCHAR) + N' đơn hết hạn' AS thongBao;
        
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
            
        SELECT 0 AS soLuongXoa, N'ERROR' AS ketQua, ERROR_MESSAGE() AS thongBao;
    END CATCH
END;
GO

---------------------------------------------------------------------
-- SP 5: Cập nhật đơn hoàn thành (đã qua thời gian học)
---------------------------------------------------------------------
CREATE PROCEDURE sp_CapNhatDonHoanThanh
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @soLuongCapNhat INT = 0;
    
    BEGIN TRY
        -- Cập nhật đơn từ "Đặt trước" -> "Đã hoàn thành" nếu:
        -- 1. Đã thanh toán (có record trong ThanhToan với trangThai = 1)
        -- 2. Đã qua thời gian học
        UPDATE d
        SET trangThai = N'Đã hoàn thành'
        FROM DatLich d
        JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
        WHERE d.trangThai = N'Đặt trước'
          AND EXISTS (
              SELECT 1 FROM ThanhToan tt 
              WHERE tt.maDatLich = d.maDatLich AND tt.trangThai = 1
          )
          AND (
              -- Đã qua ngày học
              d.ngayThamGia < CAST(GETDATE() AS DATE)
              OR 
              -- Hoặc cùng ngày nhưng đã qua giờ kết thúc
              (d.ngayThamGia = CAST(GETDATE() AS DATE) 
               AND lt.gioKetThuc < CAST(GETDATE() AS TIME))
          );
        
        SET @soLuongCapNhat = @@ROWCOUNT;
        
        SELECT 
            @soLuongCapNhat AS soLuongCapNhat, 
            N'SUCCESS' AS ketQua,
            N'Đã cập nhật ' + CAST(@soLuongCapNhat AS NVARCHAR) + N' đơn sang trạng thái Hoàn thành' AS thongBao;
        
    END TRY
    BEGIN CATCH
        SELECT 0 AS soLuongCapNhat, N'ERROR' AS ketQua, ERROR_MESSAGE() AS thongBao;
    END CATCH
END;
GO

---------------------------------------------------------------------
-- SP 6: Hủy đơn đặt lịch
---------------------------------------------------------------------
CREATE PROCEDURE sp_HuyDonDatLich
    @maDatLich INT,
    @lyDoHuy NVARCHAR(MAX) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @trangThai NVARCHAR(30);
    DECLARE @ngayThamGia DATE;
    DECLARE @gioBatDau TIME;
    DECLARE @daThanhToan BIT;
    
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- 1. Lấy thông tin đơn
        SELECT 
            @trangThai = d.trangThai,
            @ngayThamGia = d.ngayThamGia,
            @gioBatDau = lt.gioBatDau,
            @daThanhToan = CASE WHEN EXISTS(
                SELECT 1 FROM ThanhToan tt 
                WHERE tt.maDatLich = d.maDatLich AND tt.trangThai = 1
            ) THEN 1 ELSE 0 END
        FROM DatLich d
        JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
        WHERE d.maDatLich = @maDatLich;
        
        -- 2. Kiểm tra đơn có tồn tại không
        IF @trangThai IS NULL
        BEGIN
            SELECT N'ERROR' AS ketQua, N'Không tìm thấy đơn đặt lịch' AS thongBao;
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- 3. Kiểm tra đơn đã hoàn thành hoặc đã hủy rồi
        IF @trangThai IN (N'Đã hoàn thành', N'Đã huỷ')
        BEGIN
            SELECT N'ERROR' AS ketQua, N'Không thể hủy đơn ở trạng thái: ' + @trangThai AS thongBao;
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- 4. Nếu chưa thanh toán: Xóa luôn
        IF @daThanhToan = 0
        BEGIN
            DELETE FROM ThanhToan WHERE maDatLich = @maDatLich;
            DELETE FROM DatLich WHERE maDatLich = @maDatLich;
            
            COMMIT TRANSACTION;
            
            SELECT 
                N'DELETED' AS ketQua, 
                N'Đã xóa đơn chưa thanh toán' AS thongBao;
            RETURN;
        END
        
        -- 5. Nếu đã thanh toán: Kiểm tra thời gian
        DECLARE @thoiGianLop DATETIME = CAST(CAST(@ngayThamGia AS VARCHAR) + ' ' + CAST(@gioBatDau AS VARCHAR) AS DATETIME);
        DECLARE @thoiGianHuyToiDa DATETIME = DATEADD(MINUTE, -15, @thoiGianLop);
        
        IF GETDATE() >= @thoiGianHuyToiDa
        BEGIN
            SELECT 
                N'ERROR' AS ketQua, 
                N'Không thể hủy đơn. Chỉ được hủy trước thời gian lớp học 15 phút.' AS thongBao;
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- 6. Cập nhật trạng thái thành Đã Hủy
        UPDATE DatLich
        SET 
            trangThai = N'Đã huỷ',
            ghiChu = ISNULL(@lyDoHuy, N'Khách hàng hủy đơn')
        WHERE maDatLich = @maDatLich;
        
        COMMIT TRANSACTION;
        
        SELECT 
            N'CANCELLED' AS ketQua, 
            N'Đã hủy đơn thành công. Số tiền sẽ được hoàn lại trong 3-5 ngày làm việc.' AS thongBao;
        
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
            
        SELECT N'ERROR' AS ketQua, ERROR_MESSAGE() AS thongBao;
    END CATCH
END;
GO

---------------------------------------------------------------------
-- SP 7: Lấy danh sách đơn "Đặt trước"
---------------------------------------------------------------------
CREATE PROCEDURE sp_LayDonDatTruoc
    @maHocVien INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        d.maDatLich,
        d.ngayThamGia,
        d.soLuongNguoi,
        d.tongTien,
        d.soTienGiam,
        d.trangThai,
        d.ngayDat,
        d.thoiGianHetHan,
        
        k.maKhoaHoc,
        k.tenKhoaHoc,
        k.hinhAnh,
        k.giaTien,
        
        lt.gioBatDau,
        lt.gioKetThuc,
        lt.diaDiem,
        
        tt.trangThai AS daThanhToan,
        tt.transId,
        tt.ngayThanhToan,
        tt.orderId,
        tt.message AS thongBaoThanhToan,
        tt.payUrl,
        
        -- Tính thời gian còn lại để thanh toán (phút)
        CASE 
            WHEN d.thoiGianHetHan IS NOT NULL AND tt.trangThai = 0
            THEN DATEDIFF(MINUTE, GETDATE(), d.thoiGianHetHan)
            ELSE NULL
        END AS phutConLai,
        
        -- Kiểm tra có thể hủy không (trước giờ học 15 phút)
        CASE 
            WHEN DATEADD(MINUTE, -15, 
                    CAST(CAST(d.ngayThamGia AS VARCHAR) + ' ' + CAST(lt.gioBatDau AS VARCHAR) AS DATETIME)
                 ) > GETDATE()
            THEN 1
            ELSE 0
        END AS coTheHuy
        
    FROM DatLich d
    JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
    JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
    LEFT JOIN ThanhToan tt ON d.maDatLich = tt.maDatLich
    
    WHERE d.maHocVien = @maHocVien
        AND d.trangThai = N'Đặt trước'
    
    ORDER BY d.ngayDat DESC;
END;
GO

---------------------------------------------------------------------
-- 3. FUNCTION: Kiểm tra đơn đặt lịch đã được đánh giá chưa
-- Trả về: 1 = đã đánh giá, 0 = chưa đánh giá
---------------------------------------------------------------------
IF OBJECT_ID('fn_DaDanhGia', 'FN') IS NOT NULL
    DROP FUNCTION fn_DaDanhGia;
GO

CREATE FUNCTION fn_DaDanhGia(@maDatLich INT)
RETURNS BIT
AS
BEGIN
    DECLARE @result BIT = 0;
    
    IF EXISTS (SELECT 1 FROM DanhGia WHERE maDatLich = @maDatLich)
        SET @result = 1;
    
    RETURN @result;
END;
GO

---------------------------------------------------------------------
-- 4. STORED PROCEDURE: Kiểm tra trạng thái đánh giá của đơn
-- Trả về thông tin chi tiết
---------------------------------------------------------------------
IF OBJECT_ID('sp_KiemTraDaDanhGia', 'P') IS NOT NULL
    DROP PROCEDURE sp_KiemTraDaDanhGia;
GO

CREATE PROCEDURE sp_KiemTraDaDanhGia
    @maDatLich INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        dl.maDatLich,
        dl.trangThai AS trangThaiDon,
        CASE 
            WHEN dg.maDanhGia IS NOT NULL THEN 1 
            ELSE 0 
        END AS daDanhGia,
        dg.maDanhGia,
        dg.diemDanhGia,
        dg.binhLuan,
        dg.ngayDanhGia,
        CASE 
            WHEN dl.trangThai = N'Đã hoàn thành' AND dg.maDanhGia IS NULL THEN N'CHƯA ĐÁNH GIÁ'
            WHEN dl.trangThai = N'Đã hoàn thành' AND dg.maDanhGia IS NOT NULL THEN N'ĐÃ ĐÁNH GIÁ'
            ELSE N'KHÔNG THỂ ĐÁNH GIÁ'
        END AS trangThaiDanhGia
    FROM DatLich dl
    LEFT JOIN DanhGia dg ON dl.maDatLich = dg.maDatLich
    WHERE dl.maDatLich = @maDatLich;
END;
GO

---------------------------------------------------------------------
-- 5. STORED PROCEDURE: Lấy đánh giá theo maDatLich
---------------------------------------------------------------------
IF OBJECT_ID('sp_LayDanhGiaTheoDatLich', 'P') IS NOT NULL
    DROP PROCEDURE sp_LayDanhGiaTheoDatLich;
GO

CREATE PROCEDURE sp_LayDanhGiaTheoDatLich
    @maDatLich INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        dg.maDanhGia,
        dg.maHocVien,
        nd.hoTen AS tenHocVien,
        dg.maKhoaHoc,
        kh.tenKhoaHoc,
        dg.maDatLich,
        dg.diemDanhGia,
        dg.binhLuan,
        dg.ngayDanhGia,
        (SELECT duongDan, loaiFile, thuTu 
         FROM HinhAnhDanhGia 
         WHERE maDanhGia = dg.maDanhGia 
         ORDER BY thuTu
         FOR JSON PATH) AS hinhAnhJson
    FROM DanhGia dg
    JOIN NguoiDung nd ON dg.maHocVien = nd.maNguoiDung
    JOIN KhoaHoc kh ON dg.maKhoaHoc = kh.maKhoaHoc
    WHERE dg.maDatLich = @maDatLich;
END;
GO

---------------------------------------------------------------------
-- 6. STORED PROCEDURE: Tạo đánh giá mới
---------------------------------------------------------------------
IF OBJECT_ID('sp_TaoDanhGia', 'P') IS NOT NULL
    DROP PROCEDURE sp_TaoDanhGia;
GO

CREATE PROCEDURE sp_TaoDanhGia
    @maDatLich INT,
    @diemDanhGia INT,
    @binhLuan NVARCHAR(MAX),
    @maDanhGia INT OUTPUT,
    @ketQua NVARCHAR(100) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra đơn đặt lịch tồn tại
    IF NOT EXISTS (SELECT 1 FROM DatLich WHERE maDatLich = @maDatLich)
    BEGIN
        SET @ketQua = N'Đơn đặt lịch không tồn tại';
        SET @maDanhGia = 0;
        RETURN;
    END
    
    -- Kiểm tra trạng thái đơn
    DECLARE @trangThai NVARCHAR(30);
    DECLARE @maHocVien INT;
    DECLARE @maKhoaHoc INT;
    
    SELECT 
        @trangThai = dl.trangThai,
        @maHocVien = dl.maHocVien,
        @maKhoaHoc = lt.maKhoaHoc
    FROM DatLich dl
    JOIN LichTrinhLopHoc lt ON dl.maLichTrinh = lt.maLichTrinh
    WHERE dl.maDatLich = @maDatLich;
    
    IF @trangThai <> N'Đã hoàn thành'
    BEGIN
        SET @ketQua = N'Chỉ được đánh giá khi lớp học đã hoàn thành';
        SET @maDanhGia = 0;
        RETURN;
    END
    
    -- Kiểm tra đã đánh giá chưa
    IF EXISTS (SELECT 1 FROM DanhGia WHERE maDatLich = @maDatLich)
    BEGIN
        SET @ketQua = N'Lớp học này đã được đánh giá';
        SET @maDanhGia = 0;
        RETURN;
    END
    
    -- Tạo đánh giá
    INSERT INTO DanhGia (maHocVien, maKhoaHoc, maDatLich, diemDanhGia, binhLuan, ngayDanhGia)
    VALUES (@maHocVien, @maKhoaHoc, @maDatLich, @diemDanhGia, @binhLuan, GETDATE());
    
    SET @maDanhGia = SCOPE_IDENTITY();
    SET @ketQua = N'Đánh giá lớp học thành công';
END;
GO

---------------------------------------------------------------------
-- DỮ LIỆU TEST: ĐƠN ĐÃ HOÀN THÀNH (cho user maHocVien = 5)
---------------------------------------------------------------------

-- Đơn 1: Ẩm thực phố cổ Hà Nội
INSERT INTO DatLich (maHocVien, maLichTrinh, soLuongNguoi, ngayThamGia, trangThai, ngayDat, thoiGianHetHan, thoiGianHuy, tongTien)
VALUES (5, 1, 2, '2024-11-15', N'Đã hoàn thành', '2024-11-10 10:00:00', NULL, NULL, 1300000);

INSERT INTO ThanhToan (maDatLich, soTien, phuongThuc, trangThai, orderId, transId, ngayThanhToan, thoiGianTao)
VALUES (SCOPE_IDENTITY(), 1300000, N'Momo', 1, 'ORDER_TEST_001', 'TRANS_TEST_001', '2024-11-10 10:05:00', '2024-11-10 10:00:00');

-- Đơn 2: Tinh Hoa Cung Đình Huế
INSERT INTO DatLich (maHocVien, maLichTrinh, soLuongNguoi, ngayThamGia, trangThai, ngayDat, thoiGianHetHan, tongTien)
VALUES (5, 5, 1, '2024-10-20', N'Đã hoàn thành', '2024-10-15 14:00:00', NULL, 715000);

INSERT INTO ThanhToan (maDatLich, soTien, phuongThuc, trangThai, orderId, transId, ngayThanhToan, thoiGianTao)
VALUES (SCOPE_IDENTITY(), 715000, N'Momo', 1, 'ORDER_TEST_002', 'TRANS_TEST_002', '2024-10-15 14:05:00', '2024-10-15 14:00:00');

-- Đơn 3: Bánh Xèo và Nem Lụi Đà Nẵng
INSERT INTO DatLich (maHocVien, maLichTrinh, soLuongNguoi, ngayThamGia, trangThai, ngayDat, thoiGianHetHan, tongTien)
VALUES (5, 10, 3, '2024-09-10', N'Đã hoàn thành', '2024-09-05 09:00:00', NULL, 1770000);

INSERT INTO ThanhToan (maDatLich, soTien, phuongThuc, trangThai, orderId, transId, ngayThanhToan, thoiGianTao)
VALUES (SCOPE_IDENTITY(), 1770000, N'Momo', 1, 'ORDER_TEST_003', 'TRANS_TEST_003', '2024-09-05 09:05:00', '2024-09-05 09:00:00');

PRINT N'✅ Đã thực thi thành công !';
select * from ThanhToan
select * from DatLich
select * from NguoiDung
select * from MonAn

