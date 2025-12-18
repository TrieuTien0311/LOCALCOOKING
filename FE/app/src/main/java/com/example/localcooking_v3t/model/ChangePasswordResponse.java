package com.example.localcooking_v3t.model;

public class ChangePasswordResponse {
    private boolean success;
    private String message;
    private String email; // Optional field

    // Constructor mặc định (cần cho Gson)
    public ChangePasswordResponse() {
    }

    public ChangePasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ChangePasswordResponse(boolean success, String message, String email) {
        this.success = success;
        this.message = message;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
