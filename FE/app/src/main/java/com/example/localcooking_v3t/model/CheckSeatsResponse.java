package com.example.localcooking_v3t.model;

public class CheckSeatsResponse {
    private boolean success;
    private String message;
    
    // Dữ liệu từ stored procedure sp_KiemTraChoTrong
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private Integer tongCho;
    private Integer daDat;
    private Integer conTrong;
    private String trangThai; // "Hết Chỗ", "Sắp Hết", "Còn Nhiều"
    
    // Constructor
    public CheckSeatsResponse() {}
    
    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }
    
    public Integer getTongCho() { return tongCho; }
    public void setTongCho(Integer tongCho) { this.tongCho = tongCho; }
    
    public Integer getDaDat() { return daDat; }
    public void setDaDat(Integer daDat) { this.daDat = daDat; }
    
    public Integer getConTrong() { return conTrong; }
    public void setConTrong(Integer conTrong) { this.conTrong = conTrong; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    // Backward compatibility
    public int getSoChoConLai() { 
        return conTrong != null ? conTrong : 0; 
    }
    public void setSoChoConLai(int soChoConLai) { 
        this.conTrong = soChoConLai; 
    }
}
