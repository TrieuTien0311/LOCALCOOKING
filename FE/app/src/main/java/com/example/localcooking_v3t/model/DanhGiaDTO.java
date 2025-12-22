package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DanhGiaDTO {
    @SerializedName("maDanhGia")
    private Integer maDanhGia;
    
    @SerializedName("maHocVien")
    private Integer maHocVien;
    
    @SerializedName("tenHocVien")
    private String tenHocVien;
    
    @SerializedName("maKhoaHoc")
    private Integer maKhoaHoc;
    
    @SerializedName("tenKhoaHoc")
    private String tenKhoaHoc;
    
    @SerializedName("maDatLich")
    private Integer maDatLich;
    
    @SerializedName("diemDanhGia")
    private Integer diemDanhGia;
    
    @SerializedName("binhLuan")
    private String binhLuan;
    
    @SerializedName("ngayDanhGia")
    private String ngayDanhGia;
    
    @SerializedName("hinhAnhList")
    private List<HinhAnhDanhGiaDTO> hinhAnhList;
    
    @SerializedName("daDanhGia")
    private Boolean daDanhGia;
    
    @SerializedName("trangThaiDanhGia")
    private String trangThaiDanhGia;

    // Getters
    public Integer getMaDanhGia() { return maDanhGia; }
    public Integer getMaHocVien() { return maHocVien; }
    public String getTenHocVien() { return tenHocVien; }
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public Integer getMaDatLich() { return maDatLich; }
    public Integer getDiemDanhGia() { return diemDanhGia; }
    public String getBinhLuan() { return binhLuan; }
    public String getNgayDanhGia() { return ngayDanhGia; }
    public List<HinhAnhDanhGiaDTO> getHinhAnhList() { return hinhAnhList; }
    public Boolean getDaDanhGia() { return daDanhGia; }
    public String getTrangThaiDanhGia() { return trangThaiDanhGia; }

    // Setters
    public void setMaDanhGia(Integer maDanhGia) { this.maDanhGia = maDanhGia; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    public void setTenHocVien(String tenHocVien) { this.tenHocVien = tenHocVien; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    public void setDiemDanhGia(Integer diemDanhGia) { this.diemDanhGia = diemDanhGia; }
    public void setBinhLuan(String binhLuan) { this.binhLuan = binhLuan; }
    public void setNgayDanhGia(String ngayDanhGia) { this.ngayDanhGia = ngayDanhGia; }
    public void setHinhAnhList(List<HinhAnhDanhGiaDTO> hinhAnhList) { this.hinhAnhList = hinhAnhList; }
    public void setDaDanhGia(Boolean daDanhGia) { this.daDanhGia = daDanhGia; }
    public void setTrangThaiDanhGia(String trangThaiDanhGia) { this.trangThaiDanhGia = trangThaiDanhGia; }
}
