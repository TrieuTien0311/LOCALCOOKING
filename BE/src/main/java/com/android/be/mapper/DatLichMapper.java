package com.android.be.mapper;

import com.android.be.dto.DatLichDTO;
import com.android.be.model.DatLich;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DatLichMapper {
    
    public DatLichDTO toDTO(DatLich datLich) {
        if (datLich == null) return null;
        
        DatLichDTO dto = new DatLichDTO();
        dto.setMaDatLich(datLich.getMaDatLich());
        dto.setMaHocVien(datLich.getMaHocVien());
        
        // Get maKhoaHoc from LichTrinhLopHoc relationship
        if (datLich.getLichTrinh() != null) {
            dto.setMaLopHoc(datLich.getLichTrinh().getMaKhoaHoc());
            
            // Get tenLopHoc from KhoaHoc if available
            if (datLich.getLichTrinh().getKhoaHoc() != null) {
                dto.setTenLopHoc(datLich.getLichTrinh().getKhoaHoc().getTenKhoaHoc());
                dto.setHinhAnhLopHoc(datLich.getLichTrinh().getKhoaHoc().getHinhAnh());
            }
        }
        
        dto.setSoLuongNguoi(datLich.getSoLuongNguoi());
        dto.setTongTien(datLich.getTongTien() != null ? datLich.getTongTien().doubleValue() : null);
        dto.setTenNguoiDat(datLich.getTenNguoiDat());
        dto.setEmailNguoiDat(datLich.getEmailNguoiDat());
        dto.setSdtNguoiDat(datLich.getSdtNguoiDat());
        
        // Format ngày đặt
        if (datLich.getNgayDat() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            if (datLich.getNgayDat() instanceof LocalDateTime) {
                dto.setNgayDat(((LocalDateTime) datLich.getNgayDat()).format(formatter));
            } else {
                dto.setNgayDat(datLich.getNgayDat().toString());
            }
        }
        
        dto.setTrangThai(datLich.getTrangThai());
        dto.setGhiChu(datLich.getGhiChu());
        
        return dto;
    }
}
