package com.android.be.service;

import com.android.be.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final NguoiDungRepository nguoiDungRepository;
    private final OtpService otpService;
    private final EmailService emailService;

    // Lưu reset token: key = token, value = email + thời gian tạo
    private final Map<String, ResetTokenData> resetTokens = new ConcurrentHashMap<>();
    private static final int TOKEN_EXPIRY_MINUTES = 10;

    // Bước 1: Gửi OTP quên mật khẩu
    public void sendForgotPasswordOtp(String email) throws Exception {
        // Check email tồn tại
        nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
    }

    // Bước 2: Verify OTP và trả về reset token
    public String verifyOtpAndGetResetToken(String email, String otp) {
        if (!otpService.verifyOtp(email, otp)) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }

        // Tạo reset token
        String resetToken = UUID.randomUUID().toString();
        resetTokens.put(resetToken, new ResetTokenData(email, System.currentTimeMillis()));
        return resetToken;
    }

    // Bước 3: Đặt mật khẩu mới
    public void resetPassword(String resetToken, String newPassword) {
        ResetTokenData data = resetTokens.get(resetToken);
        if (data == null) {
            throw new RuntimeException("Token không hợp lệ");
        }

        // Check hết hạn
        long elapsed = (System.currentTimeMillis() - data.createdAt) / 60000;
        if (elapsed > TOKEN_EXPIRY_MINUTES) {
            resetTokens.remove(resetToken);
            throw new RuntimeException("Token đã hết hạn");
        }

        // Update password
        nguoiDungRepository.findByEmail(data.email).ifPresent(user -> {
            user.setMatKhau(newPassword); // TODO: Hash password nếu cần
            nguoiDungRepository.save(user);
        });

        resetTokens.remove(resetToken);
    }

    private record ResetTokenData(String email, long createdAt) {}
}
