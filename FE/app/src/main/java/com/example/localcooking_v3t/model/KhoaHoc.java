package com.example.localcooking_v3t.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model KhoaHoc - Tương thích với backend
 * Chứa thông tin về khóa học (nội dung, giá, mô tả)
 */
public class KhoaHoc implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private Double giaTien;  // Backend trả về BigDecimal, Android nhận Double
    private String hinhAnh;
    private Integer soLuongDanhGia;
    private Float saoTrungBinh;
    private Boolean coUuDai;
    private String ngayTao;
    
    // Danh sách lịch trình (nếu cần)
    private List<LichTrinhLopHoc> lichTrinhList;
    
    // Constructors
    public KhoaHoc() {
        this.soLuongDanhGia = 0;
        this.saoTrungBinh = 0.0f;
        this.coUuDai = false;
    }
    
    public KhoaHoc(Integer maKhoaHoc, String tenKhoaHoc, String moTa, String gioiThieu,
                   String giaTriSauBuoiHoc, Double giaTien, String hinhAnh,
                   Integer soLuongDanhGia, Float saoTrungBinh, Boolean coUuDai) {
        this.maKhoaHoc = maKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
        this.moTa = moTa;
        this.gioiThieu = gioiThieu;
        this.giaTriSauBuoiHoc = giaTriSauBuoiHoc;
        this.giaTien = giaTien;
        this.hinhAnh = hinhAnh;
        this.soLuongDanhGia = soLuongDanhGia;
        this.saoTrungBinh = saoTrungBinh;
        this.coUuDai = coUuDai;
    }
    
    // Getters and Setters
    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }
    
    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }
    
    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public String getGioiThieu() {
        return gioiThieu;
    }
    
    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
    
    public String getGiaTriSauBuoiHoc() {
        return giaTriSauBuoiHoc;
    }
    
    public void setGiaTriSauBuoiHoc(String giaTriSauBuoiHoc) {
        this.giaTriSauBuoiHoc = giaTriSauBuoiHoc;
    }
    
    public Double getGiaTien() {
        return giaTien;
    }
    
    public void setGiaTien(Double giaTien) {
        this.giaTien = giaTien;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    
    public Integer getSoLuongDanhGia() {
        return soLuongDanhGia;
    }
    
    public void setSoLuongDanhGia(Integer soLuongDanhGia) {
        this.soLuongDanhGia = soLuongDanhGia;
    }
    
    public Float getSaoTrungBinh() {
        return saoTrungBinh;
    }
    
    public void setSaoTrungBinh(Float saoTrungBinh) {
        this.saoTrungBinh = saoTrungBinh;
    }
    
    public Boolean getCoUuDai() {
        return coUuDai;
    }
    
    public void setCoUuDai(Boolean coUuDai) {
        this.coUuDai = coUuDai;
    }
    
    public String getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public List<LichTrinhLopHoc> getLichTrinhList() {
        return lichTrinhList;
    }
    
    public void setLichTrinhList(List<LichTrinhLopHoc> lichTrinhList) {
        this.lichTrinhList = lichTrinhList;
    }
    
    // Helper methods
    
    /**
     * Lấy giá dưới dạng format tiền tệ
     * VD: 650000 -> "650.000đ"
     */
    public String getGiaFormatted() {
        if (giaTien == null) {
            return "0đ";
        }
        return String.format("%,.0fđ", giaTien).replace(",", ".");
    }
    
    /**
     * Lấy giá sau khi giảm (nếu có ưu đãi)
     */
    public String getGiaSauGiam() {
        if (coUuDai != null && coUuDai && giaTien != null) {
            double giaSauGiam = giaTien * 0.9; // Giảm 10%
            return String.format("%,.0fđ", giaSauGiam).replace(",", ".");
        }
        return getGiaFormatted();
    }
    
    /**
     * Lấy địa phương từ lịch trình
     */
    public String getDiaPhuong() {
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            String diaDiem = lichTrinhList.get(0).getDiaDiem();
            if (diaDiem != null) {
                if (diaDiem.contains("Hà Nội")) return "Hà Nội";
                if (diaDiem.contains("Huế")) return "Huế";
                if (diaDiem.contains("Đà Nẵng")) return "Đà Nẵng";
                if (diaDiem.contains("Cần Thơ")) return "Cần Thơ";
            }
        }
        return "";
    }
    
    // ===== METHODS ĐỂ TƯƠNG THÍCH VỚI ClassAdapter =====
    
    /**
     * Alias cho getTenKhoaHoc() - để tương thích với code cũ
     */
    public String getTenLop() {
        return tenKhoaHoc;
    }
    
    /**
     * Lấy thời gian từ lịch trình đầu tiên
     */
    public String getThoiGian() {
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            return lichTrinhList.get(0).getThoiGianFormatted();
        }
        return "";
    }
    
    /**
     * Lấy ngày bắt đầu - mặc định "2025-01-01"
     */
    public String getNgayBatDau() {
        return "2025-01-01";
    }
    
    /**
     * Lấy địa điểm từ lịch trình đầu tiên
     */
    public String getDiaDiem() {
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            return lichTrinhList.get(0).getDiaDiem();
        }
        return "";
    }
    
    /**
     * Alias cho getGiaFormatted() - để tương thích với code cũ
     */
    public String getGia() {
        return getGiaFormatted();
    }
    
    /**
     * Alias cho getSaoTrungBinh() - để tương thích với code cũ
     */
    public Float getDanhGia() {
        return saoTrungBinh;
    }
    
    /**
     * Alias cho getSoLuongDanhGia() - để tương thích với code cũ
     */
    public Integer getSoDanhGia() {
        return soLuongDanhGia;
    }
    
    /**
     * Lấy số suất còn lại từ lịch trình đầu tiên
     */
    public Integer getSuat() {
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            return lichTrinhList.get(0).getSoChoConTrong();
        }
        return null;
    }
    
    /**
     * Trạng thái yêu thích - mặc định false
     */
    private Boolean isFavorite = false;
    
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    /**
     * Trạng thái đã diễn ra - mặc định false
     */
    private Boolean daDienRa = false;
    
    public Boolean getDaDienRa() {
        return daDienRa;
    }
    
    public void setDaDienRa(Boolean daDienRa) {
        this.daDienRa = daDienRa;
    }
    
    /**
     * Lấy giá dưới dạng số (double)
     * Parse từ string "715.000đ" hoặc "715000" thành 715000.0
     */
    public double getGiaSo() {
        if (giaTien == null) {
            return 0.0;
        }
        return giaTien;
    }
    
    /**
     * Lấy ngày - mặc định lấy từ ngày bắt đầu và format lại
     */
    public String getNgay() {
        // Format từ "2025-01-01" sang "01/01/2025"
        String ngayBatDau = getNgayBatDau();
        if (ngayBatDau != null && !ngayBatDau.isEmpty()) {
            try {
                String[] parts = ngayBatDau.split("-");
                if (parts.length == 3) {
                    return parts[2] + "/" + parts[1] + "/" + parts[0];
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return "";
    }
    
    /**
     * Lấy thời gian kết thúc ưu đãi - mặc định "23:59:59"
     */
    public String getThoiGianKetThuc() {
        return "23:59:59";
    }
    
    /**
     * Alias cho setIsFavorite() - để tương thích với code cũ
     */
    public void setFavorite(Boolean favorite) {
        this.isFavorite = favorite;
    }
    
    /**
     * Kiểm tra có đánh giá không
     */
    public boolean hasRating() {
        return soLuongDanhGia != null && soLuongDanhGia > 0;
    }
    
    /**
     * Lấy text đánh giá
     * VD: "4.5 (120 đánh giá)"
     */
    public String getRatingText() {
        if (!hasRating()) {
            return "Chưa có đánh giá";
        }
        return String.format("%.1f (%d đánh giá)", saoTrungBinh, soLuongDanhGia);
    }
    public int getHinhAnhResId(android.content.Context context) {
        if (hinhAnh == null || hinhAnh.isEmpty()) {
            return context.getResources().getIdentifier("hue", "drawable", context.getPackageName());
        }

        // Loại bỏ extension
        String name = hinhAnh.replace(".png", "").replace(".jpg", "");

        // Lấy resource ID
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resId != 0 ? resId : context.getResources().getIdentifier("hue", "drawable", context.getPackageName());
    }
}
