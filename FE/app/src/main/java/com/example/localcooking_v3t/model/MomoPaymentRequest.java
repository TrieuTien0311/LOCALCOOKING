package com.example.localcooking_v3t.model;

import java.math.BigDecimal;

public class MomoPaymentRequest {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maLichTrinh;
    private BigDecimal soTien;
    private String tenKhoaHoc;
    private String ngayThamGia;
    private Integer soLuongNguoi;
    
    // Thông tin liên hệ
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    
    // Ưu đãi (nếu có)
    private Integer maUuDai;
    private BigDecimal soTienGiam;
    
    // Getters and Setters
    public Integer getMaDatLich() { return maDatLich; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public BigDecimal getSoTien() { return soTien; }
    public void setSoTien(BigDecimal soTien) { this.soTien = soTien; }
    
    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    
    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }
    
    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }
    
    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }
    
    public Integer getMaUuDai() { return maUuDai; }
    public void setMaUuDai(Integer maUuDai) { this.maUuDai = maUuDai; }
    
    public BigDecimal getSoTienGiam() { return soTienGiam; }
    public void setSoTienGiam(BigDecimal soTienGiam) { this.soTienGiam = soTienGiam; }
}
