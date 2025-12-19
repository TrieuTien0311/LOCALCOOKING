package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileResponse {
    private boolean success;
    private String message;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String diaChi;
}
