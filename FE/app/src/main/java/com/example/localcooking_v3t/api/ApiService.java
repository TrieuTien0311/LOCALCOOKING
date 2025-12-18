package com.example.localcooking_v3t.api;

import com.example.localcooking_v3t.model.LoginRequest;
import com.example.localcooking_v3t.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/nguoidung/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
