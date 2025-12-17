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
    vaiTro NVARCHAR(20) DEFAULT N'HocVien', -- HocVien | GiaoVien | Admin
    trangThai NVARCHAR(20) DEFAULT N'HoatDong', -- HoatDong | BiKhoa
    ngayTao DATETIME DEFAULT GETDATE(),
    lanCapNhatCuoi DATETIME DEFAULT GETDATE()
);

-- 2. OTP
CREATE TABLE OTP (
    maOTP INT PRIMARY KEY IDENTITY(1,1),
    maNguoiDung INT,
    maXacThuc VARCHAR(6) NOT NULL,
    loaiOTP NVARCHAR(30) NOT NULL, -- DangKy | QuenMatKhau | XacThuc
    thoiGianTao DATETIME DEFAULT GETDATE(),
    thoiGianHetHan DATETIME NOT NULL,
    daSuDung BIT DEFAULT 0,
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
);

-- 3. GIÁO VIÊN (Thông tin chi tiết giáo viên)
CREATE TABLE GiaoVien (
    maGiaoVien INT PRIMARY KEY IDENTITY(1,1),
    maNguoiDung INT UNIQUE NOT NULL,
    chuyenMon NVARCHAR(200),
    kinhNghiem NVARCHAR(MAX),
    moTa NVARCHAR(MAX),
    hinhAnh VARCHAR(255),
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung)
);

-- 4. LỚP HỌC
CREATE TABLE LopHoc (
    maLopHoc INT PRIMARY KEY IDENTITY(1,1),
    tenLopHoc NVARCHAR(200) NOT NULL,
    moTa NVARCHAR(MAX),
    maGiaoVien INT,
    tenGiaoVien NVARCHAR(100),
    soLuongToiDa INT DEFAULT 20,
    soLuongHienTai INT DEFAULT 0,
    giaTien DECIMAL(10,2) NOT NULL,
    thoiGian NVARCHAR(100),
    diaDiem NVARCHAR(255),
    trangThai NVARCHAR(30) DEFAULT N'Sắp diễn ra',
    ngayDienRa DATE,
    gioBatDau TIME,
    gioKetThuc TIME,
    hinhAnh VARCHAR(255),
    coUuDai BIT DEFAULT 0,
    ngayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (maGiaoVien) REFERENCES GiaoVien(maGiaoVien)
);

-- 5. DANH MỤC MÓN ĂN (Category trong FE)
CREATE TABLE DanhMucMonAn (
    maDanhMuc INT PRIMARY KEY IDENTITY(1,1),
    maLopHoc INT NOT NULL,
    tenDanhMuc NVARCHAR(100) NOT NULL, -- Món khai vị, Món chính, Món tráng miệng
    thoiGian NVARCHAR(50), -- 14:00 - 15:00
    iconDanhMuc VARCHAR(255),
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- 6. MÓN ĂN (Food trong FE)
CREATE TABLE MonAn (
    maMonAn INT PRIMARY KEY IDENTITY(1,1),
    maDanhMuc INT NOT NULL,
    tenMon NVARCHAR(200) NOT NULL,
    gioiThieu NVARCHAR(MAX),
    nguyenLieu NVARCHAR(MAX),
    hinhAnh VARCHAR(255),
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucMonAn(maDanhMuc)
);

-- 7. HÌNH ẢNH LỚP HỌC (Nhiều ảnh cho 1 lớp)
CREATE TABLE HinhAnhLopHoc (
    maHinhAnh INT PRIMARY KEY IDENTITY(1,1),
    maLopHoc INT NOT NULL,
    duongDan VARCHAR(255) NOT NULL,
    thuTu INT DEFAULT 1,
    FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc)
);

