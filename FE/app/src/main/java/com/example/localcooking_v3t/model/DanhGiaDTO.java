package com.example.localcooking_v3t.model;

import java.io.Serializable;
import java.util.List;

/**
 * DTO cho đánh giá khóa học
 */
public class DanhGiaDTO implements Serializable {
    private Integer maDanhGia;
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maKhoaHoc;
    private Integer diemDanhGia;
    private String binhLuan;
    private String ngayDanhGia;
    
    // Thông tin người đánh giá
    private String tenDangNhap;
    private String avatar;
    
    // Ngày tham gia lớp học
    private String ngayThamGia;
    
    // Danh sách hình ảnh/video đánh giá
    private List<HinhAnhDanhGiaDTO> hinhAnhList;
    
    // Getters and Setters
    public Integer getMaDanhGia() { return maDanhGia; }
    public void setMaDanhGia(Integer maDanhGia) { this.maDanhGia = maDanhGia; }
    
    public Integer getMaDatLich() { return maDatLich; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public Integer getDiemDanhGia() { return diemDanhGia; }
    public void setDiemDanhGia(Integer diemDanhGia) { this.diemDanhGia = diemDanhGia; }
    
    public String getBinhLuan() { return binhLuan; }
    public void setBinhLuan(String binhLuan) { this.binhLuan = binhLuan; }
    
    public String getNgayDanhGia() { return ngayDanhGia; }
    public void setNgayDanhGia(String ngayDanhGia) { this.ngayDanhGia = ngayDanhGia; }
    
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public List<HinhAnhDanhGiaDTO> getHinhAnhList() { return hinhAnhList; }
    public void setHinhAnhList(List<HinhAnhDanhGiaDTO> hinhAnhList) { this.hinhAnhList = hinhAnhList; }
    
    public boolean hasMedia() {
        return hinhAnhList != null && !hinhAnhList.isEmpty();
    }
}
