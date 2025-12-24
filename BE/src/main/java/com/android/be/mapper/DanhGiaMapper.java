package com.android.be.mapper;

import com.android.be.dto.DanhGiaDTO;
import com.android.be.dto.HinhAnhDanhGiaDTO;
import com.android.be.model.DanhGia;
import com.android.be.model.HinhAnhDanhGia;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DanhGiaMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public DanhGiaDTO toDTO(DanhGia danhGia) {
        if (danhGia == null) return null;
        
        DanhGiaDTO dto = new DanhGiaDTO();
        dto.setMaDanhGia(danhGia.getMaDanhGia());
        dto.setMaHocVien(danhGia.getMaHocVien());
        dto.setMaKhoaHoc(danhGia.getMaKhoaHoc());
        dto.setMaDatLich(danhGia.getMaDatLich());
        dto.setDiemDanhGia(danhGia.getDiemDanhGia());
        dto.setBinhLuan(danhGia.getBinhLuan());
        
        // Lấy tên học viên và tên đăng nhập
        if (danhGia.getHocVien() != null) {
            dto.setTenHocVien(danhGia.getHocVien().getHoTen());
            dto.setTenDangNhap(danhGia.getHocVien().getTenDangNhap());
        }
        
        // Lấy tên khóa học
        if (danhGia.getKhoaHoc() != null) {
            dto.setTenKhoaHoc(danhGia.getKhoaHoc().getTenKhoaHoc());
        }
        
        // Format ngày đánh giá
        if (danhGia.getNgayDanhGia() != null) {
            dto.setNgayDanhGia(danhGia.getNgayDanhGia().format(FORMATTER));
        }
        
        // Map hình ảnh
        if (danhGia.getHinhAnhList() != null && !danhGia.getHinhAnhList().isEmpty()) {
            dto.setHinhAnhList(danhGia.getHinhAnhList().stream()
                    .map(this::toHinhAnhDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public HinhAnhDanhGiaDTO toHinhAnhDTO(HinhAnhDanhGia hinhAnh) {
        if (hinhAnh == null) return null;
        
        HinhAnhDanhGiaDTO dto = new HinhAnhDanhGiaDTO();
        dto.setMaHinhAnh(hinhAnh.getMaHinhAnh());
        dto.setMaDanhGia(hinhAnh.getMaDanhGia());
        dto.setDuongDan(hinhAnh.getDuongDan());
        dto.setLoaiFile(hinhAnh.getLoaiFile());
        dto.setThuTu(hinhAnh.getThuTu());
        return dto;
    }
}
