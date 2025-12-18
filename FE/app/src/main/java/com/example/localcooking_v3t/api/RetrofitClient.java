package com.example.localcooking_v3t.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    
    // ===== CẤU HÌNH IP =====
    // QUAN TRỌNG: Đổi IP này tùy theo môi trường
    // - Nếu dùng Emulator: "http://10.0.2.2:8080/api/"
    // - Nếu dùng điện thoại thật: "http://192.168.137.1:8080/api/"
    
    // ĐANG DÙNG EMULATOR (MÁY ẢO)123456
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    
    private static Retrofit retrofit = null;
    

    
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Thêm logging interceptor để debug
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Thêm interceptor để set headers
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
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
