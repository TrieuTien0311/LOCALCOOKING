package com.android.be.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service tự động xử lý các đơn đặt lịch hết hạn thanh toán
 * - Chạy mỗi 1 phút để kiểm tra và xóa đơn hết hạn
 * - Giải phóng slot cho khóa học
 * - Gửi thông báo cho người dùng
 */
@Service
public class DonDatLichSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(DonDatLichSchedulerService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Tự động xóa các đơn đặt lịch hết hạn thanh toán
     * Chạy mỗi 1 phút (60000 ms)
     */
    @Scheduled(fixedRate = 60000)
    public void xoaDonHetHan() {
        try {
            logger.info("🔄 Đang kiểm tra đơn đặt lịch hết hạn...");
            
            // Gọi stored procedure sp_XoaDonHetHan
            Map<String, Object> result = jdbcTemplate.queryForMap("EXEC sp_XoaDonHetHan");
            
            Integer soLuongXoa = (Integer) result.get("soLuongXoa");
            String ketQua = (String) result.get("ketQua");
            String thongBao = (String) result.get("thongBao");
            
            if (soLuongXoa != null && soLuongXoa > 0) {
                logger.info("✅ {} - {}", ketQua, thongBao);
            } else {
                logger.debug("📋 Không có đơn hết hạn cần xóa");
            }
            
        } catch (Exception e) {
            logger.error("❌ Lỗi khi xóa đơn hết hạn: {}", e.getMessage());
        }
    }

    /**
     * Tự động cập nhật trạng thái đơn đã hoàn thành (đã qua thời gian học)
     * Chạy mỗi 5 phút (300000 ms)
     */
    @Scheduled(fixedRate = 300000)
    public void capNhatDonHoanThanh() {
        try {
            logger.info("🔄 Đang cập nhật đơn hoàn thành...");
            
            // Gọi stored procedure sp_CapNhatDonHoanThanh
            Map<String, Object> result = jdbcTemplate.queryForMap("EXEC sp_CapNhatDonHoanThanh");
            
            Integer soLuongCapNhat = (Integer) result.get("soLuongCapNhat");
            String ketQua = (String) result.get("ketQua");
            
            if (soLuongCapNhat != null && soLuongCapNhat > 0) {
                logger.info("✅ Đã cập nhật {} đơn sang trạng thái hoàn thành", soLuongCapNhat);
            } else {
                logger.debug("📋 Không có đơn cần cập nhật hoàn thành");
            }
            
        } catch (Exception e) {
            logger.error("❌ Lỗi khi cập nhật đơn hoàn thành: {}", e.getMessage());
        }
    }
}
