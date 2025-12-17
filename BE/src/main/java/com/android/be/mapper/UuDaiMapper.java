package com.android.be.mapper;

import com.android.be.dto.UuDaiDTO;
import com.android.be.model.UuDai;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UuDaiMapper {
    
    public UuDaiDTO toDTO(UuDai uuDai) {
        if (uuDai == null) return null;
        
        UuDaiDTO dto = new UuDaiDTO();
        dto.setMaUuDai(uuDai.getMaUuDai());
        dto.setMaCode(uuDai.getMaCode());
        dto.setHinhAnh(uuDai.getHinhAnh());
        dto.setTieuDe(uuDai.getTenUuDai());
        dto.setMoTa(uuDai.getMoTa());
        
        // Format HSD
        if (uuDai.getNgayKetThuc() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (uuDai.getNgayKetThuc() instanceof LocalDate) {
                dto.setHSD(((LocalDate) uuDai.getNgayKetThuc()).format(formatter));
            } else {
                dto.setHSD(uuDai.getNgayKetThuc().toString());
            }
        }
        
        dto.setDuocChon(false);
        dto.setLoaiGiam(uuDai.getLoaiGiam());
        dto.setGiaTriGiam(uuDai.getGiaTriGiam() != null ? uuDai.getGiaTriGiam().doubleValue() : null);
        dto.setGiamToiDa(uuDai.getGiamToiDa() != null ? uuDai.getGiamToiDa().doubleValue() : null);
        dto.setSoLuong(uuDai.getSoLuong());
        dto.setSoLuongDaSuDung(uuDai.getSoLuongDaSuDung());
        dto.setTrangThai(uuDai.getTrangThai());
        
        return dto;
    }
}
