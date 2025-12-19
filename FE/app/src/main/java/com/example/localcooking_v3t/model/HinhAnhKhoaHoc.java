package com.example.localcooking_v3t.model;

public class HinhAnhKhoaHoc {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan;
    private Integer thuTu;

    public HinhAnhKhoaHoc() {
    }

    public HinhAnhKhoaHoc(Integer maHinhAnh, Integer maKhoaHoc, String duongDan, Integer thuTu) {
        this.maHinhAnh = maHinhAnh;
        this.maKhoaHoc = maKhoaHoc;
        this.duongDan = duongDan;
        this.thuTu = thuTu;
    }

    public Integer getMaHinhAnh() {
        return maHinhAnh;
    }

    public void setMaHinhAnh(Integer maHinhAnh) {
        this.maHinhAnh = maHinhAnh;
    }

    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }

    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
    }

    public Integer getThuTu() {
        return thuTu;
    }

    public void setThuTu(Integer thuTu) {
        this.thuTu = thuTu;
    }
}
