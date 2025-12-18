package com.android.be.controller;

import com.android.be.service.EmailService;
import com.android.be.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    
    private final OtpService otpService;
    private final EmailService emailService;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không được để trống"));
        }
        
        try {
            String otp = otpService.generateOtp(email);
            emailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok(Map.of("message", "OTP đã được gửi đến " + email));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Lỗi gửi email: " + e.getMessage()));
        }
    }
    
    // Xác thực OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String key = request.get("email");
        String otp = request.get("otp");
        
        if (key == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin"));
        }
        
        boolean valid = otpService.verifyOtp(key, otp);
        return ResponseEntity.ok(Map.of(
            "valid", valid,
            "message", valid ? "Xác thực thành công" : "OTP không hợp lệ hoặc đã hết hạn"
        ));
    }
}
