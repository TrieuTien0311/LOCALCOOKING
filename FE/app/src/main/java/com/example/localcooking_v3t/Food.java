// Food.java
package com.example.localcooking_v3t;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String tenMon;
    private String gioiThieu;
    private String nguyenLieu;
    private int hinhAnh;

    public Food(String tenMon, String gioiThieu, String nguyenLieu, int hinhAnh) {
        this.tenMon = tenMon;
        this.gioiThieu = gioiThieu;
        this.nguyenLieu = nguyenLieu;
        this.hinhAnh = hinhAnh;
    }

    protected Food(Parcel in) {
        tenMon = in.readString();
        gioiThieu = in.readString();
        nguyenLieu = in.readString();
        hinhAnh = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tenMon);
        dest.writeString(gioiThieu);
        dest.writeString(nguyenLieu);
        dest.writeInt(hinhAnh);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getTenMon() { return tenMon; }
    public String getGioiThieu() { return gioiThieu; }
    public String getNguyenLieu() { return nguyenLieu; }
    public int getHinhAnh() { return hinhAnh; }
}