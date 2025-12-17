package com.android.be.mapper;

import com.android.be.dto.LopHocDTO;
import com.android.be.model.LopHoc;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Component
public class LopHocMapper {
    
    private final DanhMucMonAnMapper danhMucMonAnMapper;
    
    public LopHocMapper(DanhMucMonAnMapper danhMucMonAnMapper) {
        this.danhMucMonAnMapper = danhMucMonAnMapper;
    }
    
    public LopHocDTO toDTO(LopHoc lopHoc) {
        if (lopHoc == null) return null;
        
        LopHocDTO dto = new LopHocDTO();
        dto.setMaLopHoc(lopHoc.getMaLopHoc());
        dto.setTenLop(lopHoc.getTenLopHoc());
        dto.setMoTa(lopHoc.getMoTa());
        dto.setThoiGian(lopHoc.getThoiGian());
        
        // Format ngày
        if (lopHoc.getNgayDienRa() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (lopHoc.getNgayDienRa() instanceof LocalDate) {
                dto.setNgay(((LocalDate) lopHoc.getNgayDienRa()).format(formatter));
            } else {
                // Fallback for java.sql.Date
                dto.setNgay(lopHoc.getNgayDienRa().toString());
            }
        }
        
        dto.setDiaDiem(lopHoc.getDiaDiem());
        
        // Format giá tiền
        if (lopHoc.getGiaTien() != null) {
            dto.setGia(String.format("%.0f₫", lopHoc.getGiaTien()));
        }
        
        dto.setDanhGia(0.0f); // Sẽ tính từ DanhGia
        dto.setSoDanhGia(0); // Sẽ tính từ DanhGia
        dto.setHinhAnh(lopHoc.getHinhAnh());
        dto.setCoUuDai(lopHoc.getCoUuDai() != null ? lopHoc.getCoUuDai() : false);
        
        // Format thời gian kết thúc
        if (lopHoc.getGioKetThuc() != null) {
            dto.setThoiGianKetThuc(lopHoc.getGioKetThuc().toString());
        }
        
        // Tính suất còn lại
        Integer suat = (lopHoc.getSoLuongToiDa() != null ? lopHoc.getSoLuongToiDa() : 0) - 
                       (lopHoc.getSoLuongHienTai() != null ? lopHoc.getSoLuongHienTai() : 0);
        dto.setSuat(suat);
        
        dto.setIsFavorite(false); // Sẽ check từ YeuThich table
        dto.setDaDienRa(false); // Sẽ check từ ngày
        dto.setLichTrinhLopHoc(new ArrayList<>());
        dto.setTenGiaoVien(lopHoc.getTenGiaoVien());
        dto.setTrangThai(lopHoc.getTrangThai());
        
        return dto;
    }
}
