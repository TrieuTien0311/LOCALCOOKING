package com.example.localcooking_v3t.api;

import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;
import com.example.localcooking_v3t.model.DanhMucMonAn;
import com.example.localcooking_v3t.model.ForgotPasswordRequest;
import com.example.localcooking_v3t.model.ForgotPasswordResponse;
import com.example.localcooking_v3t.model.GoogleLoginRequest;
import com.example.localcooking_v3t.model.GoogleLoginResponse;
import com.example.localcooking_v3t.model.HinhAnhKhoaHoc;
import com.example.localcooking_v3t.model.HinhAnhMonAn;
import com.example.localcooking_v3t.model.LichTrinhLopHoc;
import com.example.localcooking_v3t.model.LoginRequest;
import com.example.localcooking_v3t.model.LoginResponse;
import com.example.localcooking_v3t.model.KhoaHoc;
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



    // Đổi mật khẩu - Bước 1: Gửi OTP
    @POST("api/nguoidung/change-password/send-otp")
    Call<ChangePasswordResponse> sendOtpForChangePassword(@Body ChangePasswordRequest request);

    // Đổi mật khẩu - Bước 2: Xác thực OTP và đổi mật khẩu
    @POST("api/nguoidung/change-password/verify")
    Call<ChangePasswordResponse> changePasswordWithOtp(@Body ChangePasswordWithOtpRequest request);

    // Đăng nhập bằng Google
    @POST("api/nguoidung/google-login")
    Call<GoogleLoginResponse> googleLogin(@Body GoogleLoginRequest request);
    // Cập nhật thông tin cá nhân
    @POST("api/nguoidung/update-profile")
    Call<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    // Lấy thông tin profile
    @GET("api/nguoidung/profile/{id}")
    Call<ProfileResponse> getProfile(@retrofit2.http.Path("id") Integer id);

    // Lấy tất cả khóa học
    @GET("api/khoahoc")
    Call<List<KhoaHoc>> getAllKhoaHoc();
    @GET("api/danhmucmonan/khoahoc/{maKhoaHoc}")
    Call<List<DanhMucMonAn>> getDanhMucMonAnByKhoaHoc(@retrofit2.http.Path("maKhoaHoc") Integer maKhoaHoc);
    // Lấy khóa học theo ID
    @GET("api/khoahoc/{id}")
    Call<KhoaHoc> getKhoaHocById(@retrofit2.http.Path("id") Integer id);

    @GET("api/khoahoc/search")
    Call<List<KhoaHoc>> searchKhoaHoc(
            @retrofit2.http.Query("diaDiem") String diaDiem,
            @retrofit2.http.Query("ngayTimKiem") String ngayTimKiem
    );
    // Lấy hình ảnh khóa học
    @GET("api/hinhanh-khoahoc/khoahoc/{maKhoaHoc}")
    Call<List<HinhAnhKhoaHoc>> getHinhAnhKhoaHoc(@retrofit2.http.Path("maKhoaHoc") Integer maKhoaHoc);

    @GET("api/hinhanh-monan/monan/{maMonAn}")
    Call<List<HinhAnhMonAn>> getHinhAnhMonAn(@retrofit2.http.Path("maMonAn") Integer maMonAn);
    @GET("api/lichtrinh")
    Call<List<LichTrinhLopHoc>> getAllLichTrinh();
    @GET("api/lichtrinh/{id}")
    Call<LichTrinhLopHoc> getLichTrinhById(@retrofit2.http.Path("id") Integer id);
    // Lấy lịch trình theo khóa học
    @GET("api/lichtrinh/khoahoc/{maKhoaHoc}")
    Call<List<LichTrinhLopHoc>> getLichTrinhByKhoaHoc(@retrofit2.http.Path("maKhoaHoc") Integer maKhoaHoc);

    // Tìm lịch trình theo địa điểm
    @GET("api/lichtrinh/search")
    Call<List<LichTrinhLopHoc>> searchLichTrinhByDiaDiem(@retrofit2.http.Query("diaDiem") String diaDiem);


}