package com.example.localcooking_v3t.model;

import java.io.Serializable;

public class LopHoc implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer maLopHoc;
    private String tenLop;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private String thoiGian;
    private String loaiLich;
    private String ngayBatDau;
    private String ngayKetThuc;
    private String cacNgayTrongTuan;
    private String ngay;
    private String diaDiem;
    private String gia;
    private Float danhGia;
    private Integer soDanhGia;
    private String hinhAnh;
    private Boolean coUuDai;
    private String thoiGianKetThuc;
    private Integer suat;
    private Boolean isFavorite;
    private Boolean daDienRa;
    private String tenGiaoVien;
    private String trangThai;

    // Getters and Setters
    public Integer getMaLopHoc() { return maLopHoc; }
    public void setMaLopHoc(Integer maLopHoc) { this.maLopHoc = maLopHoc; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getGioiThieu() { return gioiThieu; }
    public void setGioiThieu(String gioiThieu) { this.gioiThieu = gioiThieu; }

    public String getGiaTriSauBuoiHoc() { return giaTriSauBuoiHoc; }
    public void setGiaTriSauBuoiHoc(String giaTriSauBuoiHoc) { this.giaTriSauBuoiHoc = giaTriSauBuoiHoc; }

    public String getThoiGian() { return thoiGian; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }

    public String getLoaiLich() { return loaiLich; }
    public void setLoaiLich(String loaiLich) { this.loaiLich = loaiLich; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getCacNgayTrongTuan() { return cacNgayTrongTuan; }
    public void setCacNgayTrongTuan(String cacNgayTrongTuan) { this.cacNgayTrongTuan = cacNgayTrongTuan; }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }

    public String getGia() { return gia; }
    public void setGia(String gia) { this.gia = gia; }

    public Float getDanhGia() { return danhGia; }
    public void setDanhGia(Float danhGia) { this.danhGia = danhGia; }

    public Integer getSoDanhGia() { return soDanhGia; }
    public void setSoDanhGia(Integer soDanhGia) { this.soDanhGia = soDanhGia; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Boolean getCoUuDai() { return coUuDai; }
    public void setCoUuDai(Boolean coUuDai) { this.coUuDai = coUuDai; }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(String thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public Integer getSuat() { return suat; }
    public void setSuat(Integer suat) { this.suat = suat; }

    public Boolean getIsFavorite() { return isFavorite; }
    public void setIsFavorite(Boolean isFavorite) { this.isFavorite = isFavorite; }

    public Boolean getDaDienRa() { return daDienRa; }
    public void setDaDienRa(Boolean daDienRa) { this.daDienRa = daDienRa; }

    public String getTenGiaoVien() { return tenGiaoVien; }
    public void setTenGiaoVien(String tenGiaoVien) { this.tenGiaoVien = tenGiaoVien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    // Helper method để lấy địa phương từ địa chỉ
    public String getDiaPhuong() {
        if (diaDiem != null) {
            if (diaDiem.contains("Hà Nội")) return "Hà Nội";
            if (diaDiem.contains("Huế")) return "Huế";
            if (diaDiem.contains("Đà Nẵng")) return "Đà Nẵng";
            if (diaDiem.contains("Cần Thơ")) return "Cần Thơ";
        }
        return "";
    }
    
    // Helper method để tính giá sau giảm
    public String getGiaSauGiam() {
        if (coUuDai != null && coUuDai && gia != null) {
            try {
                String giaStr = gia.replace("₫", "").replace(".", "").trim();
                double giaGoc = Double.parseDouble(giaStr);
                double giaSauGiam = giaGoc * 0.9; // Giảm 10%
                return String.format("%.0f₫", giaSauGiam);
            } catch (Exception e) {
                return gia;
            }
        }
        return gia;
    }
    
    /**
     * Lấy giá dưới dạng số (double)
     * Parse từ string "715.000đ" hoặc "715000" thành 715000.0
     */
    public double getGiaSo() {
        if (gia == null || gia.isEmpty()) {
            return 0.0;
        }
        
        try {
            // Loại bỏ ký tự không phải số (đ, ₫, dấu chấm, dấu phẩy)
            String giaStr = gia.replaceAll("[^0-9]", "");
            return Double.parseDouble(giaStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Lấy resource ID của hình ảnh
     */
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
