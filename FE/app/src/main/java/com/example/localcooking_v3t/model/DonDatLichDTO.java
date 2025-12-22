package com.example.localcooking_v3t.model;

import java.math.BigDecimal;

/**
 * DTO cho lịch sử đặt lịch - map từ API
 */
public class DonDatLichDTO {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maKhoaHoc;
    private Integer maLichTrinh;
    
    // Thông tin khóa học
    private String tenKhoaHoc;
    private String hinhAnh;
    private String moTa;
    
    // Thông tin đặt lịch
    private Integer soLuongNguoi;
    private String ngayThamGia;
    private String thoiGian; // "14:00 - 17:00"
    private String diaDiem;
    private BigDecimal tongTien;
    private String trangThai; // "Đặt trước", "Đã hoàn thành", "Đã huỷ"
    
    // Thông tin thanh toán
    private Boolean daThanhToan;
    private String ngayThanhToan;
    
    // Thông tin người đặt
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    
    // Thông tin hủy (nếu có)
    private String thoiGianHuy;
    private String lyDoHuy;
    
    // Đánh giá (nếu có)
    private Boolean daDanhGia;
    
    // Constructors
    public DonDatLichDTO() {}
    
    // Getters and Setters
    public Integer getMaDatLich() { return maDatLich; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }
    
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public String getThoiGian() { return thoiGian; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }
    
    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Boolean getDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(Boolean daThanhToan) { this.daThanhToan = daThanhToan; }
    
    public String getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(String ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
    
    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }
    
    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }
    
    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }
    
    public String getThoiGianHuy() { return thoiGianHuy; }
    public void setThoiGianHuy(String thoiGianHuy) { this.thoiGianHuy = thoiGianHuy; }
    
    public String getLyDoHuy() { return lyDoHuy; }
    public void setLyDoHuy(String lyDoHuy) { this.lyDoHuy = lyDoHuy; }
    
    public Boolean getDaDanhGia() { return daDanhGia; }
    public void setDaDanhGia(Boolean daDanhGia) { this.daDanhGia = daDanhGia; }
}
