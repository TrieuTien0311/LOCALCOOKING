package com.android.be.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    
    // Lưu OTP tạm: key = email/phone, value = OTP
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    // Tạo OTP 6 số
    public String generateOtp(String key) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(key, new OtpData(otp, System.currentTimeMillis()));
        return otp;
    }
    
    // Xác thực OTP
    public boolean verifyOtp(String key, String otp) {
        OtpData data = otpStorage.get(key);
        if (data == null) return false;
        
        // Check hết hạn
        long elapsed = (System.currentTimeMillis() - data.createdAt) / 60000;
        if (elapsed > OTP_EXPIRY_MINUTES) {
            otpStorage.remove(key);
            return false;
        }
        
        boolean valid = data.otp.equals(otp);
        if (valid) otpStorage.remove(key);
        return valid;
    }
    
    private record OtpData(String otp, long createdAt) {}
}
