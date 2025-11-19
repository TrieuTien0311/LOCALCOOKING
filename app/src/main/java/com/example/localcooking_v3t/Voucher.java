package com.example.localcooking_v3t;

public class Voucher {
    private int hinhAnh;        // Ảnh
    private String tieuDe;      // Tiêu đề
    private String moTa;        // Mô tả
    private String HSD;   // Hạn sử dụng (String hoặc Date tùy bạn)
    private boolean duocChon;   // Trạng thái được chọn (true/false)

    // Constructor
    public Voucher(int hinhAnh, String tieuDe, String moTa, String HSD) {
        this.hinhAnh = hinhAnh;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.HSD = HSD;
        this.duocChon = false; // Mặc định chưa chọn
    }

    // Getters
    public int getHinhAnh() {
        return hinhAnh;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getHSD() {
        return HSD;
    }

    public boolean isDuocChon() {
        return duocChon;
    }

    // Setters
    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setHSD(String HSD) {
        this.HSD = HSD;
    }

    public void setDuocChon(boolean duocChon) {
        this.duocChon = duocChon;
    }

}
