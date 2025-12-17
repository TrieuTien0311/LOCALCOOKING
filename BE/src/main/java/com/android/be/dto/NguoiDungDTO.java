package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDTO {
    private Integer maNguoiDung;
    private String tenDangNhap;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String diaChi;
    private String vaiTro;
    private String trangThai;
    private String ngayTao;
}
