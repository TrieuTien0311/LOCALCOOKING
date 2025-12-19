package com.android.be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

       helper.setFrom("nguyenthu2018dn@gmail.com", "LOCAL COOKING");
helper.setTo(toEmail);
helper.setSubject("Mã xác thực tài khoản Local Cooking"); 

String htmlContent = """
    <!DOCTYPE html>
    <html>
    <head>
        <style>
            body { margin: 0; padding: 0; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #f4f4f4; }
        </style>
    </head>
    <body style="margin: 0; padding: 40px 0; background-color: #f4f4f4;">
        
        <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);">
            
            <div style="background-color: #ff6b35; padding: 30px 0; text-align: center;">
                <h1 style="color: #ffffff; margin: 0; font-size: 24px; font-weight: 600; letter-spacing: 1px;">LOCAL COOKING</h1>
            </div>
            
            <div style="padding: 40px 30px; color: #333333;">
                <h2 style="margin-top: 0; font-size: 20px; color: #333333;">Xác thực tài khoản</h2>
                
                <p style="font-size: 16px; line-height: 1.6; color: #555555; margin-bottom: 25px;">
                    Xin chào,<br>
                    Cảm ơn bạn đã tham gia cộng đồng yêu ẩm thực. Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình xác thực:
                </p>
                
                <div style="background-color: #fff5f0; border: 1px dashed #ff6b35; border-radius: 6px; padding: 20px; text-align: center; margin: 30px 0;">
                    <span style="font-size: 36px; font-weight: bold; color: #ff6b35; letter-spacing: 8px; display: block;">%s</span>
                </div>
                
                <p style="font-size: 14px; color: #888888; margin-top: 20px;">
                    * Mã xác thực có hiệu lực trong vòng <strong>5 phút</strong>.
                </p>
                <p style="font-size: 14px; color: #888888;">
                    Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email và bảo mật tài khoản của mình.
                </p>
            </div>
            
            <div style="background-color: #f9f9f9; padding: 20px; text-align: center; border-top: 1px solid #eeeeee;">
                <p style="margin: 0; font-size: 12px; color: #999999;">
                    © 2024 Local Food - Học Nấu Ăn Online<br>
                    Đây là email tự động, vui lòng không trả lời.
                </p>
            </div>
            
        </div>
    </body>
    </html>
    """.formatted(otp);

helper.setText(htmlContent, true);
mailSender.send(message);
    }
}
