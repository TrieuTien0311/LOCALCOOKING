package com.example.localcooking_v3t.model;

public class VerifyOtpRequest {
    private String email;
    private String otp;

    public VerifyOtpRequest() {
    }

    public VerifyOtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
