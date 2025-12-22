package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TaoDanhGiaRequest {
    @SerializedName("maDatLich")
    private Integer maDatLich;
    
    @SerializedName("diemDanhGia")
    private Integer diemDanhGia; // 1-5
    
    @SerializedName("binhLuan")
    private String binhLuan;
    
    @SerializedName("hinhAnhUrls")
    private List<String> hinhAnhUrls;

    public TaoDanhGiaRequest() {}
    
    public TaoDanhGiaRequest(Integer maDatLich, Integer diemDanhGia, String binhLuan, List<String> hinhAnhUrls) {
        this.maDatLich = maDatLich;
        this.diemDanhGia = diemDanhGia;
        this.binhLuan = binhLuan;
        this.hinhAnhUrls = hinhAnhUrls;
    }

    // Getters
    public Integer getMaDatLich() { return maDatLich; }
    public Integer getDiemDanhGia() { return diemDanhGia; }
    public String getBinhLuan() { return binhLuan; }
    public List<String> getHinhAnhUrls() { return hinhAnhUrls; }

    // Setters
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    public void setDiemDanhGia(Integer diemDanhGia) { this.diemDanhGia = diemDanhGia; }
    public void setBinhLuan(String binhLuan) { this.binhLuan = binhLuan; }
    public void setHinhAnhUrls(List<String> hinhAnhUrls) { this.hinhAnhUrls = hinhAnhUrls; }
}
