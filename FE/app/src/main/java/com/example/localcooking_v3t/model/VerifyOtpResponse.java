package com.example.localcooking_v3t.model;

public class VerifyOtpResponse {
    private String resetToken;
    private String message;

    public VerifyOtpResponse() {
    }

    public VerifyOtpResponse(String resetToken, String message) {
        this.resetToken = resetToken;
        this.message = message;
    }

    // Getters and Setters
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
