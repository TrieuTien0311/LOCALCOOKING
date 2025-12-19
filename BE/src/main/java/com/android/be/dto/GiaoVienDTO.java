package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoVienDTO {
    private Integer maGiaoVien;
    private Integer maNguoiDung;
    private String chuyenMon;
    private String kinhNghiem;
    private String lichSuKinhNghiem;
    private String moTa;
    private String hinhAnh;
    
    // Thông tin từ NguoiDung
    private String hoTen;
    private String email;
    private String soDienThoai;
}
