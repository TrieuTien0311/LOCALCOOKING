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
        dto.setGioiThieu(lopHoc.getGioiThieu());
        dto.setGiaTriSauBuoiHoc(lopHoc.getGiaTriSauBuoiHoc());
        dto.setThoiGian(lopHoc.getThoiGian());
        
        // Lịch trình lặp lại
        dto.setLoaiLich(lopHoc.getLoaiLich());
        dto.setCacNgayTrongTuan(lopHoc.getCacNgayTrongTuan());
        
        // Format ngày bắt đầu và kết thúc
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (lopHoc.getNgayBatDau() != null) {
            dto.setNgayBatDau(lopHoc.getNgayBatDau().format(formatter));
            dto.setNgay(lopHoc.getNgayBatDau().format(formatter));  // Backward compatibility
        }
        if (lopHoc.getNgayKetThuc() != null) {
            dto.setNgayKetThuc(lopHoc.getNgayKetThuc().format(formatter));
        }
        
        dto.setDiaDiem(lopHoc.getDiaDiem());
        
        // Format giá tiền
        if (lopHoc.getGiaTien() != null) {
            dto.setGia(String.format("%.0f₫", lopHoc.getGiaTien()));
        }
        
        // Sử dụng giá trị từ database (đã được trigger tự động tính)
        dto.setDanhGia(lopHoc.getSaoTrungBinh() != null ? lopHoc.getSaoTrungBinh() : 0.0f);
        dto.setSoDanhGia(lopHoc.getSoLuongDanhGia() != null ? lopHoc.getSoLuongDanhGia() : 0);
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
        dto.setTenGiaoVien(null); // Sẽ load từ bảng GiaoVien nếu cần
        dto.setTrangThai(lopHoc.getTrangThai());
        
        return dto;
    }
}
