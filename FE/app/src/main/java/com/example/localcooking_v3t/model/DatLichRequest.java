package com.example.localcooking_v3t.model;

import java.math.BigDecimal;

public class DatLichRequest {
    private Integer maHocVien;
    private Integer maLichTrinh;
    private String ngayThamGia; // Format: "YYYY-MM-DD"
    private Integer soLuongNguoi;
    private BigDecimal tongTien;
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    private String ghiChu;
    
    // Constructor
    public DatLichRequest() {}
    
    // Getters & Setters
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }
    
    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }
    
    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
