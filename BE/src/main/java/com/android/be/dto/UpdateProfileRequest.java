package com.android.be.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private Integer maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String diaChi;
}
