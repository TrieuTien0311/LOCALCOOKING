package com.example.localcooking_v3t.model;

import java.io.Serializable;

/**
 * DTO cho hình ảnh/video đánh giá
 */
public class HinhAnhDanhGiaDTO implements Serializable {
    private Integer maHinhAnh;
    private Integer maDanhGia;
    private String duongDan;
    private String loaiFile; // "image" hoặc "video"
    private Integer thuTu;
    
    // Getters and Setters
    public Integer getMaHinhAnh() { return maHinhAnh; }
    public void setMaHinhAnh(Integer maHinhAnh) { this.maHinhAnh = maHinhAnh; }
    
    public Integer getMaDanhGia() { return maDanhGia; }
    public void setMaDanhGia(Integer maDanhGia) { this.maDanhGia = maDanhGia; }
    
    public String getDuongDan() { return duongDan; }
    public void setDuongDan(String duongDan) { this.duongDan = duongDan; }
    
    public String getLoaiFile() { return loaiFile; }
    public void setLoaiFile(String loaiFile) { this.loaiFile = loaiFile; }
    
    public Integer getThuTu() { return thuTu; }
    public void setThuTu(Integer thuTu) { this.thuTu = thuTu; }
    
    public boolean isVideo() {
        return "video".equalsIgnoreCase(loaiFile);
    }
    
    public boolean isImage() {
        return "image".equalsIgnoreCase(loaiFile) || loaiFile == null;
    }
}
