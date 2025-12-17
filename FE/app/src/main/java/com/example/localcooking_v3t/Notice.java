package com.example.localcooking_v3t;

public class Notice {
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private int anhTB; // Resource ID của ảnh
    private boolean trangThai; // true = đã đọc, false = chưa đọc

    // Constructor
    public Notice(String tieuDeTB, String noiDungTB, String thoiGianTB, int anhTB, boolean trangThai) {
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = anhTB;
        this.trangThai = trangThai;
    }

    // Getters
    public String getTieuDeTB() {
        return tieuDeTB;
    }

    public String getNoiDungTB() {
        return noiDungTB;
    }

    public String getThoiGianTB() {
        return thoiGianTB;
    }

    public int getAnhTB() {
        return anhTB;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    // Setters
    public void setTieuDeTB(String tieuDeTB) {
        this.tieuDeTB = tieuDeTB;
    }

    public void setNoiDungTB(String noiDungTB) {
        this.noiDungTB = noiDungTB;
    }

    public void setThoiGianTB(String thoiGianTB) {
        this.thoiGianTB = thoiGianTB;
    }

    public void setAnhTB(int anhTB) {
        this.anhTB = anhTB;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}