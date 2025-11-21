package com.example.localcooking_v3t;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isFavorite;
    private List<Category> lichTrinhLopHoc; // Thêm lịch trình

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
        this.isFavorite = false;
        this.lichTrinhLopHoc = new ArrayList<>();

        // Khởi tạo lịch trình mặc định
        initDefaultSchedule();
    }

    // Khởi tạo lịch trình mặc định (3 danh mục)
    private void initDefaultSchedule() {
        lichTrinhLopHoc.add(new Category("Món khai vị", "14:00 - 15:00",
                R.drawable.ic_appetizer_tt, new ArrayList<>()));
        lichTrinhLopHoc.add(new Category("Món chính", "15:00 - 16:30",
                R.drawable.ic_main_dish_tt, new ArrayList<>()));
        lichTrinhLopHoc.add(new Category("Món tráng miệng", "16:30 - 17:00",
                R.drawable.ic_dessert_tt, new ArrayList<>()));
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
    public List<Category> getLichTrinhLopHoc() { return lichTrinhLopHoc; }

    // Setters
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setLichTrinhLopHoc(List<Category> lichTrinhLopHoc) {
        this.lichTrinhLopHoc = lichTrinhLopHoc;
    }

    // Phương thức thêm món vào danh mục
    public void addFoodToCategory(int categoryIndex, Food food) {
        if (categoryIndex >= 0 && categoryIndex < lichTrinhLopHoc.size()) {
            lichTrinhLopHoc.get(categoryIndex).getDanhSachMon().add(food);
        }
    }

    public double getGiaSo() {
        try {
            String giaString = gia.replace(".", "").replace(",", "")
                    .replace("đ", "").replace("₫", "").trim();
            return Double.parseDouble(giaString);
        } catch (Exception e) {
            return 0;
        }
    }
}