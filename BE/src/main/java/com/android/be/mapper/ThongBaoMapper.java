package com.android.be.mapper;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.model.ThongBao;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ThongBaoMapper {
    
    public ThongBaoDTO toDTO(ThongBao thongBao) {
        if (thongBao == null) return null;
        
        ThongBaoDTO dto = new ThongBaoDTO();
        dto.setMaThongBao(thongBao.getMaThongBao());
        dto.setTieuDeTB(thongBao.getTieuDe());
        dto.setNoiDungTB(thongBao.getNoiDung());
        
        // Format thời gian theo kiểu "X phút trước", "X giờ trước", "X ngày trước"
        if (thongBao.getNgayTao() != null) {
            dto.setThoiGianTB(formatTimeAgo(thongBao.getNgayTao()));
        }
        
        dto.setAnhTB(thongBao.getHinhAnh());
        dto.setTrangThai(thongBao.getDaDoc() != null ? thongBao.getDaDoc() : false);
        dto.setLoaiThongBao(thongBao.getLoaiThongBao());
        
        return dto;
    }
    
    // Format thời gian theo kiểu "X phút trước", "X giờ trước"
    private String formatTimeAgo(LocalDateTime ngayTao) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(ngayTao, now);
        
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (minutes < 1) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 7) {
            return days + " ngày trước";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + " tuần trước";
        } else if (days < 365) {
            long months = days / 30;
            return months + " tháng trước";
        } else {
            // Nếu quá lâu, hiển thị ngày tháng
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return ngayTao.format(formatter);
        }
    }
}
