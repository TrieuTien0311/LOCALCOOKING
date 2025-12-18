package com.example.localcooking_v3t.api;

import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;
import com.example.localcooking_v3t.model.LoginRequest;
import com.example.localcooking_v3t.model.LoginResponse;
import com.example.localcooking_v3t.model.RegisterRequest;
import com.example.localcooking_v3t.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("nguoidung/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    
    @POST("nguoidung/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    
    @POST("nguoidung/change-password/send-otp")
    Call<ChangePasswordResponse> sendOtpForChangePassword(@Body ChangePasswordRequest request);
    
    @POST("nguoidung/change-password/verify")
    Call<ChangePasswordResponse> changePasswordWithOtp(@Body ChangePasswordWithOtpRequest request);
}
