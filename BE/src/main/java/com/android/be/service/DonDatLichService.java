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
                "CASE WHEN EXISTS(SELECT 1 FROM DanhGia dg WHERE dg.maDatLich = d.maDatLich) THEN 1 ELSE 0 END AS daDanhGia " +
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
              AND d.trangThai = N'Đã hủy'
            ORDER BY d.ngayDat DESC
            """;

        return jdbcTemplate.query(sql, new Object[]{maHocVien}, (rs, rowNum) -> mapToDonDatLichDTO(rs));
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
        
        return dto;
    }
}
