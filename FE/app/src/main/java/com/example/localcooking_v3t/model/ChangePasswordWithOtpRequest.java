package com.example.localcooking_v3t.model;

public class ChangePasswordWithOtpRequest {
    private String email;
    private String matKhauHienTai;
    private String matKhauMoi;
    private String xacNhanMatKhauMoi;
    private String otp;

    public ChangePasswordWithOtpRequest(String email, String matKhauHienTai, String matKhauMoi, String xacNhanMatKhauMoi, String otp) {
        this.email = email;
        this.matKhauHienTai = matKhauHienTai;
        this.matKhauMoi = matKhauMoi;
        this.xacNhanMatKhauMoi = xacNhanMatKhauMoi;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhauHienTai() {
        return matKhauHienTai;
    }

    public void setMatKhauHienTai(String matKhauHienTai) {
        this.matKhauHienTai = matKhauHienTai;
    }

    public String getMatKhauMoi() {
        return matKhauMoi;
    }

    public void setMatKhauMoi(String matKhauMoi) {
        this.matKhauMoi = matKhauMoi;
    }

    public String getXacNhanMatKhauMoi() {
        return xacNhanMatKhauMoi;
    }

    public void setXacNhanMatKhauMoi(String xacNhanMatKhauMoi) {
        this.xacNhanMatKhauMoi = xacNhanMatKhauMoi;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
