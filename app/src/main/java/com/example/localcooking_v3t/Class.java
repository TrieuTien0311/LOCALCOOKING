package com.example.localcooking_v3t;

public class Class {
    private String tenLop;
    private String moTa;
    private String thoiGian;
    private String ngay;
    private String diaDiem;
    private String gia;
    private float danhGia;
    private int soDanhGia;
    private int hinhAnh;
    private boolean coUuDai;
    private String thoiGianKetThuc;
    private int suat;
    private boolean isFavorite; // Thêm thuộc tính này

    // Constructor
    public Class(String tenLop, String moTa, String thoiGian, String ngay,
                 String diaDiem, String gia, float danhGia, int soDanhGia,
                 int hinhAnh, boolean coUuDai, String thoiGianKetThuc, int suat) {
        this.tenLop = tenLop;
        this.moTa = moTa;
        this.thoiGian = thoiGian;
        this.ngay = ngay;
        this.diaDiem = diaDiem;
        this.gia = gia;
        this.danhGia = danhGia;
        this.soDanhGia = soDanhGia;
        this.hinhAnh = hinhAnh;
        this.coUuDai = coUuDai;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.suat = suat;
        this.isFavorite = false; // Mặc định chưa yêu thích
    }

    // Getters
    public String getTenLop() { return tenLop; }
    public String getMoTa() { return moTa; }
    public String getThoiGian() { return thoiGian; }
    public String getNgay() { return ngay; }
    public String getDiaDiem() { return diaDiem; }
    public String getGia() { return gia; }
    public float getDanhGia() { return danhGia; }
    public int getSoDanhGia() { return soDanhGia; }
    public int getHinhAnh() { return hinhAnh; }
    public boolean isCoUuDai() { return coUuDai; }
    public String getThoiGianKetThuc() { return thoiGianKetThuc; }
    public int getSuat() { return suat; }
    public boolean isFavorite() { return isFavorite; }

    // Setters
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // Phương thức để lấy giá dưới dạng số (dùng cho sắp xếp)
    public double getGiaSo() {
        try {
            // Loại bỏ dấu phẩy, chữ "đ" và "₫" rồi parse thành số
            String giaString = gia.replace(".", "").replace(",", "")
                    .replace("đ", "").replace("₫", "").trim();
            return Double.parseDouble(giaString);
        } catch (Exception e) {
            return 0;
        }
    }
}