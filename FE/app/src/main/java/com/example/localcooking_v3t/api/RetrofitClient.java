package com.example.localcooking_v3t.api;

import android.os.Build;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    
    // ===== CẤU HÌNH IP =====
    // IP máy tính khi dùng Hotspot: 192.168.137.1
    // (Từ ipconfig: Wireless LAN adapter Local Area Connection* 10)
    private static final String IP_MAY_TINH = "192.168.137.1";
    
    // Tự động chọn URL dựa trên môi trường
    private static final String BASE_URL = isEmulator() 
            ? "http://10.0.2.2:8080/"           // Máy ảo
            : "http://" + IP_MAY_TINH + ":8080/"; // Điện thoại thật
    
    private static Retrofit retrofit = null;
    
    /**
     * Phát hiện có phải máy ảo không
     */
    private static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
    
    /**
     * Lấy BASE_URL hiện tại (để debug)
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
}
