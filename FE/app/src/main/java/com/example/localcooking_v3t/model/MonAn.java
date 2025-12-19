package com.example.localcooking_v3t.model;

import java.io.Serializable;

/**
 * Model MonAn - Món ăn trong khóa học
 */
public class MonAn implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer maMonAn;
    private Integer maKhoaHoc;
    private Integer maDanhMuc;
    private String tenMon;
    private String gioiThieu;
    private String nguyenLieu;
    private String hinhAnh;
    
    public MonAn() {
    }
    
    public MonAn(Integer maMonAn, Integer maKhoaHoc, Integer maDanhMuc, String tenMon, 
                 String gioiThieu, String nguyenLieu, String hinhAnh) {
        this.maMonAn = maMonAn;
        this.maKhoaHoc = maKhoaHoc;
        this.maDanhMuc = maDanhMuc;
        this.tenMon = tenMon;
        this.gioiThieu = gioiThieu;
        this.nguyenLieu = nguyenLieu;
        this.hinhAnh = hinhAnh;
    }
    
    // Getters and Setters
    public Integer getMaMonAn() {
        return maMonAn;
    }
    
    public void setMaMonAn(Integer maMonAn) {
        this.maMonAn = maMonAn;
    }
    
    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }
    
    public Integer getMaDanhMuc() {
        return maDanhMuc;
    }
    
    public void setMaDanhMuc(Integer maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }
    
    public String getTenMon() {
        return tenMon;
    }
    
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
    
    public String getGioiThieu() {
        return gioiThieu;
    }
    
    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
    
    public String getNguyenLieu() {
        return nguyenLieu;
    }
    
    public void setNguyenLieu(String nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
