package com.example.localcooking_v3t.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model DanhMucMonAn - Danh mục món ăn (Khai vị, Món chính, Tráng miệng)
 */
public class DanhMucMonAn implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer maDanhMuc;
    private String tenDanhMuc;
    private String iconDanhMuc;
    private Integer thuTu;
    private List<MonAn> danhSachMon; // Danh sách món ăn trong danh mục này
    
    public DanhMucMonAn() {
    }
    
    public DanhMucMonAn(Integer maDanhMuc, String tenDanhMuc, String iconDanhMuc, Integer thuTu) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.iconDanhMuc = iconDanhMuc;
        this.thuTu = thuTu;
    }
    
    // Getters and Setters
    public Integer getMaDanhMuc() {
        return maDanhMuc;
    }
    
    public void setMaDanhMuc(Integer maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }
    
    public String getTenDanhMuc() {
        return tenDanhMuc;
    }
    
    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }
    
    public String getIconDanhMuc() {
        return iconDanhMuc;
    }
    
    public void setIconDanhMuc(String iconDanhMuc) {
        this.iconDanhMuc = iconDanhMuc;
    }
    
    public Integer getThuTu() {
        return thuTu;
    }
    
    public void setThuTu(Integer thuTu) {
        this.thuTu = thuTu;
    }
    
    public List<MonAn> getDanhSachMon() {
        return danhSachMon;
    }
    
    public void setDanhSachMon(List<MonAn> danhSachMon) {
        this.danhSachMon = danhSachMon;
    }
}
