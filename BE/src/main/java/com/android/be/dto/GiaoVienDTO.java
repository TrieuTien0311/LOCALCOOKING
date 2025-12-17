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
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String chuyenMon;
    private String kinhNghiem;
    private String moTa;
    private String hinhAnh;
}
