-- =====================================================
-- INSERT DỮ LIỆU MẪU CHO BẢNG THÔNG BÁO
-- =====================================================

USE DatLichHocNauAn;
GO

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM ThongBao;
GO

-- Thêm dữ liệu mẫu cho người dùng ID = 4 (Ngô Thị Thảo Vy)
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh, daDoc, ngayTao) VALUES
-- Thông báo chưa đọc (mới nhất)
(4, 
 N'Lớp học sắp diễn ra', 
 N'Lớp "Ẩm thực phố cổ Hà Nội" của bạn sẽ bắt đầu vào ngày mai lúc 18:30. Đừng quên nhé!', 
 N'LichHoc', 
 N'hue.jpg', 
 0, 
 DATEADD(MINUTE, -15, GETDATE())),

(4, 
 N'Ưu đãi đặc biệt', 
 N'Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10. Áp dụng đến hết ngày 31/10', 
 N'UuDai', 
 N'voucher.png', 
 0, 
 DATEADD(HOUR, -3, GETDATE())),

-- Thông báo đã đọc
(4, 
 N'Đặt lịch thành công', 
 N'Chúc mừng! Bạn đã đặt chỗ thành công cho lớp "Ẩm thực phố cổ Hà Nội" vào 17:30 ngày 22 tháng 12 năm 2025.', 
 N'DatLich', 
 N'hue.jpg', 
 1, 
 DATEADD(HOUR, -1, GETDATE())),

(4, 
 N'Lớp học bị hủy', 
 N'Rất tiếc! Lớp học "Ẩm thực thủ đô Hà Nội" ngày 15 tháng 9 đã bị hủy do không đủ số lượng học viên. Học phí sẽ được hoàn lại vào tài khoản của bạn trong 3-5 ngày làm việc.', 
 N'HuyLop', 
 N'hue.jpg', 
 1, 
 DATEADD(DAY, -1, GETDATE())),

(4, 
 N'Chứng chỉ đã sẵn sàng', 
 N'Chúc mừng! Chứng chỉ hoàn thành khóa học "Ẩm thực cung đình Huế" của bạn đã sẵn sàng để tải xuống. Vui lòng truy cập mục "Hồ sơ" để tải về.', 
 N'ChungChi', 
 N'hue.jpg', 
 1, 
 DATEADD(DAY, -1, GETDATE()));

-- Thêm dữ liệu cho người dùng ID = 5 (Nguyễn Triều Tiên)
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh, daDoc, ngayTao) VALUES
(5, 
 N'Xác nhận đặt lịch', 
 N'Đơn đặt lịch của bạn cho lớp "Ẩm thực cung đình Huế" đang được xử lý. Chúng tôi sẽ thông báo khi đơn được xác nhận.', 
 N'DatLich', 
 N'hue.jpg', 
 0, 
 DATEADD(MINUTE, -30, GETDATE())),

(5, 
 N'Khuyến mãi cuối năm', 
 N'Mừng năm mới! Giảm 30% cho tất cả các khóa học khi đăng ký trước ngày 31/12. Đừng bỏ lỡ!', 
 N'UuDai', 
 N'voucher.png', 
 0, 
 DATEADD(HOUR, -5, GETDATE()));

-- Thêm dữ liệu cho người dùng ID = 6 (Nguyễn Thị Thương)
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh, daDoc, ngayTao) VALUES
(6, 
 N'Nhắc nhở thanh toán', 
 N'Bạn có một đơn đặt lịch chưa thanh toán cho lớp "Hải sản Đà Nẵng". Vui lòng hoàn tất thanh toán trước 24 giờ để giữ chỗ.', 
 N'HeThong', 
 N'phobo.png', 
 0, 
 DATEADD(HOUR, -2, GETDATE())),

(6, 
 N'Đánh giá lớp học', 
 N'Bạn đã hoàn thành lớp "Hải sản Đà Nẵng". Hãy chia sẻ trải nghiệm của bạn để giúp học viên khác!', 
 N'HeThong', 
 N'phobo.png', 
 1, 
 DATEADD(DAY, -2, GETDATE()));

-- Thông báo hệ thống cho tất cả người dùng (broadcast)
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh, daDoc, ngayTao) VALUES
(4, 
 N'Cập nhật ứng dụng', 
 N'Phiên bản mới của ứng dụng đã có sẵn với nhiều tính năng hấp dẫn. Vui lòng cập nhật để trải nghiệm tốt nhất!', 
 N'HeThong', 
 N'logo.png', 
 0, 
 DATEADD(DAY, -3, GETDATE())),

(5, 
 N'Cập nhật ứng dụng', 
 N'Phiên bản mới của ứng dụng đã có sẵn với nhiều tính năng hấp dẫn. Vui lòng cập nhật để trải nghiệm tốt nhất!', 
 N'HeThong', 
 N'logo.png', 
 1, 
 DATEADD(DAY, -3, GETDATE())),

(6, 
 N'Cập nhật ứng dụng', 
 N'Phiên bản mới của ứng dụng đã có sẵn với nhiều tính năng hấp dẫn. Vui lòng cập nhật để trải nghiệm tốt nhất!', 
 N'HeThong', 
 N'logo.png', 
 0, 
 DATEADD(DAY, -3, GETDATE()));

PRINT N'✓ Đã thêm dữ liệu mẫu cho bảng ThongBao!';
GO

-- Kiểm tra dữ liệu
SELECT 
    tb.maThongBao,
    nd.hoTen AS nguoiNhan,
    tb.tieuDe,
    tb.loaiThongBao,
    tb.daDoc,
    tb.ngayTao
FROM ThongBao tb
JOIN NguoiDung nd ON tb.maNguoiNhan = nd.maNguoiDung
ORDER BY tb.ngayTao DESC;
GO
