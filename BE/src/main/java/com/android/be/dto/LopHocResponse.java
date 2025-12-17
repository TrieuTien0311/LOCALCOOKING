package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHocResponse {
    private Integer maLopHoc;
    private String tenLopHoc;
    private String moTa;
    private String tenGiaoVien;
    private Integer soLuongToiDa;
    private Integer soLuongHienTai;
    private Integer soLuongConLai;
    private BigDecimal giaTien;
    private String thoiGian;
    private String diaDiem;
    private String trangThai;
    private String ngayDienRa; // Format: YYYY-MM-DD
    private String gioBatDau; // Format: HH:MM
    private String gioKetThuc; // Format: HH:MM
    private String hinhAnh;
    private Boolean coUuDai;
    private Double diemDanhGia;
    private Integer soDanhGia;
}
