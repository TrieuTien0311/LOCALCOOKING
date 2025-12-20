package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;

public class UuDaiDTO {
    @SerializedName("maUuDai")
    private Integer maUuDai;

    @SerializedName("maCode")
    private String maCode;

    @SerializedName("hinhAnh")
    private String hinhAnh;

    @SerializedName("tieuDe")
    private String tieuDe;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("HSD")
    private String hsd;

    @SerializedName("duocChon")
    private Boolean duocChon;

    @SerializedName("loaiGiam")
    private String loaiGiam;

    @SerializedName("giaTriGiam")
    private Double giaTriGiam;

    @SerializedName("giamToiDa")
    private Double giamToiDa;

    @SerializedName("soLuong")
    private Integer soLuong;

    @SerializedName("soLuongDaSuDung")
    private Integer soLuongDaSuDung;

    @SerializedName("trangThai")
    private String trangThai;

    // Getters and Setters
    public Integer getMaUuDai() { return maUuDai; }
    public void setMaUuDai(Integer maUuDai) { this.maUuDai = maUuDai; }

    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHsd() { return hsd; }
    public void setHsd(String hsd) { this.hsd = hsd; }

    public Boolean getDuocChon() { return duocChon; }
    public void setDuocChon(Boolean duocChon) { this.duocChon = duocChon; }

    public String getLoaiGiam() { return loaiGiam; }
    public void setLoaiGiam(String loaiGiam) { this.loaiGiam = loaiGiam; }

    public Double getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(Double giaTriGiam) { this.giaTriGiam = giaTriGiam; }

    public Double getGiamToiDa() { return giamToiDa; }
    public void setGiamToiDa(Double giamToiDa) { this.giamToiDa = giamToiDa; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Integer getSoLuongDaSuDung() { return soLuongDaSuDung; }
    public void setSoLuongDaSuDung(Integer soLuongDaSuDung) { this.soLuongDaSuDung = soLuongDaSuDung; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
