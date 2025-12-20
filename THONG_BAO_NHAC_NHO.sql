---------------------------------------------------------------------
-- THÔNG BÁO NHẮC NHỞ LỚP HỌC
-- 1. Trước 1 ngày: "Lớp học sắp diễn ra"
-- 2. Trước 30 phút: "Còn 30 phút nữa sẽ bắt đầu lớp học"
---------------------------------------------------------------------

USE DatLichHocNauAn;
GO

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
-- TEST: Chạy thử các procedure
---------------------------------------------------------------------
-- EXEC sp_ThongBaoTruoc1Ngay;
-- EXEC sp_ThongBaoTruoc30Phut;

---------------------------------------------------------------------
-- HƯỚNG DẪN TẠO SQL SERVER AGENT JOB (Chạy tự động)
---------------------------------------------------------------------
/*
Cách 1: Sử dụng SQL Server Agent (Yêu cầu SQL Server Standard/Enterprise)

-- Job 1: Chạy mỗi ngày lúc 8:00 sáng để gửi thông báo trước 1 ngày
USE msdb;
GO
EXEC sp_add_job @job_name = N'ThongBao_Truoc1Ngay';
EXEC sp_add_jobstep @job_name = N'ThongBao_Truoc1Ngay',
    @step_name = N'Tao thong bao',
    @subsystem = N'TSQL',
    @command = N'EXEC DatLichHocNauAn.dbo.sp_ThongBaoTruoc1Ngay',
    @database_name = N'DatLichHocNauAn';
EXEC sp_add_schedule @schedule_name = N'Daily_8AM',
    @freq_type = 4, -- Daily
    @freq_interval = 1,
    @active_start_time = 080000; -- 8:00 AM
EXEC sp_attach_schedule @job_name = N'ThongBao_Truoc1Ngay', @schedule_name = N'Daily_8AM';
EXEC sp_add_jobserver @job_name = N'ThongBao_Truoc1Ngay';

-- Job 2: Chạy mỗi 5 phút để kiểm tra thông báo 30 phút
USE msdb;
GO
EXEC sp_add_job @job_name = N'ThongBao_Truoc30Phut';
EXEC sp_add_jobstep @job_name = N'ThongBao_Truoc30Phut',
    @step_name = N'Tao thong bao',
    @subsystem = N'TSQL',
    @command = N'EXEC DatLichHocNauAn.dbo.sp_ThongBaoTruoc30Phut',
    @database_name = N'DatLichHocNauAn';
EXEC sp_add_schedule @schedule_name = N'Every_5Min',
    @freq_type = 4,
    @freq_interval = 1,
    @freq_subday_type = 4, -- Minutes
    @freq_subday_interval = 5;
EXEC sp_attach_schedule @job_name = N'ThongBao_Truoc30Phut', @schedule_name = N'Every_5Min';
EXEC sp_add_jobserver @job_name = N'ThongBao_Truoc30Phut';
*/

---------------------------------------------------------------------
-- Cách 2: Gọi từ Backend Spring Boot (Khuyến nghị)
-- Tạo Scheduled Task trong Spring Boot để gọi các procedure này
---------------------------------------------------------------------

PRINT N'Đã tạo xong các Stored Procedure nhắc nhở!';
PRINT N'- sp_ThongBaoTruoc1Ngay: Gọi mỗi ngày lúc 8:00 sáng';
PRINT N'- sp_ThongBaoTruoc30Phut: Gọi mỗi 5 phút';
GO
