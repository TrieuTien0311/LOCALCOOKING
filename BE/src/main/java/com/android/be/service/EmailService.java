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
                   <meta charset="UTF-8">
                   <meta name="viewport" content="width=device-width, initial-scale=1.0">
                   <style>
                       body {\s
                           margin: 0;\s
                           padding: 0;\s
                           font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;\s
                           background: linear-gradient(135deg, #FFF5EB 0%, #FFE8D6 100%);
                       }
                   </style>
               </head>
               <body style="margin: 0; padding: 50px 20px; background: linear-gradient(135deg, #FFF5EB 0%, #FFE8D6 100%);">
        
                   <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 40px rgba(218, 139, 95, 0.15);">
        
                       <!-- Header với icon -->
                       <div style="background: linear-gradient(135deg, #E8805F 0%, #DA8B5F 100%); padding: 45px 30px; text-align: center; position: relative;">
                           <!-- Icon nấu ăn -->
                           <div style="background-color: #ffffff; width: 70px; height: 70px; border-radius: 50%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">
                               <span style="font-size: 36px;">🍳</span>
                           </div>
                           <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 600; letter-spacing: 0.5px;">LOCAL COOKING</h1>
                           <p style="color: rgba(255,255,255,0.9); margin: 8px 0 0 0; font-size: 14px; font-weight: 400;">Lớp học ẩm thực địa phương</p>
                       </div>
        
                       <!-- Nội dung chính -->
                       <div style="padding: 45px 35px;">
                           <div style="text-align: center; margin-bottom: 30px;">
                               <h2 style="margin: 0 0 10px 0; font-size: 24px; color: #2D2D2D; font-weight: 600;">Xác Thực Tài Khoản</h2>
                               <div style="width: 60px; height: 3px; background: linear-gradient(90deg, #E8805F, #DA8B5F); margin: 0 auto; border-radius: 2px;"></div>
                           </div>
        
                           <p style="font-size: 15px; line-height: 1.7; color: #555555; margin-bottom: 30px; text-align: center;">
                               Xin chào! 👋<br>
                               Chào mừng bạn đến với cộng đồng yêu ẩm thực địa phương.<br>
                               Sử dụng mã OTP bên dưới để hoàn tất xác thực.
                           </p>
        
                           <!-- OTP Box với thiết kế mới -->
                           <div style="background: linear-gradient(135deg, #FFF8F3 0%, #FFF0E6 100%); border: 2px solid #E8805F; border-radius: 16px; padding: 35px 20px; text-align: center; margin: 35px 0; position: relative; overflow: hidden;">
                               <!-- Decorative elements -->
                               <div style="position: absolute; top: -20px; right: -20px; width: 80px; height: 80px; background-color: rgba(232, 128, 95, 0.1); border-radius: 50%;"></div>
                               <div style="position: absolute; bottom: -30px; left: -30px; width: 100px; height: 100px; background-color: rgba(232, 128, 95, 0.08); border-radius: 50%;"></div>
        
                               <p style="margin: 0 0 15px 0; font-size: 13px; color: #888888; text-transform: uppercase; letter-spacing: 1px; font-weight: 500;">Mã Xác Thực</p>
                               <span style="font-size: 42px; font-weight: 700; color: #E8805F; letter-spacing: 12px; display: block; position: relative; z-index: 1;">%s</span>
                               <p style="margin: 15px 0 0 0; font-size: 12px; color: #999999;">Có hiệu lực trong 5 phút</p>
                           </div>
        
                           <!-- Thông tin bổ sung -->
                           <div style="background-color: #FAFAFA; border-left: 4px solid #E8805F; border-radius: 8px; padding: 20px; margin-top: 30px;">
                               <p style="margin: 0 0 12px 0; font-size: 14px; color: #666666; line-height: 1.6;">
                                   <strong style="color: #E8805F;">⏱️ Lưu ý quan trọng:</strong><br>
                                   Mã OTP chỉ có hiệu lực trong <strong style="color: #E8805F;">5 phút</strong> kể từ khi nhận email này.
                               </p>
                               <p style="margin: 0; font-size: 14px; color: #666666; line-height: 1.6;">
                                   <strong style="color: #E8805F;">🔒 Bảo mật:</strong><br>
                                   Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email và kiểm tra bảo mật tài khoản.
                               </p>
                           </div>
        
                           <!-- Call to action -->
                           <div style="text-align: center; margin-top: 35px;">
                               <p style="font-size: 14px; color: #888888; margin: 0;">
                                   Cần hỗ trợ? Liên hệ với chúng tôi qua email<br>
                                   <a href="mailto:support@localcooking.com" style="color: #E8805F; text-decoration: none; font-weight: 500;">support@localcooking.com</a>
                               </p>
                           </div>
                       </div>
        
                       <!-- Footer -->
                       <div style="background: linear-gradient(180deg, #FAFAFA 0%, #F5F5F5 100%); padding: 30px; text-align: center; border-top: 1px solid #EEEEEE;">
                           <div style="margin-bottom: 15px;">
                               <span style="font-size: 24px; margin: 0 8px;">📧</span>
                               <span style="font-size: 24px; margin: 0 8px;">🍜</span>
                               <span style="font-size: 24px; margin: 0 8px;">👨‍🍳</span>
                           </div>
                           <p style="margin: 0 0 5px 0; font-size: 13px; color: #666666; font-weight: 500;">
                               © 2024 Local Cooking - Lớp học ẩm thực địa phương
                           </p>
                           <p style="margin: 5px 0 0 0; font-size: 12px; color: #999999;">
                               Email tự động - Vui lòng không trả lời trực tiếp
                           </p>
                       </div>
        
                   </div>
        
                   <!-- Spacing bottom -->
                   <div style="height: 30px;"></div>
        
               </body>
               </html>
    """.formatted(otp);

helper.setText(htmlContent, true);
mailSender.send(message);
    }
}
