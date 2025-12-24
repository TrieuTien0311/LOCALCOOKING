package com.example.localcooking_v3t;

public class Voucher {
    private int hinhAnh;        // Ảnh
    private String tieuDe;      // Tiêu đề
    private String moTa;        // Mô tả
    private String HSD;         // Hạn sử dụng
    private boolean duocChon;   // Trạng thái được chọn (true/false)
    private String maCode;      // Mã code voucher (để map với UuDaiDTO)
    private Integer maUuDai;    // Mã ưu đãi (để map với UuDaiDTO)

    // Constructor cơ bản
    public Voucher(int hinhAnh, String tieuDe, String moTa, String HSD) {
        this.hinhAnh = hinhAnh;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.HSD = HSD;
        this.duocChon = false;
    }
    
    // Constructor với maCode và maUuDai
    public Voucher(int hinhAnh, String tieuDe, String moTa, String HSD, String maCode, Integer maUuDai) {
        this.hinhAnh = hinhAnh;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.HSD = HSD;
        this.maCode = maCode;
        this.maUuDai = maUuDai;
        this.duocChon = false;
    }

    // Getters
    public int getHinhAnh() { return hinhAnh; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public String getHSD() { return HSD; }
    public boolean isDuocChon() { return duocChon; }
    public String getMaCode() { return maCode; }
    public Integer getMaUuDai() { return maUuDai; }

    // Setters
    public void setHinhAnh(int hinhAnh) { this.hinhAnh = hinhAnh; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setHSD(String HSD) { this.HSD = HSD; }
    public void setDuocChon(boolean duocChon) { this.duocChon = duocChon; }
    public void setMaCode(String maCode) { this.maCode = maCode; }
    public void setMaUuDai(Integer maUuDai) { this.maUuDai = maUuDai; }
}
