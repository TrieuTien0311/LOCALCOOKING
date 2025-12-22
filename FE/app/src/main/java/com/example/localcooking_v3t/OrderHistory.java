package com.example.localcooking_v3t;

import android.content.Context;
import com.example.localcooking_v3t.model.DonDatLichDTO;
import java.math.BigDecimal;

public class OrderHistory {
    private int hinhAnh;
    private String tieuDe;
    private String soLuongNguoi;
    private String lich;
    private String diaDiem;
    private String gia;
    private String thoiGianHuy;
    private String trangThai;
    
    // Thêm các field mới
    private Integer maDatLich;
    private Integer maKhoaHoc;
    private Integer maLichTrinh;
    private boolean daDanhGia;
    // URL hình ảnh từ server
    private String hinhAnhUrl; 

    private Boolean daThanhToan;
    
    // Thông tin để thanh toán lại
    private String ngayThamGia;
    private String thoiGian;
    private String hinhAnhPath;
    private String moTa;
    private BigDecimal tongTienGoc;
    private int soLuongNguoiInt;
    
    // Thông tin người đặt
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;


    // Constructor cũ (giữ nguyên để tương thích)
    public OrderHistory(int hinhAnh, String tieuDe, String soLuongNguoi, String lich,
                        String diaDiem, String gia, String thoiGianHuy, String trangThai) {
        this.hinhAnh = hinhAnh;
        this.tieuDe = tieuDe;
        this.soLuongNguoi = soLuongNguoi;
        this.lich = lich;
        this.diaDiem = diaDiem;
        this.gia = gia;
        this.thoiGianHuy = thoiGianHuy;
        this.trangThai = trangThai;
        this.daThanhToan = false;
    }

    public OrderHistory(int hinhAnh, String tieuDe, String soLuongNguoi, String lich,
                        String diaDiem, String gia, String trangThai) {
        this.hinhAnh = hinhAnh;
        this.tieuDe = tieuDe;
        this.soLuongNguoi = soLuongNguoi;
        this.lich = lich;
        this.diaDiem = diaDiem;
        this.gia = gia;
        this.trangThai = trangThai;
        this.thoiGianHuy = null;
        this.daThanhToan = false;
    }
    
    // Constructor mới từ DTO
    public OrderHistory(DonDatLichDTO dto, Context context) {
        this.maDatLich = dto.getMaDatLich();
        this.maKhoaHoc = dto.getMaKhoaHoc();
        this.maLichTrinh = dto.getMaLichTrinh();
        this.tieuDe = dto.getTenKhoaHoc();
        this.soLuongNguoiInt = dto.getSoLuongNguoi() != null ? dto.getSoLuongNguoi() : 1;
        this.soLuongNguoi = soLuongNguoiInt + " người";
        this.lich = formatLich(dto.getThoiGian(), dto.getNgayThamGia());
        this.diaDiem = dto.getDiaDiem();
        this.gia = formatCurrency(dto.getTongTien());
        this.trangThai = dto.getTrangThai();
        this.daThanhToan = dto.getDaThanhToan() != null ? dto.getDaThanhToan() : false;
        this.daDanhGia = dto.getDaDanhGia() != null ? dto.getDaDanhGia() : false;
        this.thoiGianHuy = dto.getThoiGianHuy();
        this.ngayThamGia = dto.getNgayThamGia();
        this.thoiGian = dto.getThoiGian();
        this.moTa = dto.getMoTa();
        this.hinhAnhPath = dto.getHinhAnh();
        this.tongTienGoc = dto.getTongTien();
        
        // Thông tin người đặt
        this.tenNguoiDat = dto.getTenNguoiDat();
        this.emailNguoiDat = dto.getEmailNguoiDat();
        this.sdtNguoiDat = dto.getSdtNguoiDat();
        
        // Convert hình ảnh từ tên file sang resource ID
        this.hinhAnh = getImageResId(dto.getHinhAnh(), context);
    }
    
    // Helper methods
    private String formatLich(String thoiGian, String ngayThamGia) {
        if (thoiGian != null && ngayThamGia != null) {
            return thoiGian + ", " + formatDate(ngayThamGia);
        }
        return "";
    }
    
    private String formatDate(String dateStr) {
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) {
            // Ignore
        }
        return dateStr;
    }
    
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0₫";
        return String.format("%,.0f₫", amount.doubleValue()).replace(",", ".");
    }
    
    private int getImageResId(String imageName, Context context) {
        if (imageName == null || imageName.isEmpty() || context == null) {
            return R.drawable.hue;
        }
        String name = imageName.replace(".jpg", "").replace(".png", "");
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resId != 0 ? resId : R.drawable.hue;
    }

    // Getters và Setters
    public int getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(int hinhAnh) { this.hinhAnh = hinhAnh; }
    
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    
    public String getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(String soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    
    public String getLich() { return lich; }
    public void setLich(String lich) { this.lich = lich; }
    
    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    
    public String getGia() { return gia; }
    public void setGia(String gia) { this.gia = gia; }
    
    public String getThoiGianHuy() { return thoiGianHuy; }
    public void setThoiGianHuy(String thoiGianHuy) { this.thoiGianHuy = thoiGianHuy; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Integer getMaDatLich() { return maDatLich; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public boolean isDaDanhGia() { return daDanhGia; }
    public void setDaDanhGia(boolean daDanhGia) { this.daDanhGia = daDanhGia; }
    
    public String getHinhAnhUrl() { return hinhAnhUrl; }
    public void setHinhAnhUrl(String hinhAnhUrl) { this.hinhAnhUrl = hinhAnhUrl; }
    public Boolean getDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(Boolean daThanhToan) { this.daThanhToan = daThanhToan; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public String getThoiGian() { return thoiGian; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }
    
    public String getHinhAnhPath() { return hinhAnhPath; }
    public void setHinhAnhPath(String hinhAnhPath) { this.hinhAnhPath = hinhAnhPath; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public BigDecimal getTongTienGoc() { return tongTienGoc; }
    public void setTongTienGoc(BigDecimal tongTienGoc) { this.tongTienGoc = tongTienGoc; }
    
    public int getSoLuongNguoiInt() { return soLuongNguoiInt; }
    public void setSoLuongNguoiInt(int soLuongNguoiInt) { this.soLuongNguoiInt = soLuongNguoiInt; }
    
    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }
    
    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }
    
    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }
}
