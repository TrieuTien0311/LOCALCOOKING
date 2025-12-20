package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class ApDungUuDaiRequest {
    @SerializedName("maHocVien")
    private Integer maHocVien;

    @SerializedName("maCode")
    private String maCode;

    @SerializedName("tongTien")
    private Double tongTien;

    @SerializedName("soLuongNguoi")
    private Integer soLuongNguoi;

    public ApDungUuDaiRequest(Integer maHocVien, String maCode, Double tongTien, Integer soLuongNguoi) {
        this.maHocVien = maHocVien;
        this.maCode = maCode;
        this.tongTien = tongTien;
        this.soLuongNguoi = soLuongNguoi;
    }

    // Getters and Setters
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }

    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
}
