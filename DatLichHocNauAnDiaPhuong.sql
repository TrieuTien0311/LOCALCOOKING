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
