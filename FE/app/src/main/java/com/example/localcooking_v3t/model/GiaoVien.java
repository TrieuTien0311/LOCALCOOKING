package com.example.localcooking_v3t.model;

import java.io.Serializable;

/**
 * Model GiaoVien - Thông tin giáo viên
 */
public class GiaoVien implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer maGiaoVien;
    private Integer maNguoiDung;
    private String chuyenMon;
    private String kinhNghiem;
    private String lichSuKinhNghiem;
    private String moTa;
    private String hinhAnh;
    
    // Thông tin từ NguoiDung
    private String hoTen;
    private String email;
    private String soDienThoai;
    
    public GiaoVien() {
    }
    
    // Getters and Setters
    public Integer getMaGiaoVien() {
        return maGiaoVien;
    }
    
    public void setMaGiaoVien(Integer maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }
    
    public Integer getMaNguoiDung() {
        return maNguoiDung;
    }
    
    public void setMaNguoiDung(Integer maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }
    
    public String getChuyenMon() {
        return chuyenMon;
    }
    
    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
    
    public String getKinhNghiem() {
        return kinhNghiem;
    }
    
    public void setKinhNghiem(String kinhNghiem) {
        this.kinhNghiem = kinhNghiem;
    }
    
    public String getLichSuKinhNghiem() {
        return lichSuKinhNghiem;
    }
    
    public void setLichSuKinhNghiem(String lichSuKinhNghiem) {
        this.lichSuKinhNghiem = lichSuKinhNghiem;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
    /**
     * Lấy resource ID của hình ảnh giáo viên
     * @param context Android context
     * @return Resource ID của hình ảnh, hoặc hình mặc định nếu không tìm thấy
     */
    public int getHinhAnhResId(android.content.Context context) {
        if (hinhAnh == null || hinhAnh.isEmpty()) {
            // Trả về hình mặc định
            return context.getResources().getIdentifier("giaovien1", "drawable", context.getPackageName());
        }
        
        // Loại bỏ extension
        String name = hinhAnh.replace(".png", "").replace(".jpg", "");
        
        // Lấy resource ID
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resId != 0 ? resId : context.getResources().getIdentifier("giaovien1", "drawable", context.getPackageName());
    }
}
