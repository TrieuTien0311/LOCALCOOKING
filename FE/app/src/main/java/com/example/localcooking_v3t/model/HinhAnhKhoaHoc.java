package com.example.localcooking_v3t.model;

import android.content.Context;
import com.example.localcooking_v3t.R;

public class HinhAnhKhoaHoc {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan;
    private Integer thuTu;
    
    // Constructors
    public HinhAnhKhoaHoc() {}
    
    public HinhAnhKhoaHoc(Integer maHinhAnh, Integer maKhoaHoc, String duongDan, Integer thuTu) {
        this.maHinhAnh = maHinhAnh;
        this.maKhoaHoc = maKhoaHoc;
        this.duongDan = duongDan;
        this.thuTu = thuTu;
    }
    
    // Getters & Setters
    public Integer getMaHinhAnh() { 
        return maHinhAnh; 
    }
    
    public void setMaHinhAnh(Integer maHinhAnh) { 
        this.maHinhAnh = maHinhAnh; 
    }
    
    public Integer getMaKhoaHoc() { 
        return maKhoaHoc; 
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) { 
        this.maKhoaHoc = maKhoaHoc; 
    }
    
    public String getDuongDan() { 
        return duongDan; 
    }
    
    public void setDuongDan(String duongDan) { 
        this.duongDan = duongDan; 
    }
    
    public Integer getThuTu() { 
        return thuTu; 
    }
    
    public void setThuTu(Integer thuTu) { 
        this.thuTu = thuTu; 
    }
    
    /**
     * Lấy resource ID từ tên file ảnh
     * VD: "am_thuc_pho_co_ha_noi_2.jpg" -> R.drawable.am_thuc_pho_co_ha_noi_2
     */
    public int getHinhAnhResId(Context context) {
        if (duongDan == null || duongDan.isEmpty()) {
            return R.drawable.hue; // Ảnh mặc định
        }
        
        // Loại bỏ extension .jpg, .png
        String imageName = duongDan.replace(".jpg", "").replace(".png", "");
        
        int resId = context.getResources().getIdentifier(
            imageName, 
            "drawable", 
            context.getPackageName()
        );
        
        return resId != 0 ? resId : R.drawable.hue;
    }
}
