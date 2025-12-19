package com.example.localcooking_v3t.api;

import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;
import com.example.localcooking_v3t.model.DanhMucMonAn;
import com.example.localcooking_v3t.model.ForgotPasswordRequest;
import com.example.localcooking_v3t.model.ForgotPasswordResponse;
<<<<<<< HEAD
import com.example.localcooking_v3t.model.GoogleLoginRequest;
import com.example.localcooking_v3t.model.GoogleLoginResponse;
=======
import com.example.localcooking_v3t.model.HinhAnhMonAn;
>>>>>>> 25ddb0f808b13e6cc28d9cf10d8dad8f6fb26466
import com.example.localcooking_v3t.model.LoginRequest;
import com.example.localcooking_v3t.model.LoginResponse;
import com.example.localcooking_v3t.model.LopHoc;
import com.example.localcooking_v3t.model.ProfileResponse;
import com.example.localcooking_v3t.model.RegisterRequest;
import com.example.localcooking_v3t.model.RegisterResponse;
import com.example.localcooking_v3t.model.ResetPasswordRequest;
import com.example.localcooking_v3t.model.ResetPasswordResponse;
import com.example.localcooking_v3t.model.UpdateProfileRequest;
import com.example.localcooking_v3t.model.UpdateProfileResponse;
import com.example.localcooking_v3t.model.VerifyOtpRequest;
import com.example.localcooking_v3t.model.VerifyOtpResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/nguoidung/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/nguoidung/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // Quên mật khẩu - Bước 1: Gửi OTP
    @POST("api/quenmatkhau/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    // Quên mật khẩu - Bước 2: Xác thực OTP
    @POST("api/quenmatkhau/verify-reset-otp")
    Call<VerifyOtpResponse> verifyResetOtp(@Body VerifyOtpRequest request);

    // Quên mật khẩu - Bước 3: Đặt mật khẩu mới
    @POST("api/quenmatkhau/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);


    @GET("api/lophoc")
    Call<List<LopHoc>> getAllLopHoc();

    // Đổi mật khẩu - Bước 1: Gửi OTP
    @POST("api/nguoidung/change-password/send-otp")
    Call<ChangePasswordResponse> sendOtpForChangePassword(@Body ChangePasswordRequest request);

    // Đổi mật khẩu - Bước 2: Xác thực OTP và đổi mật khẩu
    @POST("api/nguoidung/change-password/verify")
    Call<ChangePasswordResponse> changePasswordWithOtp(@Body ChangePasswordWithOtpRequest request);

<<<<<<< HEAD
    // Đăng nhập bằng Google
    @POST("api/nguoidung/google-login")
    Call<GoogleLoginResponse> googleLogin(@Body GoogleLoginRequest request);
=======
    // Cập nhật thông tin cá nhân
    @POST("api/nguoidung/update-profile")
    Call<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    // Lấy thông tin profile
    @GET("api/nguoidung/profile/{id}")
    Call<ProfileResponse> getProfile(@retrofit2.http.Path("id") Integer id);
    @GET("api/danhmucmonan/lophoc/{maLopHoc}")
    Call<List<DanhMucMonAn>> getDanhMucMonAnByLopHoc(@retrofit2.http.Path("maLopHoc") Integer maLopHoc);

    @GET("api/lophoc/search")
    Call<List<LopHoc>> searchLopHocByDiaDiem(
            @retrofit2.http.Query("diaDiem") String diaDiem,
            @retrofit2.http.Query("ngayTimKiem") String ngayTimKiem
    );

    @GET("api/hinhanh-monan/monan/{maMonAn}")
    Call<List<HinhAnhMonAn>> getHinhAnhMonAn(@retrofit2.http.Path("maMonAn") Integer maMonAn);

>>>>>>> 25ddb0f808b13e6cc28d9cf10d8dad8f6fb26466
}