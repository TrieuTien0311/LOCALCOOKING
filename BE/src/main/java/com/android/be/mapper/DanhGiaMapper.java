package com.android.be.mapper;

import com.android.be.dto.DanhGiaDTO;
import com.android.be.model.DanhGia;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DanhGiaMapper {
    
    public DanhGiaDTO toDTO(DanhGia danhGia) {
        if (danhGia == null) return null;
        
        DanhGiaDTO dto = new DanhGiaDTO();
        dto.setMaDanhGia(danhGia.getMaDanhGia());
        dto.setMaHocVien(danhGia.getMaHocVien());
        dto.setMaLopHoc(danhGia.getMaLopHoc());
        dto.setDiemDanhGia(danhGia.getDiemDanhGia());
        dto.setBinhLuan(danhGia.getBinhLuan());
        
        // Format ngày đánh giá
        if (danhGia.getNgayDanhGia() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            if (danhGia.getNgayDanhGia() instanceof LocalDateTime) {
                dto.setNgayDanhGia(((LocalDateTime) danhGia.getNgayDanhGia()).format(formatter));
            } else {
                dto.setNgayDanhGia(danhGia.getNgayDanhGia().toString());
            }
        }
        
        return dto;
    }
}
