package com.example.localcooking_v3t;

public class Food {
    private String tenMon;
    private String gioiThieu;
    private String nguyenLieu;
    private int hinhAnh;

    public Food(String tenMon, String gioiThieu, String nguyenLieu, int hinhAnh) {
        this.tenMon = tenMon;
        this.gioiThieu = gioiThieu;
        this.nguyenLieu = nguyenLieu;
        this.hinhAnh = hinhAnh;
    }

    public String getTenMon() { return tenMon; }
    public String getGioiThieu() { return gioiThieu; }
    public String getNguyenLieu() { return nguyenLieu; }
    public int getHinhAnh() { return hinhAnh; }
}