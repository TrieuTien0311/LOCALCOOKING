package com.example.localcooking_v3t.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model LichTrinhLopHoc - Tương thích với backend
 * Chứa thông tin về lịch trình (thời gian, địa điểm, giáo viên)
 */
public class LichTrinhLopHoc implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private Integer maGiaoVien;
    private String thuTrongTuan;  // VD: "2,3,4,5,6,7,CN"
    private String gioBatDau;     // VD: "17:30:00"
    private String gioKetThuc;    // VD: "20:30:00"
    private String diaDiem;
    private Integer soLuongToiDa;
    private Boolean trangThai;
    
    // Thông tin bổ sung (nếu backend trả về)
    private String tenGiaoVien;
    
    @SerializedName("conTrong")  // Backend trả về "conTrong", Android map thành "soChoConTrong"
    private Integer soChoConTrong;
    
    // Constructors
    public LichTrinhLopHoc() {
        this.soLuongToiDa = 20;
        this.trangThai = true;
    }
    
    public LichTrinhLopHoc(Integer maLichTrinh, Integer maKhoaHoc, Integer maGiaoVien,
                           String thuTrongTuan, String gioBatDau, String gioKetThuc,
                           String diaDiem, Integer soLuongToiDa, Boolean trangThai) {
        this.maLichTrinh = maLichTrinh;
        this.maKhoaHoc = maKhoaHoc;
        this.maGiaoVien = maGiaoVien;
        this.thuTrongTuan = thuTrongTuan;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.diaDiem = diaDiem;
        this.soLuongToiDa = soLuongToiDa;
        this.trangThai = trangThai;
    }
    
    // Getters and Setters
    public Integer getMaLichTrinh() {
        return maLichTrinh;
    }
    
    public void setMaLichTrinh(Integer maLichTrinh) {
        this.maLichTrinh = maLichTrinh;
    }
    
    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }
    
    public Integer getMaGiaoVien() {
        return maGiaoVien;
    }
    
    public void setMaGiaoVien(Integer maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }
    
    public String getThuTrongTuan() {
        return thuTrongTuan;
    }
    
    public void setThuTrongTuan(String thuTrongTuan) {
        this.thuTrongTuan = thuTrongTuan;
    }
    
    public String getGioBatDau() {
        return gioBatDau;
    }
    
    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }
    
    public String getGioKetThuc() {
        return gioKetThuc;
    }
    
    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }
    
    public String getDiaDiem() {
        return diaDiem;
    }
    
    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }
    
    public Integer getSoLuongToiDa() {
        return soLuongToiDa;
    }
    
    public void setSoLuongToiDa(Integer soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }
    
    public Boolean getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
    
    public String getTenGiaoVien() {
        return tenGiaoVien;
    }
    
    public void setTenGiaoVien(String tenGiaoVien) {
        this.tenGiaoVien = tenGiaoVien;
    }
    
    public Integer getSoChoConTrong() {
        return soChoConTrong;
    }
    
    public void setSoChoConTrong(Integer soChoConTrong) {
        this.soChoConTrong = soChoConTrong;
    }
    
    // Helper methods
    
    /**
     * Lấy thời gian dạng "17:30 - 20:30"
     */
    public String getThoiGianFormatted() {
        if (gioBatDau == null || gioKetThuc == null) {
            return "";
        }
        
        // Loại bỏ giây nếu có (17:30:00 -> 17:30)
        String gioBatDauShort = gioBatDau.length() > 5 ? gioBatDau.substring(0, 5) : gioBatDau;
        String gioKetThucShort = gioKetThuc.length() > 5 ? gioKetThuc.substring(0, 5) : gioKetThuc;
        
        return gioBatDauShort + " - " + gioKetThucShort;
    }
    
    /**
     * Lấy địa phương từ địa chỉ
     * VD: "45 Hàng Bạc, Hoàn Kiếm, Hà Nội" -> "Hà Nội"
     */
    public String getDiaPhuong() {
        if (diaDiem == null) {
            return "";
        }
        
        if (diaDiem.contains("Hà Nội")) return "Hà Nội";
        if (diaDiem.contains("Huế")) return "Huế";
        if (diaDiem.contains("Đà Nẵng")) return "Đà Nẵng";
        if (diaDiem.contains("Cần Thơ")) return "Cần Thơ";
        
        return "";
    }
    
    /**
     * Kiểm tra có học vào thứ này không
     * @param thu Thứ cần kiểm tra (2-7 cho T2-T7, 1 hoặc "CN" cho Chủ Nhật)
     */
    public boolean isHocVaoThu(String thu) {
        if (thuTrongTuan == null || thuTrongTuan.isEmpty()) {
            return false;
        }
        
        // Chuyển đổi Chủ Nhật
        if ("1".equals(thu) || "CN".equalsIgnoreCase(thu)) {
            return thuTrongTuan.contains("CN") || thuTrongTuan.contains("1");
        }
        
        return thuTrongTuan.contains(thu);
    }
    
    /**
     * Lấy danh sách các thứ học trong tuần
     * VD: "2,3,4,5,6,7,CN" -> "T2, T3, T4, T5, T6, T7, CN"
     */
    public String getThuHocFormatted() {
        if (thuTrongTuan == null || thuTrongTuan.isEmpty()) {
            return "";
        }
        
        String[] days = thuTrongTuan.split(",");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < days.length; i++) {
            String day = days[i].trim();
            
            if ("CN".equalsIgnoreCase(day) || "1".equals(day)) {
                result.append("CN");
            } else {
                result.append("T").append(day);
            }
            
            if (i < days.length - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }
    
    /**
     * Kiểm tra còn chỗ trống không
     */
    public boolean isConCho() {
        return soChoConTrong != null && soChoConTrong > 0;
    }
    
    /**
     * Lấy text trạng thái chỗ
     * VD: "Còn 17 chỗ", "Hết chỗ", "Sắp hết chỗ"
     */
    public String getTrangThaiChoText() {
        if (soChoConTrong == null) {
            return "";
        }
        
        if (soChoConTrong == 0) {
            return "Hết chỗ";
        } else if (soChoConTrong <= 5) {
            return "Sắp hết chỗ (" + soChoConTrong + " chỗ)";
        } else {
            return "Còn " + soChoConTrong + " chỗ";
        }
    }
    
    /**
     * Kiểm tra lịch trình có đang hoạt động không
     */
    public boolean isActive() {
        return trangThai != null && trangThai;
    }
}
