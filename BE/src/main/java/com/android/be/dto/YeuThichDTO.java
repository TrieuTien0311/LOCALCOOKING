package com.android.be.dto;

import java.time.LocalDateTime;

public class YeuThichDTO {
    private Integer maYeuThich;
    private Integer maHocVien;
    private Integer maKhoaHoc;
    private LocalDateTime ngayThem;
    
    // Thông tin khóa học (để hiển thị trong danh sách yêu thích)
    private String tenKhoaHoc;
    private String moTa;
    private Double giaTien;
    private String hinhAnh;
    private Float saoTrungBinh;
    private Integer soLuongDanhGia;
    private Boolean coUuDai;
    
    // Constructors
    public YeuThichDTO() {
    }
    
    // Getters and Setters
    public Integer getMaYeuThich() {
        return maYeuThich;
    }
    
    public void setMaYeuThich(Integer maYeuThich) {
        this.maYeuThich = maYeuThich;
    }
    
    public Integer getMaHocVien() {
        return maHocVien;
    }
    
    public void setMaHocVien(Integer maHocVien) {
        this.maHocVien = maHocVien;
    }
    
    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }
    
    public LocalDateTime getNgayThem() {
        return ngayThem;
    }
    
    public void setNgayThem(LocalDateTime ngayThem) {
        this.ngayThem = ngayThem;
    }
    
    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }
    
    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public Double getGiaTien() {
        return giaTien;
    }
    
    public void setGiaTien(Double giaTien) {
        this.giaTien = giaTien;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    
    public Float getSaoTrungBinh() {
        return saoTrungBinh;
    }
    
    public void setSaoTrungBinh(Float saoTrungBinh) {
        this.saoTrungBinh = saoTrungBinh;
    }
    
    public Integer getSoLuongDanhGia() {
        return soLuongDanhGia;
    }
    
    public void setSoLuongDanhGia(Integer soLuongDanhGia) {
        this.soLuongDanhGia = soLuongDanhGia;
    }
    
    public Boolean getCoUuDai() {
        return coUuDai;
    }
    
    public void setCoUuDai(Boolean coUuDai) {
        this.coUuDai = coUuDai;
    }
}
