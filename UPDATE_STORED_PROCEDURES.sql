-- Script để update stored procedures đã sửa
-- Chạy script này trong SQL Server Management Studio

USE DatLichHocNauAn;
GO

-- Xóa procedure cũ nếu tồn tại
IF OBJECT_ID('sp_LayDanhSachLopTheoNgay', 'P') IS NOT NULL
    DROP PROCEDURE sp_LayDanhSachLopTheoNgay;
GO

-- Tạo lại procedure với logic đúng (SUM thay vì COUNT)
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
        
        -- Tổng số người đã đặt trong ngày hôm đó (Bỏ qua đơn đã hủy)
        ISNULL(SUM(d.soLuongNguoi), 0) AS DaDat,
        
        -- Tính số chỗ còn trống = Tổng chỗ - Tổng số người đã đặt
        (lt.soLuongToiDa - ISNULL(SUM(d.soLuongNguoi), 0)) AS ConTrong,
        
        -- Trả về trạng thái text
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
            (@ThuCuaNgay = 1 AND (CHARINDEX('CN', lt.thuTrongTuan) > 0 OR CHARINDEX('1', lt.thuTrongTuan) > 0))
            OR
            CHARINDEX(CAST(@ThuCuaNgay AS VARCHAR), lt.thuTrongTuan) > 0
        )
        AND lt.trangThai = 1
        
    GROUP BY 
        k.maKhoaHoc, k.tenKhoaHoc, k.hinhAnh, k.giaTien, k.saoTrungBinh, k.soLuongDanhGia,
        lt.maLichTrinh, lt.gioBatDau, lt.gioKetThuc, lt.diaDiem, lt.soLuongToiDa;
END;
GO

-- Xóa procedure thứ 2
IF OBJECT_ID('sp_KiemTraChoTrong', 'P') IS NOT NULL
    DROP PROCEDURE sp_KiemTraChoTrong;
GO

-- Tạo lại procedure thứ 2
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

PRINT N'✓ Đã update stored procedures thành công!';
PRINT N'';
PRINT N'Thay đổi:';
PRINT N'- COUNT(d.maDatLich) → SUM(d.soLuongNguoi)';
PRINT N'- Bây giờ sẽ tính đúng số người đã đặt, không chỉ đếm số đơn';
