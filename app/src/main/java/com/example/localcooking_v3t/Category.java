package com.example.localcooking_v3t;

import java.util.List;

public class Category {
    private String tenDanhMuc; // Món khai vị, Món chính, Món tráng miệng
    private String thoiGian;   // 14:00 - 15:00
    private int iconDanhMuc;   // Icon cho danh mục
    private List<Food> danhSachMon;

    public Category(String tenDanhMuc, String thoiGian, int iconDanhMuc, List<Food> danhSachMon) {
        this.tenDanhMuc = tenDanhMuc;
        this.thoiGian = thoiGian;
        this.iconDanhMuc = iconDanhMuc;
        this.danhSachMon = danhSachMon;
    }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public String getThoiGian() { return thoiGian; }
    public int getIconDanhMuc() { return iconDanhMuc; }
    public List<Food> getDanhSachMon() { return danhSachMon; }
}