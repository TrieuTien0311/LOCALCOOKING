package com.android.be.service;

import com.android.be.dto.*;
import com.android.be.model.NguoiDung;
import com.android.be.repository.NguoiDungRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final GoogleAuthService googleAuthService;
    
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }
    
    public Optional<NguoiDung> getNguoiDungById(Integer id) {
        return nguoiDungRepository.findById(id);
    }
    
    public NguoiDung createNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public NguoiDung updateNguoiDung(Integer id, NguoiDung nguoiDung) {
        nguoiDung.setMaNguoiDung(id);
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public void deleteNguoiDung(Integer id) {
        nguoiDungRepository.deleteById(id);
    }
    
    public LoginResponse login(LoginRequest request) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByEmailAndMatKhau(
            request.getEmail(), 
            request.getMatKhau()
        );
        
        if (nguoiDungOpt.isPresent()) {
            NguoiDung nguoiDung = nguoiDungOpt.get();
            
            // Kiểm tra trạng thái tài khoản
            if (!"HoatDong".equals(nguoiDung.getTrangThai())) {
                return new LoginResponse(
                    false, 
                    "Tài khoản đã bị khóa", 
                    null, null, null, null, null
                );
            }
            
            return new LoginResponse(
                true,
                "Đăng nhập thành công",
                nguoiDung.getMaNguoiDung(),
                nguoiDung.getTenDangNhap(),
                nguoiDung.getHoTen(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro()
            );
        }
        
        return new LoginResponse(
            false, 
            "Email hoặc mật khẩu không đúng", 
            null, null, null, null, null
        );
    }
    
    public RegisterResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại
        if (nguoiDungRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterResponse(
                false,
                "Email đã được sử dụng",
                null, null, null, null, null
            );
        }
        
        // Kiểm tra tên đăng nhập đã tồn tại
        if (nguoiDungRepository.findByTenDangNhap(request.getTenDangNhap()).isPresent()) {
            return new RegisterResponse(
                false,
                "Tên đăng nhập đã được sử dụng",
                null, null, null, null, null
            );
        }
        
        // Tạo người dùng mới
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(request.getTenDangNhap());
        nguoiDung.setMatKhau(request.getMatKhau());
        nguoiDung.setHoTen(request.getHoTen());
        nguoiDung.setEmail(request.getEmail());
        nguoiDung.setSoDienThoai(request.getSoDienThoai());
        nguoiDung.setVaiTro("HocVien"); // Mặc định là học viên
        nguoiDung.setTrangThai("HoatDong");
        nguoiDung.setNgayTao(LocalDateTime.now());
        nguoiDung.setLanCapNhatCuoi(LocalDateTime.now());
        
        // Lưu vào database
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        
        return new RegisterResponse(
            true,
            "Đăng ký thành công",
            savedNguoiDung.getMaNguoiDung(),
            savedNguoiDung.getTenDangNhap(),
            savedNguoiDung.getHoTen(),
            savedNguoiDung.getEmail(),
            savedNguoiDung.getVaiTro()
        );
    }
    
    // Gửi OTP để đổi mật khẩu
    public Map<String, Object> sendOtpForChangePassword(ChangePasswordRequest request) throws Exception {
        Map<String, Object> response = new HashMap<>();
        
        // Validate input
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new Exception("Email không được để trống");
        }
        
        if (request.getMatKhauHienTai() == null || request.getMatKhauHienTai().isBlank()) {
            throw new Exception("Mật khẩu hiện tại không được để trống");
        }
        
        if (request.getMatKhauMoi() == null || request.getMatKhauMoi().isBlank()) {
            throw new Exception("Mật khẩu mới không được để trống");
        }
        
        if (request.getXacNhanMatKhauMoi() == null || request.getXacNhanMatKhauMoi().isBlank()) {
            throw new Exception("Xác nhận mật khẩu mới không được để trống");
        }
        
        // Kiểm tra mật khẩu mới và xác nhận khớp nhau
        if (!request.getMatKhauMoi().equals(request.getXacNhanMatKhauMoi())) {
            throw new Exception("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        
        // Kiểm tra mật khẩu mới khác mật khẩu cũ
        if (request.getMatKhauHienTai().equals(request.getMatKhauMoi())) {
            throw new Exception("Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        
        // Kiểm tra email tồn tại
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByEmail(request.getEmail());
        if (nguoiDungOpt.isEmpty()) {
            throw new Exception("Email không tồn tại trong hệ thống");
        }
        
        NguoiDung nguoiDung = nguoiDungOpt.get();
        
        // Kiểm tra mật khẩu hiện tại đúng không
        if (!nguoiDung.getMatKhau().equals(request.getMatKhauHienTai())) {
            throw new Exception("Mật khẩu hiện tại không đúng");
        }
        
        // Kiểm tra trạng thái tài khoản
        if (!"HoatDong".equals(nguoiDung.getTrangThai())) {
            throw new Exception("Tài khoản đã bị khóa");
        }
        
        // Tạo và gửi OTP
        try {
            String otp = otpService.generateOtp(request.getEmail());
            emailService.sendOtpEmail(request.getEmail(), otp);
            
            response.put("success", true);
            response.put("message", "Mã OTP đã được gửi đến email " + request.getEmail());
            response.put("email", request.getEmail());
        } catch (MessagingException e) {
            throw new Exception("Lỗi gửi email: " + e.getMessage());
        }
        
        return response;
    }
    
    // Đổi mật khẩu với OTP
    public Map<String, Object> changePasswordWithOtp(ChangePasswordWithOtpRequest request) throws Exception {
        Map<String, Object> response = new HashMap<>();
        
        // Validate input
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new Exception("Email không được để trống");
        }
        
        if (request.getOtp() == null || request.getOtp().isBlank()) {
            throw new Exception("Mã OTP không được để trống");
        }
        
        if (request.getMatKhauMoi() == null || request.getMatKhauMoi().isBlank()) {
            throw new Exception("Mật khẩu mới không được để trống");
        }
        
        // Xác thực OTP
        boolean otpValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!otpValid) {
            throw new Exception("Mã OTP không hợp lệ hoặc đã hết hạn");
        }
        
        // Tìm người dùng
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByEmail(request.getEmail());
        if (nguoiDungOpt.isEmpty()) {
            throw new Exception("Email không tồn tại trong hệ thống");
        }
        
        NguoiDung nguoiDung = nguoiDungOpt.get();
        
        // Cập nhật mật khẩu mới
        nguoiDung.setMatKhau(request.getMatKhauMoi());
        nguoiDung.setLanCapNhatCuoi(LocalDateTime.now());
        nguoiDungRepository.save(nguoiDung);
        
        response.put("success", true);
        response.put("message", "Đổi mật khẩu thành công");
        
        return response;
    }
    
    // Đăng nhập bằng Google
    public GoogleLoginResponse loginWithGoogle(String idToken) {
        try {
            // Verify token với Google
            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = 
                googleAuthService.verifyToken(idToken);
            
            if (payload == null) {
                return new GoogleLoginResponse(
                    false, "Token không hợp lệ", 
                    null, null, null, null, null, null, false
                );
            }
            
            String email = payload.getEmail();
            String googleId = payload.getSubject();
            String name = (String) payload.get("name");
            String avatarUrl = (String) payload.get("picture");
            
            // Tìm user theo email
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByEmail(email);
            
            boolean isNewUser = false;
            NguoiDung nguoiDung;
            
            if (nguoiDungOpt.isPresent()) {
                // User đã tồn tại
                nguoiDung = nguoiDungOpt.get();
                
                // Kiểm tra trạng thái
                if (!"HoatDong".equals(nguoiDung.getTrangThai())) {
                    return new GoogleLoginResponse(
                        false, "Tài khoản đã bị khóa",
                        null, null, null, null, null, null, false
                    );
                }
                
                // Cập nhật thông tin Google nếu chưa có
                if (nguoiDung.getGoogleId() == null) {
                    nguoiDung.setGoogleId(googleId);
                    nguoiDung.setLoginMethod("GOOGLE");
                }
                if (nguoiDung.getAvatarUrl() == null) {
                    nguoiDung.setAvatarUrl(avatarUrl);
                }
                nguoiDung.setLanCapNhatCuoi(LocalDateTime.now());
                nguoiDungRepository.save(nguoiDung);
                
            } else {
                // Tạo user mới
                isNewUser = true;
                nguoiDung = new NguoiDung();
                nguoiDung.setEmail(email);
                nguoiDung.setGoogleId(googleId);
                nguoiDung.setHoTen(name);
                nguoiDung.setTenDangNhap(email.split("@")[0]); // Lấy phần trước @ làm username
                nguoiDung.setAvatarUrl(avatarUrl);
                nguoiDung.setLoginMethod("GOOGLE");
                nguoiDung.setVaiTro("HocVien");
                nguoiDung.setTrangThai("HoatDong");
                nguoiDung.setNgayTao(LocalDateTime.now());
                nguoiDung.setLanCapNhatCuoi(LocalDateTime.now());
                nguoiDungRepository.save(nguoiDung);
            }
            
            return new GoogleLoginResponse(
                true,
                "Đăng nhập thành công",
                nguoiDung.getMaNguoiDung(),
                nguoiDung.getTenDangNhap(),
                nguoiDung.getHoTen(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro(),
                nguoiDung.getAvatarUrl(),
                isNewUser
            );
            
        } catch (Exception e) {
            e.printStackTrace();
            return new GoogleLoginResponse(
                false, "Lỗi xác thực: " + e.getMessage(),
                null, null, null, null, null, null, false
            );
        }
    }
}
