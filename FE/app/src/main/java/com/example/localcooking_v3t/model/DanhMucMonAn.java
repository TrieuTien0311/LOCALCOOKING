package com.example.localcooking_v3t.model;

import java.util.List;

public class DanhMucMonAn {
    private Integer maDanhMuc;
    private String tenDanhMuc;
    private String iconDanhMuc;
    private Integer thuTu;
    private List<MonAn> danhSachMon;

    public DanhMucMonAn() {
    }

    public DanhMucMonAn(Integer maDanhMuc, String tenDanhMuc, String iconDanhMuc, Integer thuTu, List<MonAn> danhSachMon) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.iconDanhMuc = iconDanhMuc;
        this.thuTu = thuTu;
        this.danhSachMon = danhSachMon;
    }

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
