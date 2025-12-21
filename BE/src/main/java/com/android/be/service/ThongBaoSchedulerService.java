package com.android.be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.android.be.model.ThongBao;
import com.android.be.repository.ThongBaoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service tự động tạo thông báo nhắc nhở lớp học
 * - Trước 1 ngày: "Lớp học sắp diễn ra"
 * - Trước 30 phút: "Còn 30 phút nữa sẽ bắt đầu lớp học"
 */
@Service
public class ThongBaoSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(ThongBaoSchedulerService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    /**
     * Chạy mỗi ngày lúc 8:00 sáng
     * Tạo thông báo nhắc nhở cho các lớp học diễn ra vào ngày mai
     */
    @Scheduled(cron = "0 0 8 * * *") // 8:00 AM mỗi ngày
    public void taoThongBaoTruoc1Ngay() {
        try {
            logger.info("Bắt đầu tạo thông báo nhắc nhở trước 1 ngày...");
            
            LocalDate ngayMai = LocalDate.now().plusDays(1);
            logger.info("Tìm lớp học vào ngày: {}", ngayMai);
            
            // Sử dụng CONVERT để format ngày và giờ thành string trong SQL
            String sql = "SELECT DISTINCT d.maHocVien, kh.tenKhoaHoc, " +
                        "CONVERT(VARCHAR(10), d.ngayThamGia, 103) as ngayThamGiaStr, " +
                        "CONVERT(VARCHAR(5), lt.gioBatDau, 108) as gioBatDauStr, " +
                        "lt.diaDiem, kh.hinhAnh " +
                        "FROM DatLich d " +
                        "JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh " +
                        "JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc " +
                        "WHERE CAST(d.ngayThamGia AS DATE) = ?1 " +
                        "AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')";
            
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, java.sql.Date.valueOf(ngayMai));
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            
            logger.info("Tìm thấy {} lớp học", results.size());
            
            int count = 0;
            for (Object[] row : results) {
                try {
                    Integer maHocVien = (Integer) row[0];
                    String tenKhoaHoc = (String) row[1];
                    String ngayThamGiaStr = (String) row[2]; // Đã format sẵn dd/MM/yyyy
                    String gioBatDauStr = (String) row[3]; // Đã format sẵn HH:mm
                    String diaDiem = (String) row[4];
                    String hinhAnh = (String) row[5];
                    
                    logger.info("Xử lý: User {}, Khóa học: {}", maHocVien, tenKhoaHoc);
                    
                    // Kiểm tra đã có thông báo chưa
                    if (!daCoThongBaoNhacNho1Ngay(maHocVien, tenKhoaHoc)) {
                        ThongBao tb = new ThongBao();
                        tb.setMaNguoiNhan(maHocVien);
                        tb.setTieuDe("🔔 Lớp học sắp diễn ra");
                        tb.setNoiDung("Lớp \"" + tenKhoaHoc + "\" sẽ diễn ra vào ngày mai (" +
                                ngayThamGiaStr + ") lúc " + gioBatDauStr +
                                " tại " + diaDiem + ". Hãy chuẩn bị sẵn sàng nhé!");
                        tb.setLoaiThongBao("NhacNho");
                        tb.setHinhAnh(hinhAnh);
                        tb.setDaDoc(false);
                        tb.setNgayTao(LocalDateTime.now());
                        
                        thongBaoRepository.save(tb);
                        count++;
                        logger.info("Đã tạo thông báo cho user {}", maHocVien);
                    } else {
                        logger.info("Đã có thông báo cho user {} hôm nay", maHocVien);
                    }
                } catch (Exception e) {
                    logger.error("Lỗi xử lý thông báo cho row: {}", e.getMessage());
                }
            }
            
            logger.info("Đã tạo {} thông báo nhắc nhở trước 1 ngày", count);
            
        } catch (Exception e) {
            logger.error("Lỗi khi tạo thông báo trước 1 ngày: {}", e.getMessage(), e);
        }
    }

    /**
     * Chạy mỗi 5 phút
     * Tạo thông báo nhắc nhở cho các lớp học bắt đầu trong 30 phút tới
     */
    @Scheduled(fixedRate = 300000) // 5 phút = 300,000 ms
    public void taoThongBaoTruoc30Phut() {
        try {
            LocalDate homNay = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(homNay);
            LocalTime gioHienTai = LocalTime.now();
            
            // Format thời gian thành chuỗi HH:mm:ss để so sánh trong SQL Server
            String gioSau25Phut = gioHienTai.plusMinutes(25).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String gioSau35Phut = gioHienTai.plusMinutes(35).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            
            String sql = "SELECT DISTINCT d.maHocVien, kh.tenKhoaHoc, " +
                        "lt.gioBatDau, lt.diaDiem, kh.hinhAnh " +
                        "FROM DatLich d " +
                        "JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh " +
                        "JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc " +
                        "WHERE d.ngayThamGia = ?1 " +
                        "AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành') " +
                        "AND lt.gioBatDau >= CAST(?2 AS TIME) " +
                        "AND lt.gioBatDau <= CAST(?3 AS TIME)";
            
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, sqlDate);
            query.setParameter(2, gioSau25Phut);
            query.setParameter(3, gioSau35Phut);
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            
            int count = 0;
            for (Object[] row : results) {
                try {
                    Integer maHocVien = (Integer) row[0];
                    String tenKhoaHoc = (String) row[1];
                    LocalTime gioBatDau = ((java.sql.Time) row[2]).toLocalTime();
                    String diaDiem = (String) row[3];
                    String hinhAnh = (String) row[4];
                    
                    // Kiểm tra đã có thông báo chưa
                    if (!daCoThongBaoNhacNho30Phut(maHocVien, tenKhoaHoc)) {
                        ThongBao tb = new ThongBao();
                        tb.setMaNguoiNhan(maHocVien);
                        tb.setTieuDe("⏰ Còn 30 phút nữa!");
                        tb.setNoiDung("Lớp \"" + tenKhoaHoc + "\" sẽ bắt đầu lúc " +
                                gioBatDau.format(DateTimeFormatter.ofPattern("HH:mm")) +
                                " tại " + diaDiem + ". Hãy đến đúng giờ nhé!");
                        tb.setLoaiThongBao("NhacNho");
                        tb.setHinhAnh(hinhAnh);
                        tb.setDaDoc(false);
                        tb.setNgayTao(LocalDateTime.now());
                        
                        thongBaoRepository.save(tb);
                        count++;
                    }
                } catch (Exception e) {
                    logger.error("Lỗi xử lý thông báo 30 phút: {}", e.getMessage());
                }
            }
            
            if (count > 0) {
                logger.info("Đã tạo {} thông báo nhắc nhở trước 30 phút", count);
            }
            
        } catch (Exception e) {
            logger.error("Lỗi khi tạo thông báo trước 30 phút: {}", e.getMessage(), e);
        }
    }
    
    private boolean daCoThongBaoNhacNho1Ngay(Integer maHocVien, String tenKhoaHoc) {
        String sql = "SELECT COUNT(*) FROM ThongBao " +
                    "WHERE maNguoiNhan = ?1 " +
                    "AND loaiThongBao = N'NhacNho' " +
                    "AND tieuDe = N'🔔 Lớp học sắp diễn ra' " +
                    "AND noiDung LIKE ?2 " +
                    "AND CAST(ngayTao AS DATE) = CAST(GETDATE() AS DATE)";
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, maHocVien);
        query.setParameter(2, "%" + tenKhoaHoc + "%");
        
        Number count = (Number) query.getSingleResult();
        return count.intValue() > 0;
    }
    
    private boolean daCoThongBaoNhacNho30Phut(Integer maHocVien, String tenKhoaHoc) {
        String sql = "SELECT COUNT(*) FROM ThongBao " +
                    "WHERE maNguoiNhan = ?1 " +
                    "AND loaiThongBao = N'NhacNho' " +
                    "AND tieuDe = N'⏰ Còn 30 phút nữa!' " +
                    "AND noiDung LIKE ?2 " +
                    "AND CAST(ngayTao AS DATE) = CAST(GETDATE() AS DATE)";
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, maHocVien);
        query.setParameter(2, "%" + tenKhoaHoc + "%");
        
        Number count = (Number) query.getSingleResult();
        return count.intValue() > 0;
    }
    
    /**
     * Version test không kiểm tra trùng lặp
     */
    @Transactional
    public int taoThongBaoTruoc1NgayKhongKiemTra() {
        LocalDate ngayMai = LocalDate.now().plusDays(1);
        logger.info("TEST: Tìm lớp học vào ngày: {}", ngayMai);
        
        // Sử dụng CONVERT thay vì FORMAT để tương thích tốt hơn
        String sql = "SELECT DISTINCT d.maHocVien, kh.tenKhoaHoc, " +
                    "CONVERT(VARCHAR(10), d.ngayThamGia, 103) as ngayThamGiaStr, " +
                    "CONVERT(VARCHAR(5), lt.gioBatDau, 108) as gioBatDauStr, " +
                    "lt.diaDiem, kh.hinhAnh " +
                    "FROM DatLich d " +
                    "JOIN LichTrinhLopHoc lt ON d.maLichTrinh = lt.maLichTrinh " +
                    "JOIN KhoaHoc kh ON lt.maKhoaHoc = kh.maKhoaHoc " +
                    "WHERE CAST(d.ngayThamGia AS DATE) = ?1 " +
                    "AND d.trangThai NOT IN (N'Đã Hủy', N'Hoàn Thành')";
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, java.sql.Date.valueOf(ngayMai));
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        logger.info("TEST: Tìm thấy {} lớp học", results.size());
        
        int count = 0;
        for (Object[] row : results) {
            try {
                Integer maHocVien = (Integer) row[0];
                String tenKhoaHoc = (String) row[1];
                String ngayThamGiaStr = (String) row[2]; // Đã format sẵn dd/MM/yyyy
                String gioBatDauStr = (String) row[3]; // Đã format sẵn HH:mm
                String diaDiem = (String) row[4];
                String hinhAnh = (String) row[5];
                
                logger.info("TEST: Tạo thông báo cho User {}, Khóa học: {}, Giờ: {}", maHocVien, tenKhoaHoc, gioBatDauStr);
                
                ThongBao tb = new ThongBao();
                tb.setMaNguoiNhan(maHocVien);
                tb.setTieuDe("🔔 Lớp học sắp diễn ra");
                tb.setNoiDung("Lớp \"" + tenKhoaHoc + "\" sẽ diễn ra vào ngày mai (" +
                        ngayThamGiaStr + ") lúc " + gioBatDauStr +
                        " tại " + diaDiem + ". Hãy chuẩn bị sẵn sàng nhé!");
                tb.setLoaiThongBao("NhacNho");
                tb.setHinhAnh(hinhAnh);
                tb.setDaDoc(false);
                tb.setNgayTao(LocalDateTime.now());
                
                thongBaoRepository.save(tb);
                count++;
                logger.info("TEST: Đã tạo thông báo #{}", count);
            } catch (Exception e) {
                logger.error("TEST: Lỗi xử lý row: {}", e.getMessage(), e);
            }
        }
        
        logger.info("TEST: Tổng cộng tạo {} thông báo", count);
        return count;
    }
}
