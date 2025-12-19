package com.example.localcooking_v3t.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "LocalCookingSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_MA_NGUOI_DUNG = "maNguoiDung";
    private static final String KEY_TEN_DANG_NHAP = "tenDangNhap";
    private static final String KEY_HO_TEN = "hoTen";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_VAI_TRO = "vaiTro";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    public void createLoginSession(Integer maNguoiDung, String tenDangNhap, String hoTen, String email, String vaiTro) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_MA_NGUOI_DUNG, maNguoiDung);
        editor.putString(KEY_TEN_DANG_NHAP, tenDangNhap);
        editor.putString(KEY_HO_TEN, hoTen);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_VAI_TRO, vaiTro);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public String getTenDangNhap() {
        return prefs.getString(KEY_TEN_DANG_NHAP, null);
    }
    
    public String getHoTen() {
        return prefs.getString(KEY_HO_TEN, null);
    }
    
    public Integer getMaNguoiDung() {
        return prefs.getInt(KEY_MA_NGUOI_DUNG, -1);
    }
    
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }
    
    public String getVaiTro() {
        return prefs.getString(KEY_VAI_TRO, null);
    }
    
    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void updateUserInfo(String hoTen, String email) {
        editor.putString(KEY_HO_TEN, hoTen);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }
}
