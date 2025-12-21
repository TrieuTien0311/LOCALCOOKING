package com.example.localcooking_v3t.model;

import java.io.Serializable;

/**
 * DTO cho lịch sử đặt lịch từ API
 */
public class DonDatLichDTO implements Serializable {
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
    private String thoiGian;
    private String diaDiem;
    private Double tongTien;
    private String trangThai;
    
    // Thông tin thanh toán
    private Boolean daThanhToan;
    private String ngayThanhToan;
    
    // Thông tin hủy
    private String thoiGianHuy;
    private String lyDoHuy;
    
    // Đánh giá
    private Boolean daDanhGia;
    
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
    
    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Boolean getDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(Boolean daThanhToan) { this.daThanhToan = daThanhToan; }
    
    public String getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(String ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
    
    public String getThoiGianHuy() { return thoiGianHuy; }
    public void setThoiGianHuy(String thoiGianHuy) { this.thoiGianHuy = thoiGianHuy; }
    
    public String getLyDoHuy() { return lyDoHuy; }
    public void setLyDoHuy(String lyDoHuy) { this.lyDoHuy = lyDoHuy; }
    
    public Boolean getDaDanhGia() { return daDanhGia; }
    public void setDaDanhGia(Boolean daDanhGia) { this.daDanhGia = daDanhGia; }
    
    /**
     * Lấy resource ID của hình ảnh
     */
    public int getHinhAnhResId(android.content.Context context) {
        if (hinhAnh == null || hinhAnh.isEmpty()) {
            return context.getResources().getIdentifier("hue", "drawable", context.getPackageName());
        }
        
        String name = hinhAnh.toLowerCase()
                .replace(".png", "")
                .replace(".jpg", "")
                .replace(".jpeg", "");
        
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resId != 0 ? resId : context.getResources().getIdentifier("hue", "drawable", context.getPackageName());
    }
    
    /**
     * Format ngày từ "2025-12-25" sang "25/12/2025"
     */
    public String getNgayThamGiaFormatted() {
        if (ngayThamGia == null || ngayThamGia.isEmpty()) return "";
        try {
            String[] parts = ngayThamGia.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ngayThamGia;
    }
    
    /**
     * Format lịch: "14:00 - 17:00, 25/12/2025"
     */
    public String getLichFormatted() {
        StringBuilder sb = new StringBuilder();
        if (thoiGian != null && !thoiGian.isEmpty()) {
            sb.append(thoiGian);
        }
        if (ngayThamGia != null && !ngayThamGia.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(getNgayThamGiaFormatted());
        }
        return sb.toString();
    }
    
    /**
     * Format tiền: "715.000₫"
     */
    public String getTongTienFormatted() {
        if (tongTien == null) return "0₫";
        return String.format("%,.0f₫", tongTien).replace(",", ".");
    }
    
    /**
     * Format số lượng người: "1 người"
     */
    public String getSoLuongNguoiFormatted() {
        return (soLuongNguoi != null ? soLuongNguoi : 1) + " người";
    }
}
