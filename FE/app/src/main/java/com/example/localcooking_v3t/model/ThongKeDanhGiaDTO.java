package com.example.localcooking_v3t.model;

import java.io.Serializable;

/**
 * DTO cho thống kê đánh giá của khóa học
 */
public class ThongKeDanhGiaDTO implements Serializable {
    private Integer maKhoaHoc;
    private Double diemTrungBinh;
    private Integer tongSoDanhGia;
    private Integer soLuong1Sao;
    private Integer soLuong2Sao;
    private Integer soLuong3Sao;
    private Integer soLuong4Sao;
    private Integer soLuong5Sao;
    private Integer soLuongCoNhanXet;
    private Integer soLuongCoHinhAnh;
    
    // Getters and Setters
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public Double getDiemTrungBinh() { return diemTrungBinh; }
    public void setDiemTrungBinh(Double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
    
    public Integer getTongSoDanhGia() { return tongSoDanhGia; }
    public void setTongSoDanhGia(Integer tongSoDanhGia) { this.tongSoDanhGia = tongSoDanhGia; }
    
    public Integer getSoLuong1Sao() { return soLuong1Sao; }
    public void setSoLuong1Sao(Integer soLuong1Sao) { this.soLuong1Sao = soLuong1Sao; }
    
    public Integer getSoLuong2Sao() { return soLuong2Sao; }
    public void setSoLuong2Sao(Integer soLuong2Sao) { this.soLuong2Sao = soLuong2Sao; }
    
    public Integer getSoLuong3Sao() { return soLuong3Sao; }
    public void setSoLuong3Sao(Integer soLuong3Sao) { this.soLuong3Sao = soLuong3Sao; }
    
    public Integer getSoLuong4Sao() { return soLuong4Sao; }
    public void setSoLuong4Sao(Integer soLuong4Sao) { this.soLuong4Sao = soLuong4Sao; }
    
    public Integer getSoLuong5Sao() { return soLuong5Sao; }
    public void setSoLuong5Sao(Integer soLuong5Sao) { this.soLuong5Sao = soLuong5Sao; }
    
    public Integer getSoLuongCoNhanXet() { return soLuongCoNhanXet; }
    public void setSoLuongCoNhanXet(Integer soLuongCoNhanXet) { this.soLuongCoNhanXet = soLuongCoNhanXet; }
    
    public Integer getSoLuongCoHinhAnh() { return soLuongCoHinhAnh; }
    public void setSoLuongCoHinhAnh(Integer soLuongCoHinhAnh) { this.soLuongCoHinhAnh = soLuongCoHinhAnh; }
    
    /**
     * Tính phần trăm cho progress bar (1-5 sao)
     */
    public int getPercent1Sao() {
        return tongSoDanhGia > 0 ? (soLuong1Sao * 100 / tongSoDanhGia) : 0;
    }
    
    public int getPercent2Sao() {
        return tongSoDanhGia > 0 ? (soLuong2Sao * 100 / tongSoDanhGia) : 0;
    }
    
    public int getPercent3Sao() {
        return tongSoDanhGia > 0 ? (soLuong3Sao * 100 / tongSoDanhGia) : 0;
    }
    
    public int getPercent4Sao() {
        return tongSoDanhGia > 0 ? (soLuong4Sao * 100 / tongSoDanhGia) : 0;
    }
    
    public int getPercent5Sao() {
        return tongSoDanhGia > 0 ? (soLuong5Sao * 100 / tongSoDanhGia) : 0;
    }
}
