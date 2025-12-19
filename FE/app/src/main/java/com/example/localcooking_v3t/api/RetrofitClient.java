package com.example.localcooking_v3t.api;

import android.os.Build;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    
    // ===== CẤU HÌNH IP =====
<<<<<<< HEAD
    // HƯỚNG DẪN TÌM IP:
    // 1. Mở CMD → gõ: ipconfig
    // 2. Tìm "IPv4 Address" của adapter đang dùng
    // 3. Cập nhật IP_MAY_TINH bên dưới
    
    // IP máy tính (cập nhật theo ipconfig của bạn)
    private static final String IP_MAY_TINH = "10.0.2.2"; // Hotspot
    // private static final String IP_MAY_TINH = "192.168.1.x"; // WiFi thường
    
    // Tự động chọn URL dựa trên môi trường
    private static final String BASE_URL = isEmulator() 
            ? "http://10.0.2.2:8080/"           // Máy ảo Android
=======
    // IP máy tính khi dùng Hotspot: 192.168.137.1
    // (Từ ipconfig: Wireless LAN adapter Local Area Connection* 10)
    private static final String IP_MAY_TINH = "10.0.2.2";
    
    // Tự động chọn URL dựa trên môi trường
    private static final String BASE_URL = isEmulator() 
            ? "http://1:8080/"           // Máy ảo
>>>>>>> f65c26518465b72341a9c57626f4faebbd31dc22
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
            // Thêm logging interceptor để debug
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
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