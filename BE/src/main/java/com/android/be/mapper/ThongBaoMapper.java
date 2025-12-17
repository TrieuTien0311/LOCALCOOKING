package com.android.be.mapper;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.model.ThongBao;
import org.springframework.stereotype.Component;
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
        
        // Format thời gian
        if (thongBao.getNgayTao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            if (thongBao.getNgayTao() instanceof LocalDateTime) {
                dto.setThoiGianTB(((LocalDateTime) thongBao.getNgayTao()).format(formatter));
            } else {
                dto.setThoiGianTB(thongBao.getNgayTao().toString());
            }
        }
        
        dto.setAnhTB(thongBao.getHinhAnh());
        dto.setTrangThai(thongBao.getDaDoc() != null ? thongBao.getDaDoc() : false);
        dto.setLoaiThongBao(thongBao.getLoaiThongBao());
        
        return dto;
    }
}
