package com.example.localcooking_v3t.api;

import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;
import com.example.localcooking_v3t.model.CheckSeatsResponse;
import com.example.localcooking_v3t.model.DanhMucMonAn;
import com.example.localcooking_v3t.model.DatLich;
import com.example.localcooking_v3t.model.DatLichRequest;
import com.example.localcooking_v3t.model.DatLichResponse;
import com.example.localcooking_v3t.model.ForgotPasswordRequest;
import com.example.localcooking_v3t.model.ForgotPasswordResponse;
import com.example.localcooking_v3t.model.GiaoVien;
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
import com.example.localcooking_v3t.model.ThongBaoDTO;
import com.example.localcooking_v3t.model.UnreadCountResponse;
import com.example.localcooking_v3t.model.MessageResponse;
import com.example.localcooking_v3t.model.UuDaiDTO;
import com.example.localcooking_v3t.model.ApDungUuDaiRequest;
import com.example.localcooking_v3t.model.ApDungUuDaiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    // Lấy lịch trình với thông tin đầy đủ (có số chỗ trống)
    @GET("api/lichtrinh/{id}/detail")
    Call<LichTrinhLopHoc> getLichTrinhDetailById(
            @retrofit2.http.Path("id") Integer id,
            @retrofit2.http.Query("ngayThamGia") String ngayThamGia
    );

    // Lấy lịch trình theo khóa học
    @GET("api/lichtrinh/khoahoc/{maKhoaHoc}")
    Call<List<LichTrinhLopHoc>> getLichTrinhByKhoaHoc(@retrofit2.http.Path("maKhoaHoc") Integer maKhoaHoc);

    // Tìm lịch trình theo địa điểm
    @GET("api/lichtrinh/search")
    Call<List<LichTrinhLopHoc>> searchLichTrinhByDiaDiem(@retrofit2.http.Query("diaDiem") String diaDiem);

    // Lấy thông tin giáo viên theo ID
    @GET("api/giaovien/{id}")
    Call<GiaoVien> getGiaoVienById(@retrofit2.http.Path("id") Integer id);

    // ========== API THÔNG BÁO ==========

    // Lấy tất cả thông báo của người dùng
    @GET("api/thongbao/user/{maNguoiNhan}")
    Call<List<ThongBaoDTO>> getThongBaoByUser(@Path("maNguoiNhan") Integer maNguoiNhan);

    // Lấy thông báo chưa đọc
    @GET("api/thongbao/user/{maNguoiNhan}/unread")
    Call<List<ThongBaoDTO>> getUnreadThongBao(@Path("maNguoiNhan") Integer maNguoiNhan);

    // Đếm số thông báo chưa đọc
    @GET("api/thongbao/user/{maNguoiNhan}/unread-count")
    Call<UnreadCountResponse> getUnreadCount(@Path("maNguoiNhan") Integer maNguoiNhan);

    // Lấy thông báo theo loại
    @GET("api/thongbao/user/{maNguoiNhan}/type/{loaiThongBao}")
    Call<List<ThongBaoDTO>> getThongBaoByType(
            @Path("maNguoiNhan") Integer maNguoiNhan,
            @Path("loaiThongBao") String loaiThongBao
    );

    // Đánh dấu đã đọc
    @PUT("api/thongbao/{id}/mark-read")
    Call<ThongBaoDTO> markAsRead(@Path("id") Integer id);

    // Đánh dấu tất cả đã đọc
    @PUT("api/thongbao/user/{maNguoiNhan}/mark-all-read")
    Call<MessageResponse> markAllAsRead(@Path("maNguoiNhan") Integer maNguoiNhan);

    // Xóa thông báo
    @DELETE("api/thongbao/{id}")
    Call<Void> deleteThongBao(@Path("id") Integer id);

    // Xóa tất cả thông báo đã đọc
    @DELETE("api/thongbao/user/{maNguoiNhan}/delete-read")
    Call<MessageResponse> deleteAllReadNotifications(@Path("maNguoiNhan") Integer maNguoiNhan);


    // ========== API YÊU THÍCH ==========

    // Lấy danh sách khóa học yêu thích của học viên (trả về KhoaHoc đầy đủ)
    @GET("api/yeuthich/hocvien/{maHocVien}/khoahoc")
    Call<List<KhoaHoc>> getFavoritesByHocVien(@Path("maHocVien") Integer maHocVien);

    // Kiểm tra khóa học đã được yêu thích chưa
    @GET("api/yeuthich/check")
    Call<java.util.Map<String, Boolean>> checkFavorite(
            @Query("maHocVien") Integer maHocVien,
            @Query("maKhoaHoc") Integer maKhoaHoc
    );

    // Toggle yêu thích (thêm/xóa)
    @POST("api/yeuthich/toggle")
    Call<java.util.Map<String, Object>> toggleFavorite(@Body java.util.Map<String, Integer> request);

    // ========== API ĐẶT LỊCH ==========

    // Kiểm tra chỗ trống
    @GET("api/lichtrinh/check-seats")
    Call<CheckSeatsResponse> checkAvailableSeats(
            @Query("maLichTrinh") Integer maLichTrinh,
            @Query("ngayThamGia") String ngayThamGia
    );

    // Tạo đặt lịch mới
    @POST("api/datlich")
    Call<DatLichResponse> createDatLich(@Body DatLichRequest request);

    // Lấy đặt lịch theo học viên
    @GET("api/datlich/hocvien/{maHocVien}")
    Call<List<DatLich>> getDatLichByHocVien(@Path("maHocVien") Integer maHocVien);

    // Lấy đặt lịch theo học viên và trạng thái
    @GET("api/datlich/hocvien/{maHocVien}/trangthai/{trangThai}")
    Call<List<DatLich>> getDatLichByHocVienAndTrangThai(
            @Path("maHocVien") Integer maHocVien,
            @Path("trangThai") String trangThai
    );
    // ========== API ƯU ĐÃI ==========

    // Lấy tất cả ưu đãi
    @GET("api/uudai")
    Call<List<UuDaiDTO>> getAllUuDai();

    // Lấy ưu đãi theo ID
    @GET("api/uudai/{id}")
    Call<UuDaiDTO> getUuDaiById(@Path("id") Integer id);

    // Lấy danh sách ưu đãi khả dụng cho user
    @GET("api/uudai/available")
    Call<List<UuDaiDTO>> getAvailableUuDai(
            @Query("maHocVien") Integer maHocVien,
            @Query("soLuongNguoi") Integer soLuongNguoi
    );

    // Áp dụng mã ưu đãi
    @POST("api/uudai/apply")
    Call<ApDungUuDaiResponse> apDungUuDai(@Body ApDungUuDaiRequest request);

    // Xác nhận sử dụng mã ưu đãi (sau khi thanh toán thành công)
    @POST("api/uudai/confirm/{maUuDai}")
    Call<Void> confirmUuDai(@Path("maUuDai") Integer maUuDai);


}