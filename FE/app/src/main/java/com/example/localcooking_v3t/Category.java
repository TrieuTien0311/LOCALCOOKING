// Category.java
package com.example.localcooking_v3t;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {
    private String tenDanhMuc;
    private String thoiGian;
    private int iconDanhMuc;
    private List<Food> danhSachMon;

    public Category(String tenDanhMuc, String thoiGian, int iconDanhMuc, List<Food> danhSachMon) {
        this.tenDanhMuc = tenDanhMuc;
        this.thoiGian = thoiGian;
        this.iconDanhMuc = iconDanhMuc;
        this.danhSachMon = danhSachMon != null ? danhSachMon : new ArrayList<>();
    }

    protected Category(Parcel in) {
        tenDanhMuc = in.readString();
        thoiGian = in.readString();
        iconDanhMuc = in.readInt();
        danhSachMon = in.createTypedArrayList(Food.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tenDanhMuc);
        dest.writeString(thoiGian);
        dest.writeInt(iconDanhMuc);
        dest.writeTypedList(danhSachMon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getTenDanhMuc() { return tenDanhMuc; }
    public String getThoiGian() { return thoiGian; }
    public int getIconDanhMuc() { return iconDanhMuc; }
    public List<Food> getDanhSachMon() { return danhSachMon; }
}