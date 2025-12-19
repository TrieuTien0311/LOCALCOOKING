package com.android.be.controller;

import com.android.be.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quenmatkhau")
@RequiredArgsConstructor
public class QuenMatKhauController {

    private final PasswordResetService passwordResetService;

    // Bước 1: Gửi OTP quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không được để trống"));
        }

        try {
            passwordResetService.sendForgotPasswordOtp(email);
            return ResponseEntity.ok(Map.of("message", "OTP đã được gửi đến " + email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Bước 2: Xác thực OTP
    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin"));
        }

        try {
            String resetToken = passwordResetService.verifyOtpAndGetResetToken(email, otp);
            return ResponseEntity.ok(Map.of(
                    "resetToken", resetToken,
                    "message", "Xác thực thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Bước 3: Đặt mật khẩu mới
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String resetToken = request.get("resetToken");
        String newPassword = request.get("newPassword");

        if (resetToken == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin"));
        }

        if (newPassword.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu phải có ít nhất 6 ký tự"));
        }

        try {
            passwordResetService.resetPassword(resetToken, newPassword);
            return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