-- 8. LỊCH HỌC
CREATE TABLE LichHoc (
    maLichHoc INT PRIMARY KEY IDENTITY(1,1),
    maLopHoc INT NOT NULL,
    ngayHoc DATE NOT NULL,
    gioBatDau TIME NOT NULL,
    gioKetThuc TIME NOT NULL,
    noiDung NVARCHAR(500),
    trangThai NVARCHAR(20) DEFAULT N'Chưa Học',
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
    phuongThuc NVARCHAR(30) NOT NULL, -- ChuyenKhoan | MoMo | The
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
    loaiGiam NVARCHAR(20) NOT NULL, -- PhanTram | SoTien
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
-- TRIGGER 1: Tự động cập nhật số lượng học viên khi đặt lịch
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


-- TRIGGER 2: Cập nhật số lượng khi hủy đặt lịch
CREATE TRIGGER trg_CapNhatSoLuongHocVien_Update
ON DatLich
AFTER UPDATE
AS
BEGIN
    -- Từ DaDuyet → DaHuy
    UPDATE LopHoc
    SET soLuongHienTai = soLuongHienTai - 1
    FROM LopHoc lh
    JOIN deleted d ON lh.maLopHoc = d.maLopHoc
    JOIN inserted i ON d.maDatLich = i.maDatLich
    WHERE d.trangThai = N'Đã Duyệt'
      AND i.trangThai = N'Đã Hủy';

    -- Từ khác → DaDuyet
    UPDATE LopHoc
    SET soLuongHienTai = soLuongHienTai + 1
    FROM LopHoc lh
    JOIN deleted d ON lh.maLopHoc = d.maLopHoc
    JOIN inserted i ON d.maDatLich = i.maDatLich
    WHERE d.trangThai <> N'Đã Duyệt'
      AND i.trangThai = N'Đã Duyệt';
END;
GO

-- TRIGGER 3: Tạo thông báo khi đặt lịch mới
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

-- TRIGGER 4: Cập nhật số lượng ưu đãi đã sử dụng
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

-- TRIGGER 5: Tự động set hết hạn OTP
CREATE TRIGGER trg_OTP_HetHan
ON OTP
INSTEAD OF INSERT
AS
BEGIN
    INSERT INTO OTP (
        maNguoiDung,
        maXacThuc,
        loaiOTP,
        thoiGianTao,
        thoiGianHetHan,
        daSuDung
    )
    SELECT
        maNguoiDung,
        maXacThuc,
        loaiOTP,
        GETDATE(),
        DATEADD(MINUTE, 5, GETDATE()),
        daSuDung
    FROM inserted;
END;
GO
--------------------------------------------------------
-- THỦ TỤC 1: Đăng ký người dùng
GO
CREATE PROCEDURE sp_DangKyNguoiDung
    @TenDangNhap VARCHAR(50),
    @MatKhau VARCHAR(255),
    @HoTen NVARCHAR(100),
    @Email VARCHAR(100),
    @SoDienThoai VARCHAR(15)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        INSERT INTO NguoiDung (tenDangNhap, matKhau, hoTen, email, soDienThoai)
        VALUES (@TenDangNhap, @MatKhau, @HoTen, @Email, @SoDienThoai);

        COMMIT;
        SELECT N'Đăng ký thành công' AS ThongBao, SCOPE_IDENTITY() AS MaNguoiDung;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO

-- THỦ TỤC 2: Đặt lịch học
GO
CREATE PROCEDURE sp_DatLichHoc
    @MaHocVien INT,
    @MaLopHoc INT,
    @GhiChu NVARCHAR(MAX)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @SoLuongHienTai INT, @SoLuongToiDa INT;

    BEGIN TRY
        BEGIN TRAN;

        SELECT 
            @SoLuongHienTai = soLuongHienTai,
            @SoLuongToiDa = soLuongToiDa
        FROM LopHoc
        WHERE maLopHoc = @MaLopHoc;

        IF @SoLuongHienTai >= @SoLuongToiDa
        BEGIN
            ROLLBACK;
            SELECT N'Lớp học đã đầy' AS ThongBao;
            RETURN;
        END

        INSERT INTO DatLich (maHocVien, maLopHoc, ghiChu)
        VALUES (@MaHocVien, @MaLopHoc, @GhiChu);

        COMMIT;
        SELECT N'Đặt lịch thành công' AS ThongBao, SCOPE_IDENTITY() AS MaDatLich;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO

-- THỦ TỤC 3: Thanh toán
GO
CREATE PROCEDURE sp_ThanhToan
    @MaDatLich INT,
    @SoTien DECIMAL(10,2),
    @PhuongThuc NVARCHAR(30),
    @MaGiaoDich VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        INSERT INTO ThanhToan
        (maDatLich, soTien, phuongThuc, trangThai, ngayThanhToan, maGiaoDich)
        VALUES
        (@MaDatLich, @SoTien, @PhuongThuc, N'DaThanhToan', GETDATE(), @MaGiaoDich);

        UPDATE DatLich
        SET trangThai = N'DaDuyet'
        WHERE maDatLich = @MaDatLich;

        COMMIT;
        SELECT N'Thanh toán thành công' AS ThongBao, SCOPE_IDENTITY() AS MaThanhToan;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO

-- THỦ TỤC 4: Áp dụng mã ưu đãi
GO
CREATE PROCEDURE sp_ApDungUuDai
    @MaDatLich INT,
    @MaCode VARCHAR(50),
    @GiaGoc DECIMAL(10,2),
    @SoTienGiam DECIMAL(10,2) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaUuDai INT, @LoaiGiam NVARCHAR(20),
            @GiaTriGiam DECIMAL(10,2), @GiamToiDa DECIMAL(10,2),
            @SoLuong INT, @DaDung INT;

    SELECT TOP 1
        @MaUuDai = maUuDai,
        @LoaiGiam = loaiGiam,
        @GiaTriGiam = giaTriGiam,
        @GiamToiDa = giamToiDa,
        @SoLuong = soLuong,
        @DaDung = soLuongDaSuDung
    FROM UuDai
    WHERE maCode = @MaCode
      AND trangThai = N'HoatDong'
      AND GETDATE() BETWEEN ngayBatDau AND ngayKetThuc;

    IF @MaUuDai IS NULL OR (@SoLuong IS NOT NULL AND @DaDung >= @SoLuong)
    BEGIN
        SET @SoTienGiam = 0;
        RETURN;
    END

    IF @LoaiGiam = N'PhanTram'
    BEGIN
        SET @SoTienGiam = @GiaGoc * @GiaTriGiam / 100;
        IF @GiamToiDa IS NOT NULL AND @SoTienGiam > @GiamToiDa
            SET @SoTienGiam = @GiamToiDa;
    END
    ELSE
        SET @SoTienGiam = @GiaTriGiam;

    INSERT INTO LichSuUuDai (maUuDai, maDatLich, soTienGiam)
    VALUES (@MaUuDai, @MaDatLich, @SoTienGiam);
END;
GO

-- THỦ TỤC 5: Lấy danh sách lớp học (có phân trang, lọc)
CREATE PROCEDURE sp_LayDanhSachLopHoc
    @TrangThai NVARCHAR(30) = NULL,
    @TimKiem NVARCHAR(200) = NULL,
    @TrangHienTai INT = 1,
    @SoPhanTuTrenTrang INT = 10
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Offset INT;
    SET @Offset = (@TrangHienTai - 1) * @SoPhanTuTrenTrang;

    SELECT 
        l.*,
        n.hoTen AS TenGiaoVien,
        (SELECT AVG(diemDanhGia) 
         FROM DanhGia 
         WHERE maLopHoc = l.maLopHoc) AS DiemTrungBinh,
        (SELECT COUNT(*) 
         FROM DanhGia 
         WHERE maLopHoc = l.maLopHoc) AS SoLuotDanhGia
    FROM LopHoc l
    LEFT JOIN NguoiDung n ON l.maGiaoVien = n.maNguoiDung
    WHERE 
        (@TrangThai IS NULL OR l.trangThai = @TrangThai)
        AND (@TimKiem IS NULL OR l.tenLopHoc LIKE N'%' + @TimKiem + N'%')
    ORDER BY l.ngayTao DESC
    OFFSET @Offset ROWS
    FETCH NEXT @SoPhanTuTrenTrang ROWS ONLY;
END;
GO

-- THỦ TỤC 6: Tạo và gửi OTP
CREATE PROCEDURE sp_TaoOTP
    @MaNguoiDung INT,
    @LoaiOTP NVARCHAR(30),
    @MaXacThuc VARCHAR(6) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Tạo OTP 6 số ngẫu nhiên
    SET @MaXacThuc = RIGHT(
        '000000' + CAST(ABS(CHECKSUM(NEWID())) % 1000000 AS VARCHAR),
        6
    );

    BEGIN TRY
        BEGIN TRAN;

        -- Vô hiệu hóa OTP cũ
        UPDATE OTP
        SET daSuDung = 1
        WHERE maNguoiDung = @MaNguoiDung
          AND loaiOTP = @LoaiOTP
          AND daSuDung = 0;

        -- Thêm OTP mới
        INSERT INTO OTP (
            maNguoiDung,
            maXacThuc,
            loaiOTP,
            thoiGianTao,
            thoiGianHetHan,
            daSuDung
        )
        VALUES (
            @MaNguoiDung,
            @MaXacThuc,
            @LoaiOTP,
            GETDATE(),
            DATEADD(MINUTE, 5, GETDATE()),
            0
        );

        COMMIT;
        SELECT @MaXacThuc AS MaOTP;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        THROW;
    END CATCH
END;
GO

-- THỦ TỤC 7: Xác thực OTP
GO
CREATE PROCEDURE sp_XacThucOTP
    @MaNguoiDung INT,
    @MaXacThuc VARCHAR(6),
    @LoaiOTP NVARCHAR(30),
    @KetQua BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1 FROM OTP
        WHERE maNguoiDung = @MaNguoiDung
          AND maXacThuc = @MaXacThuc
          AND loaiOTP = @LoaiOTP
          AND daSuDung = 0
          AND thoiGianHetHan > GETDATE()
    )
    BEGIN
        UPDATE OTP
        SET daSuDung = 1
        WHERE maNguoiDung = @MaNguoiDung
          AND maXacThuc = @MaXacThuc;

        SET @KetQua = 1;
    END
    ELSE
        SET @KetQua = 0;

    SELECT @KetQua AS KetQua;
