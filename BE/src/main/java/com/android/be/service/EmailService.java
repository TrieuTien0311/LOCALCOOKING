package com.android.be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("localcooking23@gmail.com", "LOCAL COOKING");
        helper.setTo(toEmail);
        helper.setSubject("Mã xác thực tài khoản Local Cooking");

        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 50px 20px; background: linear-gradient(135deg, #FFF5EB 0%%, #FFE8D6 100%%; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
            
                <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 40px rgba(218, 139, 95, 0.15);">
            
                    <div style="background: linear-gradient(135deg, #E8805F 0%%, #DA8B5F 100%%); padding: 40px 30px; text-align: center;">
                        <div style="background-color: #ffffff; width: 80px; height: 80px; border-radius: 50%%; margin: 0 auto 15px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.15);">
                            <img src="cid:logo" alt="Logo" style="width: 100%%; height: 100%%; object-fit: cover; display: block;">
                        </div>
                        <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 600; letter-spacing: 0.5px;">LOCAL COOKING</h1>
                        <p style="color: rgba(255,255,255,0.9); margin: 8px 0 0 0; font-size: 14px;">Lớp học ẩm thực địa phương</p>
                    </div>
            
                    <div style="padding: 35px 30px;">
                        <div style="text-align: center; margin-bottom: 20px;">
                            <h2 style="margin: 0 0 10px 0; font-size: 24px; color: #2D2D2D; font-weight: 600;">Xác Thực Tài Khoản</h2>
                            <div style="width: 60px; height: 3px; background: linear-gradient(90deg, #E8805F, #DA8B5F); margin: 0 auto; border-radius: 2px;"></div>
                        </div>
            
                        <p style="font-size: 15px; line-height: 1.6; color: #555555; margin-bottom: 20px; text-align: center;">
                            Xin chào! 👋<br>
                            Chào mừng bạn đến với cộng đồng yêu ẩm thực địa phương.<br>
                            Sử dụng mã OTP bên dưới để hoàn tất xác thực.
                        </p>
            
                        <div style="background: linear-gradient(135deg, #FFF8F3 0%%, #FFF0E6 100%%); border: 2px solid #E8805F; border-radius: 16px; padding: 20px; text-align: center; margin: 20px 0;">
                            <p style="margin: 0 0 10px 0; font-size: 12px; color: #888888; text-transform: uppercase; letter-spacing: 1px; font-weight: 500;">MÃ XÁC THỰC</p>
                            <span style="font-size: 38px; font-weight: 700; color: #E8805F; letter-spacing: 10px; display: block;">%s</span>
                            <p style="margin: 10px 0 0 0; font-size: 12px; color: #999999;">Có hiệu lực trong 5 phút</p>
                        </div>
            
                        <div style="background-color: #FAFAFA; border-left: 4px solid #E8805F; border-radius: 8px; padding: 15px; margin-top: 20px;">
                            <p style="margin: 0 0 10px 0; font-size: 13px; color: #666666; line-height: 1.5;">
                                <strong style="color: #E8805F;">⏱️ Lưu ý quan trọng:</strong><br>
                                Mã OTP chỉ có hiệu lực trong <strong style="color: #E8805F;">5 phút</strong> kể từ khi nhận email này.
                            </p>
                            <p style="margin: 0; font-size: 13px; color: #666666; line-height: 1.5;">
                                <strong style="color: #E8805F;">🔒 Bảo mật:</strong><br>
                                Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email và kiểm tra bảo mật tài khoản.
                            </p>
                        </div>
            
                        <div style="text-align: center; margin-top: 20px;">
                            <p style="font-size: 13px; color: #888888; margin: 0;">
                                Cần hỗ trợ? Liên hệ với chúng tôi qua email<br>
                                <a href="mailto:support@localcooking.com" style="color: #E8805F; text-decoration: none; font-weight: 500;">support@localcooking.com</a>
                            </p>
                        </div>
                    </div>
            
                    <div style="background: linear-gradient(180deg, #FAFAFA 0%%, #F5F5F5 100%%); padding: 20px; text-align: center; border-top: 1px solid #EEEEEE;">
                        <div style="margin-bottom: 10px;">
                            <span style="font-size: 22px; margin: 0 6px;">📧</span>
                            <span style="font-size: 22px; margin: 0 6px;">🍜</span>
                            <span style="font-size: 22px; margin: 0 6px;">👨‍🍳</span>
                        </div>
                        <p style="margin: 0 0 5px 0; font-size: 12px; color: #666666; font-weight: 500;">
                            © 2024 Local Cooking - Lớp học ẩm thực địa phương
                        </p>
                        <p style="margin: 5px 0 0 0; font-size: 11px; color: #999999;">
                            Email tự động - Vui lòng không trả lời trực tiếp
                        </p>
                    </div>
            
                </div>
            
            </body>
            </html>
            """.formatted(otp);
        helper.setText(htmlContent, true);
        helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
        mailSender.send(message);
    }
}