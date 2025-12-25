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
    
    // Thông tin ưu đãi
    private Double phanTramGiam; // Phần trăm giảm giá (VD: 10.0 = giảm 10%)
    private Double giaSauGiam; // Giá sau khi áp dụng ưu đãi
    
    // Danh sách lịch trình (nếu cần)
    private List<LichTrinhLopHoc> lichTrinhList;
    
    // THÊM MỚI: Danh sách hình ảnh slide
    private List<HinhAnhKhoaHoc> hinhAnhList;
    
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
    
    public Double getPhanTramGiam() {
        return phanTramGiam;
    }
    
    public void setPhanTramGiam(Double phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }
    
    public Double getGiaSauGiam() {
        return giaSauGiam;
    }
    
    public void setGiaSauGiam(Double giaSauGiam) {
        this.giaSauGiam = giaSauGiam;
    }
    
    public List<LichTrinhLopHoc> getLichTrinhList() {
        return lichTrinhList;
    }
    
    public void setLichTrinhList(List<LichTrinhLopHoc> lichTrinhList) {
        this.lichTrinhList = lichTrinhList;
    }
    
    // THÊM MỚI: Getter/Setter cho hinhAnhList
    public List<HinhAnhKhoaHoc> getHinhAnhList() {
        return hinhAnhList;
    }
    
    public void setHinhAnhList(List<HinhAnhKhoaHoc> hinhAnhList) {
        this.hinhAnhList = hinhAnhList;
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
     * Lấy giá sau khi giảm dưới dạng format tiền tệ (nếu có ưu đãi)
     */
    public String getGiaSauGiamFormatted() {
        if (giaSauGiam != null) {
            return String.format("%,.0fđ", giaSauGiam).replace(",", ".");
        }
        if (coUuDai != null && coUuDai && giaTien != null) {
            double giaSauGiamTinh = giaTien * 0.9; // Giảm 10%
            return String.format("%,.0fđ", giaSauGiamTinh).replace(",", ".");
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
     * Lấy ngày bắt đầu từ lịch trình đầu tiên
     * Format: "2025-01-15" (yyyy-MM-dd)
     */
    public String getNgayBatDau() {
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            LichTrinhLopHoc lichTrinh = lichTrinhList.get(0);
            
            // Lấy thứ đầu tiên trong tuần từ thuTrongTuan
            String thuTrongTuan = lichTrinh.getThuTrongTuan();
            if (thuTrongTuan != null && !thuTrongTuan.isEmpty()) {
                // Parse thứ đầu tiên (VD: "2,3,4" -> lấy "2")
                String[] days = thuTrongTuan.split(",");
                if (days.length > 0) {
                    String firstDay = days[0].trim();
                    
                    // Tính ngày gần nhất có thứ này
                    return getNextDateForDay(firstDay);
                }
            }
        }
        
        // Mặc định trả về ngày hiện tại
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }
    
    /**
     * Tính ngày gần nhất có thứ được chỉ định
     * @param dayStr Thứ trong tuần (2-7 cho T2-T7, CN hoặc 1 cho Chủ Nhật)
     * @return Ngày dạng "yyyy-MM-dd"
     */
    private String getNextDateForDay(String dayStr) {
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            
            // Chuyển đổi thứ từ string sang Calendar constant
            int targetDay;
            if ("CN".equalsIgnoreCase(dayStr) || "1".equals(dayStr)) {
                targetDay = java.util.Calendar.SUNDAY;
            } else {
                int day = Integer.parseInt(dayStr);
                // Chuyển từ 2-7 (T2-T7) sang Calendar.MONDAY-SATURDAY
                targetDay = day == 7 ? java.util.Calendar.SATURDAY : day + 1;
            }
            
            // Tìm ngày gần nhất có thứ này
            int currentDay = cal.get(java.util.Calendar.DAY_OF_WEEK);
            int daysToAdd = (targetDay - currentDay + 7) % 7;
            if (daysToAdd == 0) daysToAdd = 0; // Nếu là hôm nay thì giữ nguyên
            
            cal.add(java.util.Calendar.DAY_OF_MONTH, daysToAdd);
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            // Nếu lỗi, trả về ngày hiện tại
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new java.util.Date());
        }
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
}