END;
GO

---------------------------------------------------------------
-- INSERT DATA MẪU
---------------------------------------------------------------

-- 1. NGƯỜI DÙNG (Admin, Giáo viên, Học viên)
INSERT INTO NguoiDung (tenDangNhap, matKhau, hoTen, email, soDienThoai, diaChi, vaiTro, trangThai) VALUES
(N'admin', N'admin123', N'Quản Trị Viên', N'admin@localcooking.vn', N'0901234567', N'123 Nguyễn Huệ, Q1, TP.HCM', N'Admin', N'HoatDong'),
(N'giaovien1', N'gv123', N'Nguyễn Văn An', N'nguyenvanan@gmail.com', N'0912345678', N'456 Lê Lợi, Q1, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'giaovien2', N'gv123', N'Trần Thị Bình', N'tranthibinh@gmail.com', N'0923456789', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'GiaoVien', N'HoatDong'),
(N'hocvien1', N'hv123', N'Lê Văn Cường', N'levancuong@gmail.com', N'0934567890', N'321 Võ Văn Tần, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'hocvien2', N'hv123', N'Phạm Thị Dung', N'phamthidung@gmail.com', N'0945678901', N'654 Hai Bà Trưng, Q3, TP.HCM', N'HocVien', N'HoatDong'),
(N'hocvien3', N'hv123', N'Hoàng Văn Em', N'hoangvanem@gmail.com', N'0956789012', N'987 Cách Mạng Tháng 8, Q10, TP.HCM', N'HocVien', N'HoatDong');

-- 2. GIÁO VIÊN
INSERT INTO GiaoVien (maNguoiDung, chuyenMon, kinhNghiem, moTa, hinhAnh) VALUES
(2, N'Ẩm thực Việt Nam truyền thống', N'15 năm kinh nghiệm, từng làm việc tại các nhà hàng 5 sao', N'Chuyên gia về các món ăn Việt Nam cổ truyền, đặc biệt là món Huế và Hà Nội', N'giaovien1.jpg'),
(3, N'Bánh ngọt và tráng miệng', N'10 năm kinh nghiệm, tốt nghiệp Le Cordon Bleu Paris', N'Chuyên về bánh Pháp, bánh Âu và các món tráng miệng hiện đại', N'giaovien2.jpg');

-- 3. LỚP HỌC
INSERT INTO LopHoc (tenLopHoc, moTa, maGiaoVien, tenGiaoVien, soLuongToiDa, soLuongHienTai, giaTien, thoiGian, diaDiem, trangThai, ngayDienRa, gioBatDau, gioKetThuc, hinhAnh, coUuDai) VALUES
(N'Nấu ăn Việt Nam cơ bản', N'Học cách nấu các món ăn Việt Nam truyền thống như phở, bún chả, nem rán', 1, N'Nguyễn Văn An', 20, 5, 500000, N'14:00 - 17:00', N'123 Nguyễn Huệ, Quận 1, TP.HCM', N'Sắp diễn ra', '2025-01-15', '14:00', '17:00', N'lophoc1.jpg', 1),
(N'Món Huế đặc sản', N'Khám phá ẩm thực cung đình Huế với bún bò Huế, bánh bèo, bánh nậm', 1, N'Nguyễn Văn An', 15, 3, 600000, N'09:00 - 12:00', N'123 Nguyễn Huệ, Quận 1, TP.HCM', N'Sắp diễn ra', '2025-01-20', '09:00', '12:00', N'lophoc2.jpg', 0),
(N'Bánh ngọt Pháp', N'Học làm croissant, macaron, éclair và các loại bánh Pháp khác', 2, N'Trần Thị Bình', 12, 8, 800000, N'14:00 - 18:00', N'456 Lê Lợi, Quận 1, TP.HCM', N'Sắp diễn ra', '2025-01-18', '14:00', '18:00', N'lophoc3.jpg', 1),
(N'Món ăn chay healthy', N'Các món ăn chay bổ dưỡng, phù hợp với người ăn kiêng', 1, N'Nguyễn Văn An', 20, 12, 450000, N'15:00 - 17:30', N'123 Nguyễn Huệ, Quận 1, TP.HCM', N'Sắp diễn ra', '2025-01-25', '15:00', '17:30', N'lophoc4.jpg', 0),
(N'Nấu ăn Ý cơ bản', N'Pizza, pasta, risotto và các món Ý phổ biến', 2, N'Trần Thị Bình', 15, 15, 700000, N'10:00 - 13:00', N'456 Lê Lợi, Quận 1, TP.HCM', N'Đã đầy', '2025-01-22', '10:00', '13:00', N'lophoc5.jpg', 0);

-- 4. DANH MỤC MÓN ĂN
-- Lớp 1: Nấu ăn Việt Nam cơ bản
INSERT INTO DanhMucMonAn (maLopHoc, tenDanhMuc, thoiGian, iconDanhMuc, thuTu) VALUES
(1, N'Món khai vị', N'14:00 - 15:00', N'ic_appetizer.png', 1),
(1, N'Món chính', N'15:00 - 16:30', N'ic_main_dish.png', 2),
(1, N'Món tráng miệng', N'16:30 - 17:00', N'ic_dessert.png', 3),
-- Lớp 2: Món Huế đặc sản
(2, N'Món khai vị', N'09:00 - 09:30', N'ic_appetizer.png', 1),
(2, N'Món chính', N'09:30 - 11:30', N'ic_main_dish.png', 2),
(2, N'Món tráng miệng', N'11:30 - 12:00', N'ic_dessert.png', 3),
-- Lớp 3: Bánh ngọt Pháp
(3, N'Bánh mặn', N'14:00 - 16:00', N'ic_bread.png', 1),
(3, N'Bánh ngọt', N'16:00 - 18:00', N'ic_dessert.png', 2);

-- 5. MÓN ĂN
-- Lớp 1: Nấu ăn Việt Nam cơ bản
INSERT INTO MonAn (maDanhMuc, tenMon, gioiThieu, nguyenLieu, hinhAnh) VALUES
(1, N'Gỏi cuốn', N'Món khai vị nhẹ nhàng, tươi mát với tôm, thịt và rau sống', N'Bánh tráng, tôm, thịt ba chỉ, bún, rau sống, nước chấm', N'goicuon.jpg'),
(1, N'Nem rán', N'Nem giòn rụm với nhân thịt và rau củ', N'Bánh đa nem, thịt lợn, mộc nhĩ, miến, cà rốt, hành tây', N'nemran.jpg'),
(2, N'Phở bò', N'Món phở truyền thống Hà Nội với nước dùng trong, thơm', N'Bánh phở, thịt bò, xương bò, hành, gừng, gia vị', N'phobo.jpg'),
(2, N'Bún chả', N'Đặc sản Hà Nội với chả nướng thơm lừng', N'Bún, thịt ba chỉ, thịt nạc, nước mắm, rau sống', N'buncha.jpg'),
(3, N'Chè ba màu', N'Món tráng miệng mát lạnh, ngọt ngào', N'Đậu đỏ, đậu xanh, thạch, nước cốt dừa, đá bào', N'chebamau.jpg'),
-- Lớp 2: Món Huế đặc sản
(4, N'Bánh bèo', N'Bánh bèo Huế truyền thống', N'Bột gạo, tôm khô, mỡ hành, nước mắm', N'banhbeo.jpg'),
(5, N'Bún bò Huế', N'Món bún đặc trưng của xứ Huế', N'Bún, thịt bò, chả, giò heo, sả, mắm ruốc', N'bunbohue.jpg'),
(5, N'Bánh nậm', N'Bánh nậm gói lá chuối', N'Bột gạo, tôm, thịt, lá chuối', N'banhnam.jpg'),
(6, N'Chè Huế', N'Chè truyền thống Huế ngọt thanh', N'Hạt sen, long nhãn, đường phèn', N'chehue.jpg'),
-- Lớp 3: Bánh ngọt Pháp
(7, N'Croissant', N'Bánh sừng bò Pháp giòn tan', N'Bột mì, bơ, men, sữa, trứng', N'croissant.jpg'),
(7, N'Pain au chocolat', N'Bánh sô-cô-la Pháp', N'Bột mì, bơ, chocolate, trứng', N'painauchocolat.jpg'),
(8, N'Macaron', N'Bánh macaron nhiều màu sắc', N'Bột hạnh nhân, đường, lòng trắng trứng, ganache', N'macaron.jpg'),
(8, N'Éclair', N'Bánh su kem dài Pháp', N'Bột mì, trứng, bơ, kem, chocolate', N'eclair.jpg'),
(8, N'Tarte aux fruits', N'Bánh tart trái cây', N'Bột mì, bơ, kem, trái cây tươi', N'tarte.jpg');

-- 6. HÌNH ẢNH LỚP HỌC
INSERT INTO HinhAnhLopHoc (maLopHoc, duongDan, thuTu) VALUES
(1, N'lophoc1_1.jpg', 1),
(1, N'lophoc1_2.jpg', 2),
(1, N'lophoc1_3.jpg', 3),
(2, N'lophoc2_1.jpg', 1),
(3, N'lophoc3_1.jpg', 1);

-- 7. ĐẶT LỊCH
INSERT INTO DatLich (maHocVien, maLopHoc, soLuongNguoi, tongTien, tenNguoiDat, emailNguoiDat, sdtNguoiDat, trangThai, ghiChu) VALUES
(4, 1, 1, 500000, N'Lê Văn Cường', N'levancuong@gmail.com', N'0934567890', N'Đã Duyệt', N'Muốn học kỹ món phở'),
(5, 1, 2, 1000000, N'Phạm Thị Dung', N'phamthidung@gmail.com', N'0945678901', N'Đã Duyệt', N'Đăng ký cho 2 người'),
(6, 3, 1, 800000, N'Hoàng Văn Em', N'hoangvanem@gmail.com', N'0956789012', N'Chờ Duyệt', NULL),
(4, 2, 1, 600000, N'Lê Văn Cường', N'levancuong@gmail.com', N'0934567890', N'Đã Duyệt', NULL);

-- 8. THANH TOÁN
INSERT INTO ThanhToan (maDatLich, soTien, phuongThuc, trangThai, ngayThanhToan, maGiaoDich) VALUES
(1, 500000, N'MoMo', N'Đã Thanh Toán', GETDATE(), N'MOMO123456789'),
(2, 1000000, N'ChuyenKhoan', N'Đã Thanh Toán', GETDATE(), N'CK987654321'),
(4, 600000, N'The', N'Đã Thanh Toán', GETDATE(), N'VISA456789123');

-- 9. ĐÁNH GIÁ
INSERT INTO DanhGia (maHocVien, maLopHoc, diemDanhGia, binhLuan) VALUES
(4, 1, 5, N'Lớp học rất hay, thầy dạy nhiệt tình và dễ hiểu'),
(5, 1, 4, N'Món ăn ngon, không gian thoải mái. Chỉ tiếc là thời gian hơi ngắn'),
(4, 2, 5, N'Món Huế rất đặc sắc, học được nhiều kỹ năng mới');

-- 10. THÔNG BÁO
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, daDoc) VALUES
(4, N'Đặt lịch thành công', N'Bạn đã đặt lịch học lớp Nấu ăn Việt Nam cơ bản thành công', N'LopHoc', 1),
(5, N'Đặt lịch thành công', N'Bạn đã đặt lịch học lớp Nấu ăn Việt Nam cơ bản thành công', N'LopHoc', 1),
(6, N'Đặt lịch thành công', N'Bạn đã đặt lịch học lớp Bánh ngọt Pháp thành công', N'LopHoc', 0),
(4, N'Ưu đãi đặc biệt', N'Giảm 20% cho lớp học Món Huế đặc sản. Đăng ký ngay!', N'UuDai', 0);

-- 11. YÊU THÍCH
INSERT INTO YeuThich (maHocVien, maLopHoc) VALUES
(4, 1),
(4, 2),
(5, 1),
(5, 3),
(6, 3);

-- 12. ƯU ĐÃI
INSERT INTO UuDai (maCode, tenUuDai, moTa, loaiGiam, giaTriGiam, giamToiDa, soLuong, soLuongDaSuDung, ngayBatDau, ngayKetThuc, hinhAnh, trangThai) VALUES
(N'GIAM20', N'Giảm 20% cho học viên mới', N'Áp dụng cho tất cả các lớp học, dành cho học viên đăng ký lần đầu', N'PhanTram', 20, 100000, 100, 15, '2025-01-01', '2025-02-28', N'voucher1.jpg', N'Hoạt Động'),
(N'TETAM', N'Ưu đãi Tết Âm lịch', N'Giảm 100.000đ cho đơn hàng từ 500.000đ', N'SoTien', 100000, NULL, 50, 8, '2025-01-15', '2025-02-15', N'voucher2.jpg', N'Hoạt Động'),
(N'FREESHIP', N'Miễn phí vận chuyển', N'Miễn phí giao nguyên liệu tận nhà', N'SoTien', 50000, 50000, 200, 45, '2025-01-01', '2025-03-31', N'voucher3.jpg', N'Hoạt Động');

-- 13. LỊCH SỬ ƯU ĐÃI
INSERT INTO LichSuUuDai (maUuDai, maDatLich, soTienGiam) VALUES
(1, 1, 100000),
(2, 2, 100000);

-- 14. LỊCH HỌC
INSERT INTO LichHoc (maLopHoc, ngayHoc, gioBatDau, gioKetThuc, noiDung, trangThai) VALUES
(1, '2025-01-15', '14:00', '15:00', N'Giới thiệu và chuẩn bị nguyên liệu', N'Chưa Học'),
(1, '2025-01-15', '15:00', '16:30', N'Thực hành nấu món chính', N'Chưa Học'),
(1, '2025-01-15', '16:30', '17:00', N'Hoàn thiện và thưởng thức', N'Chưa Học'),
(2, '2025-01-20', '09:00', '10:30', N'Học làm bún bò Huế', N'Chưa Học'),
(2, '2025-01-20', '10:30', '12:00', N'Học làm bánh bèo, bánh nậm', N'Chưa Học');

PRINT N'✓ Đã insert data mẫu thành công!';
PRINT N'- 6 người dùng (1 admin, 2 giáo viên, 3 học viên)';
PRINT N'- 5 lớp học';
PRINT N'- 5 món ăn';
PRINT N'- 4 đặt lịch';
PRINT N'- 3 đánh giá';
PRINT N'- 3 ưu đãi';
GO
