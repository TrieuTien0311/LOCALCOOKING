package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class HinhAnhDanhGiaDTO {
    @SerializedName("maHinhAnh")
    private Integer maHinhAnh;
    
    @SerializedName("maDanhGia")
    private Integer maDanhGia;
    
    @SerializedName("duongDan")
    private String duongDan;
    
    @SerializedName("loaiFile")
    private String loaiFile; // 'image' hoặc 'video'
    
    @SerializedName("thuTu")
    private Integer thuTu;

    // Getters
    public Integer getMaHinhAnh() { return maHinhAnh; }
    public Integer getMaDanhGia() { return maDanhGia; }
    public String getDuongDan() { return duongDan; }
    public String getLoaiFile() { return loaiFile; }
    public Integer getThuTu() { return thuTu; }

    // Setters
    public void setMaHinhAnh(Integer maHinhAnh) { this.maHinhAnh = maHinhAnh; }
    public void setMaDanhGia(Integer maDanhGia) { this.maDanhGia = maDanhGia; }
    public void setDuongDan(String duongDan) { this.duongDan = duongDan; }
    public void setLoaiFile(String loaiFile) { this.loaiFile = loaiFile; }
    public void setThuTu(Integer thuTu) { this.thuTu = thuTu; }
    
    public boolean isVideo() {
        return "video".equalsIgnoreCase(loaiFile);
    }
}
