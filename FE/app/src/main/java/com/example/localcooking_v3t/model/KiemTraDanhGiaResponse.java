package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class KiemTraDanhGiaResponse {
    @SerializedName("maDatLich")
    private Integer maDatLich;
    
    @SerializedName("trangThaiDon")
    private String trangThaiDon;
    
    @SerializedName("daDanhGia")
    private Boolean daDanhGia;
    
    @SerializedName("maDanhGia")
    private Integer maDanhGia;
    
    @SerializedName("trangThaiDanhGia")
    private String trangThaiDanhGia; // "CÓ THỂ ĐÁNH GIÁ", "ĐÃ ĐÁNH GIÁ", "KHÔNG THỂ ĐÁNH GIÁ"
    
    @SerializedName("danhGia")
    private DanhGiaDTO danhGia;

    // Getters
    public Integer getMaDatLich() { return maDatLich; }
    public String getTrangThaiDon() { return trangThaiDon; }
    public Boolean getDaDanhGia() { return daDanhGia; }
    public Integer getMaDanhGia() { return maDanhGia; }
    public String getTrangThaiDanhGia() { return trangThaiDanhGia; }
    public DanhGiaDTO getDanhGia() { return danhGia; }

    // Setters
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    public void setTrangThaiDon(String trangThaiDon) { this.trangThaiDon = trangThaiDon; }
    public void setDaDanhGia(Boolean daDanhGia) { this.daDanhGia = daDanhGia; }
    public void setMaDanhGia(Integer maDanhGia) { this.maDanhGia = maDanhGia; }
    public void setTrangThaiDanhGia(String trangThaiDanhGia) { this.trangThaiDanhGia = trangThaiDanhGia; }
    public void setDanhGia(DanhGiaDTO danhGia) { this.danhGia = danhGia; }
    
    // Helper methods
    public boolean coTheDanhGia() {
        return "CÓ THỂ ĐÁNH GIÁ".equals(trangThaiDanhGia);
    }
    
    public boolean daDanhGiaRoi() {
        return daDanhGia != null && daDanhGia;
    }
}
