package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordWithOtpRequest {
    private String email;
    private String matKhauHienTai;
    private String matKhauMoi;
    private String xacNhanMatKhauMoi;
    private String otp;
}
