package com.example.localcooking_v3t.model;

public class DatLichResponse {
    private boolean success;
    private String message;
    private DatLich data;
    
    // Constructor
    public DatLichResponse() {}
    
    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public DatLich getData() { return data; }
    public void setData(DatLich data) { this.data = data; }
}
