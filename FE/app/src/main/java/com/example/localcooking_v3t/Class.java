// Class.java (đổi tên thành CookingClass sau này cho đẹp nhé )
package com.example.localcooking_v3t;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Class implements Parcelable {
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
    private boolean daDienRa;
    private List<Category> lichTrinhLopHoc;

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
        this.daDienRa = false;
        this.lichTrinhLopHoc = new ArrayList<>();
        initDefaultSchedule();
    }

    protected Class(Parcel in) {
        tenLop = in.readString();
        moTa = in.readString();
        thoiGian = in.readString();
        ngay = in.readString();
        diaDiem = in.readString();
        gia = in.readString();
        danhGia = in.readFloat();
        soDanhGia = in.readInt();
        hinhAnh = in.readInt();
        coUuDai = in.readByte() != 0;
        thoiGianKetThuc = in.readString();
        suat = in.readInt();
        isFavorite = in.readByte() != 0;
        daDienRa = in.readByte() != 0;
        lichTrinhLopHoc = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tenLop);
        dest.writeString(moTa);
        dest.writeString(thoiGian);
        dest.writeString(ngay);
        dest.writeString(diaDiem);
        dest.writeString(gia);
        dest.writeFloat(danhGia);
        dest.writeInt(soDanhGia);
        dest.writeInt(hinhAnh);
        dest.writeByte((byte) (coUuDai ? 1 : 0));
        dest.writeString(thoiGianKetThuc);
        dest.writeInt(suat);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (daDienRa ? 1 : 0));
        dest.writeTypedList(lichTrinhLopHoc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // === Tất cả getter/setter giữ nguyên như cũ ===
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
    public boolean isDaDienRa() { return daDienRa; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setDaDienRa(boolean daDienRa) { this.daDienRa = daDienRa; }

    public void addFoodToCategory(int categoryIndex, Food food) {
        if (categoryIndex >= 0 && categoryIndex < lichTrinhLopHoc.size()) {
            lichTrinhLopHoc.get(categoryIndex).getDanhSachMon().add(food);
        }
    }

    public double getGiaSo() {
        try {
            String giaString = gia.replace(".", "").replace("₫", "").trim();
            return Double.parseDouble(giaString);
        } catch (Exception e) {
            return 0;
        }
    }

    private void initDefaultSchedule() {
        lichTrinhLopHoc.add(new Category("Món khai vị", "14:00 - 15:00",
                R.drawable.ic_appetizer_tt, new ArrayList<>()));
        lichTrinhLopHoc.add(new Category("Món chính", "15:00 - 16:30",
                R.drawable.ic_main_dish_tt, new ArrayList<>()));
        lichTrinhLopHoc.add(new Category("Món tráng miệng", "16:30 - 17:00",
                R.drawable.ic_dessert_tt, new ArrayList<>()));
    }
}