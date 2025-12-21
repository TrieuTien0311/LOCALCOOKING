package com.example.localcooking_v3t;

public class OrderHistory {
    private int hinhAnh;
    private String tieuDe;
    private String soLuongNguoi;
    private String lich;
    private String diaDiem;
    private  String gia;
    private String thoiGianHuy;
    private String trangThai;
    
    // Thêm các field mới để hỗ trợ đánh giá
    private Integer maDatLich;
    private Integer maKhoaHoc;
    private boolean daDanhGia;

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
        this.thoiGianHuy = null; // Mặc định chưa hủy
    }

    public int getHinhAnh() { return hinhAnh; }
    public String getTieuDe() { return tieuDe; }
    public String getSoLuongNguoi() { return soLuongNguoi; }
    public String getLich() { return lich; }
    public String getDiaDiem() { return diaDiem; }
    public String getGia() { return gia; }
    public String getThoiGianHuy() { return thoiGianHuy; }
    public String getTrangThai() { return trangThai; }

    public void setHinhAnh(int hinhAnh) { this.hinhAnh = hinhAnh; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public void setSoLuongNguoi(String soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    public void setLich(String lich) { this.lich = lich; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    public void setGia(String gia) { this.gia = gia; }

    public void setThoiGianHuy(String thoiGianHuy) { this.thoiGianHuy = thoiGianHuy; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    // Getters và Setters cho các field mới
    public Integer getMaDatLich() { return maDatLich; }
    public void setMaDatLich(Integer maDatLich) { this.maDatLich = maDatLich; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public boolean isDaDanhGia() { return daDanhGia; }
    public void setDaDanhGia(boolean daDanhGia) { this.daDanhGia = daDanhGia; }
}
