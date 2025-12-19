package com.example.localcooking_v3t.model;

public class MonAn {
    private Integer maMonAn;
    private Integer maLopHoc;
    private Integer maDanhMuc;
    private String tenMon;
    private String gioiThieu;
    private String nguyenLieu;

    public MonAn() {
    }

    public MonAn(Integer maMonAn, Integer maLopHoc, Integer maDanhMuc, String tenMon, String gioiThieu, String nguyenLieu) {
        this.maMonAn = maMonAn;
        this.maLopHoc = maLopHoc;
        this.maDanhMuc = maDanhMuc;
        this.tenMon = tenMon;
        this.gioiThieu = gioiThieu;
        this.nguyenLieu = nguyenLieu;
    }

    public Integer getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(Integer maMonAn) {
        this.maMonAn = maMonAn;
    }

    public Integer getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(Integer maLopHoc) {
        this.maLopHoc = maLopHoc;
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
}
