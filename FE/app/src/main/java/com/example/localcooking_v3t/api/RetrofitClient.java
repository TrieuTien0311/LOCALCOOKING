package com.example.localcooking_v3t.api;

import android.os.Build;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ===== CẤU HÌNH IP =====
    // IP máy tính khi dùng Mobile Hotspot: 192.168.137.1
    // (Từ ipconfig: Wireless LAN adapter Local Area Connection* X)
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
        boolean result = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu");
        
        // Debug log
        android.util.Log.d("RetrofitClient", "=== EMULATOR DETECTION ===");
        android.util.Log.d("RetrofitClient", "FINGERPRINT: " + Build.FINGERPRINT);
        android.util.Log.d("RetrofitClient", "MODEL: " + Build.MODEL);
        android.util.Log.d("RetrofitClient", "MANUFACTURER: " + Build.MANUFACTURER);
        android.util.Log.d("RetrofitClient", "BRAND: " + Build.BRAND);
        android.util.Log.d("RetrofitClient", "DEVICE: " + Build.DEVICE);
        android.util.Log.d("RetrofitClient", "PRODUCT: " + Build.PRODUCT);
        android.util.Log.d("RetrofitClient", "HARDWARE: " + Build.HARDWARE);
        android.util.Log.d("RetrofitClient", "Is Emulator: " + result);
        android.util.Log.d("RetrofitClient", "Using URL: " + (result ? "http://10.0.2.2:8080/" : "http://" + IP_MAY_TINH + ":8080/"));
        
        return result;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Thêm logging interceptor để debug
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
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
    
    /**
     * Lấy URL đầy đủ cho hình ảnh
     */
    public static String getFullImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        // Nếu đã là URL đầy đủ thì trả về luôn
        if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            return imagePath;
        }
        // Nếu bắt đầu bằng /uploads/ (ảnh đánh giá) thì ghép trực tiếp
        if (imagePath.startsWith("/uploads/")) {
            return BASE_URL.substring(0, BASE_URL.length() - 1) + imagePath;
        }
        // Nếu là đường dẫn tương đối thì ghép với BASE_URL + images/
        return BASE_URL + "images/" + imagePath;
    }
}