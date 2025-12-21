package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class ApDungUuDaiResponse {
    @SerializedName("thanhCong")
    private boolean thanhCong;

    @SerializedName("message")
    private String message;

    @SerializedName("maUuDai")
    private Integer maUuDai;

    @SerializedName("maCode")
    private String maCode;

    @SerializedName("tenUuDai")
    private String tenUuDai;

    @SerializedName("tongTienGoc")
    private Double tongTienGoc;

    @SerializedName("soTienGiam")
    private Double soTienGiam;

    @SerializedName("tongTienSauGiam")
    private Double tongTienSauGiam;

    @SerializedName("loaiGiam")
    private String loaiGiam;

    @SerializedName("phanTramGiam")
    private Double phanTramGiam;

    // Getters
    public boolean isThanhCong() { return thanhCong; }
    public String getMessage() { return message; }
    public Integer getMaUuDai() { return maUuDai; }
    public String getMaCode() { return maCode; }
    public String getTenUuDai() { return tenUuDai; }
    public Double getTongTienGoc() { return tongTienGoc; }
    public Double getSoTienGiam() { return soTienGiam; }
    public Double getTongTienSauGiam() { return tongTienSauGiam; }
    public String getLoaiGiam() { return loaiGiam; }
    public Double getPhanTramGiam() { return phanTramGiam; }
}
