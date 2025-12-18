package com.example.localcooking_v3t.model;

public class ForgotPasswordResponse {
    private String message;

    public ForgotPasswordResponse() {
    }

    public ForgotPasswordResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
