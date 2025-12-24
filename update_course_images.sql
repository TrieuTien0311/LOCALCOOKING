-- =====================================================
-- SCRIPT CẬP NHẬT HÌNH ẢNH KHÓA HỌC
-- Chạy script này trong SQL Server Management Studio
-- =====================================================

USE DatLichHocNauAn;
GO

-- =====================================================
-- PHẦN 1: CẬP NHẬT HÌNH ẢNH BANNER CHÍNH (KhoaHoc.hinhAnh)
-- Ảnh _1 là ảnh banner hiển thị chính
-- =====================================================

-- Khóa học 13: Hương Vị Miền Tây Sông Nước - ảnh 1 là .png
UPDATE KhoaHoc SET hinhAnh = N'huong_vi_mien_tay_song_nuoc_1.png' WHERE maKhoaHoc = 13;

-- Các khóa học khác đã đúng format _1.jpg trong INSERT gốc
-- Chỉ cần verify lại nếu cần:
/*
UPDATE KhoaHoc SET hinhAnh = N'am_thuc_pho_co_ha_noi_1.jpg' WHERE maKhoaHoc = 1;
UPDATE KhoaHoc SET hinhAnh = N'bun_va_mon_cuon_ha_thanh_1.jpg' WHERE maKhoaHoc = 2;
UPDATE KhoaHoc SET hinhAnh = N'banh_dan_gian_va_qua_que_bac_bo_1.jpg' WHERE maKhoaHoc = 3;
UPDATE KhoaHoc SET hinhAnh = N'mon_nhau_va_lai_rai_ha_noi_1.jpg' WHERE maKhoaHoc = 4;
UPDATE KhoaHoc SET hinhAnh = N'tinh_hoa_cung_dinh_hue_1.jpg' WHERE maKhoaHoc = 5;
UPDATE KhoaHoc SET hinhAnh = N'banh_hue_truyen_thong_1.jpg' WHERE maKhoaHoc = 6;
UPDATE KhoaHoc SET hinhAnh = N'am_thuc_chay_xu_hue_1.jpg' WHERE maKhoaHoc = 7;
UPDATE KhoaHoc SET hinhAnh = N'banh_trai_va_qua_chieu_co_do_1.jpg' WHERE maKhoaHoc = 8;
UPDATE KhoaHoc SET hinhAnh = N'dac_san_bien_da_nang_1.jpg' WHERE maKhoaHoc = 9;
UPDATE KhoaHoc SET hinhAnh = N'banh_xeo_va_nem_lui_da_nang_1.jpg' WHERE maKhoaHoc = 10;
UPDATE KhoaHoc SET hinhAnh = N'bun_mam_va_cha_ca_mien_trung_1.jpg' WHERE maKhoaHoc = 11;
UPDATE KhoaHoc SET hinhAnh = N'soi_banh_thu_cong_va_cao_lau_1.jpg' WHERE maKhoaHoc = 12;
UPDATE KhoaHoc SET hinhAnh = N'banh_xeo_va_banh_khot_nam_bo_1.jpg' WHERE maKhoaHoc = 14;
UPDATE KhoaHoc SET hinhAnh = N'hu_tieu_va_mon_ngon_phuong_nam_1.jpg' WHERE maKhoaHoc = 15;
UPDATE KhoaHoc SET hinhAnh = N'bien_tau_chuoi_va_che_nam_bo_1.jpg' WHERE maKhoaHoc = 16;
*/

-- =====================================================
-- PHẦN 2: THÊM ẢNH _1 VÀO BẢNG HinhAnhKhoaHoc (SLIDE ẢNH)
-- Hiện tại chỉ có _2 và _3, cần thêm _1 với thuTu = 0
-- =====================================================

-- Xóa dữ liệu cũ nếu muốn reset hoàn toàn
-- DELETE FROM HinhAnhKhoaHoc;

-- Thêm ảnh _1 cho tất cả khóa học (thuTu = 0 để hiển thị đầu tiên)
INSERT INTO HinhAnhKhoaHoc (maKhoaHoc, duongDan, thuTu) VALUES
-- Hà Nội
(1, N'am_thuc_pho_co_ha_noi_1.jpg', 0),
(2, N'bun_va_mon_cuon_ha_thanh_1.jpg', 0),
(3, N'banh_dan_gian_va_qua_que_bac_bo_1.jpg', 0),
(4, N'mon_nhau_va_lai_rai_ha_noi_1.jpg', 0),

-- Huế
(5, N'tinh_hoa_cung_dinh_hue_1.jpg', 0),
(6, N'banh_hue_truyen_thong_1.jpg', 0),
(7, N'am_thuc_chay_xu_hue_1.jpg', 0),
(8, N'banh_trai_va_qua_chieu_co_do_1.jpg', 0),

-- Đà Nẵng
(9, N'dac_san_bien_da_nang_1.jpg', 0),
(10, N'banh_xeo_va_nem_lui_da_nang_1.jpg', 0),
(11, N'bun_mam_va_cha_ca_mien_trung_1.jpg', 0),
(12, N'soi_banh_thu_cong_va_cao_lau_1.jpg', 0),

-- Cần Thơ (lưu ý khóa 13 là .png)
(13, N'huong_vi_mien_tay_song_nuoc_1.png', 0),
(14, N'banh_xeo_va_banh_khot_nam_bo_1.jpg', 0),
(15, N'hu_tieu_va_mon_ngon_phuong_nam_1.jpg', 0),
(16, N'bien_tau_chuoi_va_che_nam_bo_1.jpg', 0);

-- =====================================================
-- PHẦN 3: KIỂM TRA KẾT QUẢ
-- =====================================================

-- Xem tất cả hình ảnh khóa học
SELECT 
    kh.maKhoaHoc,
    kh.tenKhoaHoc,
    kh.hinhAnh AS 'Ảnh Banner',
    ha.duongDan AS 'Ảnh Slide',
    ha.thuTu
FROM KhoaHoc kh
LEFT JOIN HinhAnhKhoaHoc ha ON kh.maKhoaHoc = ha.maKhoaHoc
ORDER BY kh.maKhoaHoc, ha.thuTu;

-- Đếm số ảnh mỗi khóa học
SELECT 
    kh.maKhoaHoc,
    kh.tenKhoaHoc,
    COUNT(ha.maHinhAnh) AS 'Số ảnh slide'
FROM KhoaHoc kh
LEFT JOIN HinhAnhKhoaHoc ha ON kh.maKhoaHoc = ha.maKhoaHoc
GROUP BY kh.maKhoaHoc, kh.tenKhoaHoc
ORDER BY kh.maKhoaHoc;

PRINT N'✅ Cập nhật hình ảnh khóa học thành công!';
GO
