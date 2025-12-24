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
    private static final String KEY_AVATAR_URL = "avatarUrl";
    private static final String KEY_LOGIN_METHOD = "loginMethod";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    public void createLoginSession(Integer maNguoiDung, String tenDangNhap, String hoTen, String email, String vaiTro) {
        // Kiểm tra nếu user mới khác user cũ thì xóa lịch sử tìm kiếm
        Integer oldUserId = getMaNguoiDung();
        if (oldUserId != null && oldUserId != -1 && !oldUserId.equals(maNguoiDung)) {
            // User khác đăng nhập, xóa lịch sử tìm kiếm cũ
            clearSearchHistory();
        } else if (oldUserId == -1) {
            // Chưa có user nào đăng nhập trước đó, xóa lịch sử tìm kiếm cũ (nếu có)
            clearSearchHistory();
        }
        
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
    
    public void saveAvatarUrl(String avatarUrl) {
        editor.putString(KEY_AVATAR_URL, avatarUrl);
        editor.apply();
    }
    
    public String getAvatarUrl() {
        return prefs.getString(KEY_AVATAR_URL, null);
    }
    
    public void saveLoginMethod(String loginMethod) {
        editor.putString(KEY_LOGIN_METHOD, loginMethod);
        editor.apply();
    }
    
    public String getLoginMethod() {
        return prefs.getString(KEY_LOGIN_METHOD, "EMAIL");
    }
    
    public void logout() {
        editor.clear();
        editor.apply();
        
        // Xóa lịch sử tìm kiếm khi đăng xuất
        clearSearchHistory();
    }
    
    /**
     * Xóa lịch sử tìm kiếm
     */
    private void clearSearchHistory() {
        SharedPreferences searchPrefs = context.getSharedPreferences("LocalCookingPrefs", Context.MODE_PRIVATE);
        searchPrefs.edit().putString("search_history", "[]").apply();
    }

    public void updateUserInfo(String hoTen, String email) {
        editor.putString(KEY_HO_TEN, hoTen);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }
}
