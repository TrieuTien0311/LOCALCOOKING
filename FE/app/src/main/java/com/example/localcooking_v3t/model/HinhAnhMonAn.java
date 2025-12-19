package com.example.localcooking_v3t.model;

public class HinhAnhMonAn {
    private Integer maHinhAnh;
    private Integer maMonAn;
    private String duongDan;
    private Integer thuTu;

    public HinhAnhMonAn() {
    }

    public HinhAnhMonAn(Integer maHinhAnh, Integer maMonAn, String duongDan, Integer thuTu) {
        this.maHinhAnh = maHinhAnh;
        this.maMonAn = maMonAn;
        this.duongDan = duongDan;
        this.thuTu = thuTu;
    }

    public Integer getMaHinhAnh() {
        return maHinhAnh;
    }

    public void setMaHinhAnh(Integer maHinhAnh) {
        this.maHinhAnh = maHinhAnh;
    }

    public Integer getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(Integer maMonAn) {
        this.maMonAn = maMonAn;
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
