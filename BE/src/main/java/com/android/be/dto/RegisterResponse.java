package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String message;
    private Integer maNguoiDung;
    private String tenDangNhap;
    private String hoTen;
    private String email;
    private String vaiTro;
}
