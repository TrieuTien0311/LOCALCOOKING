package com.example.localcooking_v3t.model;

public class CheckSeatsResponse {
    private boolean success;
    private int soChoConLai;
    private String message;
    
    // Constructor
    public CheckSeatsResponse() {}
    
    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public int getSoChoConLai() { return soChoConLai; }
    public void setSoChoConLai(int soChoConLai) { this.soChoConLai = soChoConLai; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
