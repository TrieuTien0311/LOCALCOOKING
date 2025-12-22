package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class TaoDanhGiaResponse {
    @SerializedName("success")
    private Boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private DanhGiaDTO data;

    // Getters
    public Boolean getSuccess() { return success; }
    public String getMessage() { return message; }
    public DanhGiaDTO getData() { return data; }

    // Setters
    public void setSuccess(Boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(DanhGiaDTO data) { this.data = data; }
    
    public boolean isSuccess() {
        return success != null && success;
    }
}
