package com.android.be.service;

import com.android.be.dto.DonDatLichDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

@Service
public class DonDatLichService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Lấy danh sách đơn đã hoàn thành của học viên
     */
    public List<DonDatLichDTO> getDonHoanThanh(Integer maHocVien) {
        String sql = "SELECT " +
                "d.maDatLich, " +
                "d.maHocVien, " +
                "d.maLichTrinh, " +
                "d.soLuongNguoi, " +
                "d.ngayThamGia, " +
                "d.trangThai, " +
                "k.maKhoaHoc, " +
                "k.tenKhoaHoc, " +
                "k.hinhAnh, " +
                "k.moTa, " +
                "lt.diaDiem, " +
                "CONVERT(VARCHAR(5), lt.gioBatDau, 108) + ' - ' + CONVERT(VARCHAR(5), lt.gioKetThuc, 108) AS thoiGian, " +
                "tt.soTien AS tongTien, " +
                "tt.trangThai AS daThanhToan, " +
                "tt.ngayThanhToan, " +
                "CASE WHEN EXISTS(SELECT 1 FROM DanhGia dg WHERE dg.maHocVien = d.maHocVien AND dg.maKhoaHoc = k.maKhoaHoc) THEN 1 ELSE 0 END AS daDanhGia " +
                "FROM DatLich d " +
                "JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh " +
                "JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc " +
                "LEFT JOIN ThanhToan tt ON d.maDatLich = tt.maDatLich " +
                "WHERE d.maHocVien = ? " +
                "AND d.trangThai = N'Đã hoàn thành' " +
                "ORDER BY d.ngayThamGia DESC";

        return jdbcTemplate.query(sql, new Object[]{maHocVien}, (rs, rowNum) -> mapToDonDatLichDTO(rs));
    }

    /**
     * Lấy danh sách đơn đặt trước của học viên
     */
    public List<DonDatLichDTO> getDonDatTruoc(Integer maHocVien) {
        String sql = """
            SELECT 
                d.maDatLich,
                d.maHocVien,
                d.maLichTrinh,
                d.soLuongNguoi,
                d.ngayThamGia,
                d.trangThai,
                d.tenNguoiDat,
                d.emailNguoiDat,
                d.sdtNguoiDat,
                
                k.maKhoaHoc,
                k.tenKhoaHoc,
                k.hinhAnh,
                k.moTa,
                
                lt.diaDiem,
                CONVERT(VARCHAR(5), lt.gioBatDau, 108) + ' - ' + CONVERT(VARCHAR(5), lt.gioKetThuc, 108) AS thoiGian,
                
                tt.soTien AS tongTien,
                tt.trangThai AS daThanhToan,
                tt.ngayThanhToan,
                
                0 AS daDanhGia
                
            FROM DatLich d
            JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
            JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
            LEFT JOIN ThanhToan tt ON d.maDatLich = tt.maDatLich
            WHERE d.maHocVien = ?
              AND d.trangThai = N'Đặt trước'
            ORDER BY d.ngayThamGia ASC
            """;

        return jdbcTemplate.query(sql, new Object[]{maHocVien}, (rs, rowNum) -> mapToDonDatLichDTO(rs));
    }

    /**
     * Lấy danh sách đơn đã hủy của học viên
     */
    public List<DonDatLichDTO> getDonDaHuy(Integer maHocVien) {
        String sql = """
            SELECT 
                d.maDatLich,
                d.maHocVien,
                d.maLichTrinh,
                d.soLuongNguoi,
                d.ngayThamGia,
                d.trangThai,
                d.thoiGianHuy,
                
                k.maKhoaHoc,
                k.tenKhoaHoc,
                k.hinhAnh,
                k.moTa,
                
                lt.diaDiem,
                CONVERT(VARCHAR(5), lt.gioBatDau, 108) + ' - ' + CONVERT(VARCHAR(5), lt.gioKetThuc, 108) AS thoiGian,
                
                tt.soTien AS tongTien,
                tt.trangThai AS daThanhToan,
                tt.ngayThanhToan,
                
                0 AS daDanhGia
                
            FROM DatLich d
            JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh
            JOIN KhoaHoc k ON lt.maKhoaHoc = k.maKhoaHoc
            LEFT JOIN ThanhToan tt ON d.maDatLich = tt.maDatLich
            WHERE d.maHocVien = ?
              AND d.trangThai = N'Đã huỷ'
            ORDER BY d.thoiGianHuy DESC
            """;

        return jdbcTemplate.query(sql, new Object[]{maHocVien}, (rs, rowNum) -> mapToDonDatLichDTO(rs));
    }

    /**
     * Xóa đơn chưa thanh toán (xóa vĩnh viễn)
     */
    public boolean xoaDonChuaThanhToan(Integer maDatLich) {
        try {
            // Kiểm tra đơn có tồn tại và chưa thanh toán không
            String checkSql = """
                SELECT d.maDatLich, ISNULL(tt.trangThai, 0) AS daThanhToan
                FROM DatLich d
                LEFT JOIN ThanhToan tt ON d.maDatLich = tt.maDatLich
                WHERE d.maDatLich = ?
                """;
            
            var result = jdbcTemplate.queryForMap(checkSql, maDatLich);
            Object daThanhToanObj = result.get("daThanhToan");
            
            // Xử lý cả Boolean và Integer (bit trong SQL Server)
            boolean daThanhToan = false;
            if (daThanhToanObj instanceof Boolean) {
                daThanhToan = (Boolean) daThanhToanObj;
            } else if (daThanhToanObj instanceof Number) {
                daThanhToan = ((Number) daThanhToanObj).intValue() == 1;
            }
            
            if (daThanhToan) {
                // Đã thanh toán, không được xóa
                return false;
            }
            
            // Xóa ThanhToan trước (nếu có)
            jdbcTemplate.update("DELETE FROM ThanhToan WHERE maDatLich = ?", maDatLich);
            
            // Xóa DatLich
            int deleted = jdbcTemplate.update("DELETE FROM DatLich WHERE maDatLich = ?", maDatLich);
            return deleted > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Hủy đơn đã thanh toán (chuyển sang "Đã huỷ")
     * Lưu ý: Dùng "huỷ" với dấu hỏi để khớp với CHECK constraint trong DB
     */
    public boolean huyDonDaThanhToan(Integer maDatLich) {
        try {
            // Kiểm tra đơn có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM DatLich WHERE maDatLich = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, maDatLich);
            
            if (count == null || count == 0) {
                return false;
            }
            
            // Cập nhật trạng thái sang "Đã huỷ" và ghi nhận thời gian hủy
            String sql = "UPDATE DatLich SET trangThai = N'Đã huỷ', thoiGianHuy = GETDATE() WHERE maDatLich = ?";
            int updated = jdbcTemplate.update(sql, maDatLich);
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private DonDatLichDTO mapToDonDatLichDTO(ResultSet rs) throws java.sql.SQLException {
        DonDatLichDTO dto = new DonDatLichDTO();
        
        dto.setMaDatLich(rs.getInt("maDatLich"));
        dto.setMaHocVien(rs.getInt("maHocVien"));
        dto.setMaLichTrinh(rs.getInt("maLichTrinh"));
        dto.setSoLuongNguoi(rs.getInt("soLuongNguoi"));
        dto.setNgayThamGia(rs.getString("ngayThamGia"));
        dto.setTrangThai(rs.getString("trangThai"));
        
        dto.setMaKhoaHoc(rs.getInt("maKhoaHoc"));
        dto.setTenKhoaHoc(rs.getString("tenKhoaHoc"));
        dto.setHinhAnh(rs.getString("hinhAnh"));
        dto.setMoTa(rs.getString("moTa"));
        
        dto.setDiaDiem(rs.getString("diaDiem"));
        dto.setThoiGian(rs.getString("thoiGian"));
        
        BigDecimal tongTien = rs.getBigDecimal("tongTien");
        dto.setTongTien(tongTien != null ? tongTien : BigDecimal.ZERO);
        
        dto.setDaThanhToan(rs.getBoolean("daThanhToan"));
        dto.setNgayThanhToan(rs.getString("ngayThanhToan"));
        
        dto.setDaDanhGia(rs.getBoolean("daDanhGia"));
        
        // Thông tin người đặt (nếu có trong query)
        try {
            dto.setTenNguoiDat(rs.getString("tenNguoiDat"));
            dto.setEmailNguoiDat(rs.getString("emailNguoiDat"));
            dto.setSdtNguoiDat(rs.getString("sdtNguoiDat"));
        } catch (Exception e) {
            // Ignore nếu không có column này
        }
        
        // Thời gian hủy (nếu có trong query)
        try {
            java.sql.Timestamp thoiGianHuy = rs.getTimestamp("thoiGianHuy");
            if (thoiGianHuy != null) {
                // Format: "HH:mm - dd/MM/yyyy"
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm - dd/MM/yyyy");
                dto.setThoiGianHuy(sdf.format(thoiGianHuy));
            }
        } catch (Exception e) {
            // Ignore nếu không có column này
        }
        
        return dto;
    }
}
