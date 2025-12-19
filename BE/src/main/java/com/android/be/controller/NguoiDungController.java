package com.android.be.controller;

import com.android.be.dto.*;
import com.android.be.model.NguoiDung;
import com.android.be.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoidung")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NguoiDungController {
    
    private final NguoiDungService nguoiDungService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = nguoiDungService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = nguoiDungService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<NguoiDung>> getAllNguoiDung() {
        return ResponseEntity.ok(nguoiDungService.getAllNguoiDung());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getNguoiDungById(@PathVariable Integer id) {
        return nguoiDungService.getNguoiDungById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<NguoiDung> createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nguoiDungService.createNguoiDung(nguoiDung));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateNguoiDung(@PathVariable Integer id, @RequestBody NguoiDung nguoiDung) {
        return ResponseEntity.ok(nguoiDungService.updateNguoiDung(id, nguoiDung));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNguoiDung(@PathVariable Integer id) {
        nguoiDungService.deleteNguoiDung(id);
        return ResponseEntity.noContent().build();
    }
    
    // API gửi OTP để đổi mật khẩu
    @PostMapping("/change-password/send-otp")
    public ResponseEntity<?> sendOtpForChangePassword(@RequestBody ChangePasswordRequest request) {
        try {
            Map<String, Object> response = nguoiDungService.sendOtpForChangePassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // API đổi mật khẩu với OTP
    @PostMapping("/change-password/verify")
    public ResponseEntity<?> changePasswordWithOtp(@RequestBody ChangePasswordWithOtpRequest request) {
        try {
            Map<String, Object> response = nguoiDungService.changePasswordWithOtp(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
<<<<<<< HEAD
    
    // API đăng nhập bằng Google
    @PostMapping("/google-login")
    public ResponseEntity<GoogleLoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        GoogleLoginResponse response = nguoiDungService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(response);
=======

    // API cập nhật thông tin cá nhân
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            UpdateProfileResponse response = nguoiDungService.updateProfile(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // API lấy thông tin profile theo maNguoiDung
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Integer id) {
        try {
            return nguoiDungService.getNguoiDungById(id)
                    .map(user -> ResponseEntity.ok(Map.of(
                            "success", true,
                            "maNguoiDung", user.getMaNguoiDung(),
                            "tenDangNhap", user.getTenDangNhap(),
                            "hoTen", user.getHoTen() != null ? user.getHoTen() : "",
                            "email", user.getEmail(),
                            "soDienThoai", user.getSoDienThoai() != null ? user.getSoDienThoai() : "",
                            "diaChi", user.getDiaChi() != null ? user.getDiaChi() : ""
                    )))
                    .orElse(ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Không tìm thấy người dùng"
                    )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
>>>>>>> 25ddb0f808b13e6cc28d9cf10d8dad8f6fb26466
    }
}
