package com.example.localcooking_v3t.model;

public class LichTrinhLopHoc {
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private Integer maGiaoVien;
    private String thuTrongTuan; // "2,3,4,5,6,7,CN"
    private String gioBatDau;
    private String gioKetThuc;
    private String diaDiem;
    private Integer soLuongToiDa;
    private Boolean trangThai;
    private Integer soChoConTrong; // Số chỗ còn trống
    private Integer conTrong; // Alias cho soChoConTrong (từ backend DTO)
    
    // Constructor
    public LichTrinhLopHoc() {}
    
    // Getters & Setters
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public Integer getMaGiaoVien() { return maGiaoVien; }
    public void setMaGiaoVien(Integer maGiaoVien) { this.maGiaoVien = maGiaoVien; }
    
    public String getThuTrongTuan() { return thuTrongTuan; }
    public void setThuTrongTuan(String thuTrongTuan) { this.thuTrongTuan = thuTrongTuan; }
    
    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }
    
    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }
    
    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    
    public Integer getSoLuongToiDa() { return soLuongToiDa; }
    public void setSoLuongToiDa(Integer soLuongToiDa) { this.soLuongToiDa = soLuongToiDa; }
    
    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }
    
    public Integer getSoChoConTrong() { 
        // Ưu tiên lấy từ conTrong (từ backend DTO), nếu không có thì lấy soChoConTrong
        return conTrong != null ? conTrong : soChoConTrong; 
    }
    public void setSoChoConTrong(Integer soChoConTrong) { this.soChoConTrong = soChoConTrong; }
    
    public Integer getConTrong() { return conTrong; }
    public void setConTrong(Integer conTrong) { 
        this.conTrong = conTrong;
        // Đồng bộ với soChoConTrong
        this.soChoConTrong = conTrong;
    }
    
    /**
     * Lấy thời gian đã format
     * VD: "14:00:00" - "17:00:00" -> "14:00 - 17:00"
     */
    public String getThoiGianFormatted() {
        if (gioBatDau == null || gioKetThuc == null) {
            return "";
        }
        
        // Loại bỏ giây nếu có
        String gioBatDauShort = gioBatDau.length() > 5 ? gioBatDau.substring(0, 5) : gioBatDau;
        String gioKetThucShort = gioKetThuc.length() > 5 ? gioKetThuc.substring(0, 5) : gioKetThuc;
        
        return gioBatDauShort + " - " + gioKetThucShort;
    }
}
