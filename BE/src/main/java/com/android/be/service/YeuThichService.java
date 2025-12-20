package com.android.be.service;

import com.android.be.dto.YeuThichDTO;
import com.android.be.model.KhoaHoc;
import com.android.be.model.LichTrinhLopHoc;
import com.android.be.model.YeuThich;
import com.android.be.repository.KhoaHocRepository;
import com.android.be.repository.LichTrinhLopHocRepository;
import com.android.be.repository.YeuThichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class YeuThichService {
    
    @Autowired
    private YeuThichRepository yeuThichRepository;
    
    @Autowired
    private KhoaHocRepository khoaHocRepository;
    
    @Autowired
    private LichTrinhLopHocRepository lichTrinhLopHocRepository;
    
    /**
     * Lấy danh sách khóa học yêu thích của học viên (trả về KhoaHoc đầy đủ)
     */
    public List<KhoaHoc> getFavoriteKhoaHocByHocVien(Integer maHocVien) {
        List<YeuThich> favorites = yeuThichRepository.findByMaHocVien(maHocVien);
        
        return favorites.stream()
                .map(yeuThich -> {
                    KhoaHoc khoaHoc = khoaHocRepository.findById(yeuThich.getMaKhoaHoc()).orElse(null);
                    if (khoaHoc != null) {
                        // Load lịch trình cho khóa học
                        List<LichTrinhLopHoc> lichTrinhList = lichTrinhLopHocRepository.findByMaKhoaHoc(yeuThich.getMaKhoaHoc());
                        khoaHoc.setLichTrinhList(lichTrinhList);
                    }
                    return khoaHoc;
                })
                .filter(khoaHoc -> khoaHoc != null)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy danh sách khóa học yêu thích của học viên (trả về DTO)
     */
    public List<YeuThichDTO> getFavoritesByHocVien(Integer maHocVien) {
        List<YeuThich> favorites = yeuThichRepository.findByMaHocVien(maHocVien);
        
        return favorites.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra khóa học đã được yêu thích chưa
     */
    public boolean isFavorite(Integer maHocVien, Integer maKhoaHoc) {
        return yeuThichRepository.existsByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc);
    }
    
    /**
     * Thêm khóa học vào danh sách yêu thích
     */
    @Transactional
    public YeuThichDTO addFavorite(Integer maHocVien, Integer maKhoaHoc) {
        // Kiểm tra đã tồn tại chưa
        if (yeuThichRepository.existsByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc)) {
            throw new RuntimeException("Khóa học đã có trong danh sách yêu thích");
        }
        
        // Kiểm tra khóa học có tồn tại không
        if (!khoaHocRepository.existsById(maKhoaHoc)) {
            throw new RuntimeException("Khóa học không tồn tại");
        }
        
        YeuThich yeuThich = new YeuThich(maHocVien, maKhoaHoc);
        YeuThich saved = yeuThichRepository.save(yeuThich);
        
        return convertToDTO(saved);
    }
    
    /**
     * Xóa khóa học khỏi danh sách yêu thích
     */
    @Transactional
    public void removeFavorite(Integer maHocVien, Integer maKhoaHoc) {
        if (!yeuThichRepository.existsByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc)) {
            throw new RuntimeException("Khóa học không có trong danh sách yêu thích");
        }
        
        yeuThichRepository.deleteByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc);
    }
    
    /**
     * Toggle yêu thích (thêm nếu chưa có, xóa nếu đã có)
     */
    @Transactional
    public boolean toggleFavorite(Integer maHocVien, Integer maKhoaHoc) {
        if (yeuThichRepository.existsByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc)) {
            // Đã yêu thích → Xóa
            yeuThichRepository.deleteByMaHocVienAndMaKhoaHoc(maHocVien, maKhoaHoc);
            return false; // Đã xóa
        } else {
            // Chưa yêu thích → Thêm
            YeuThich yeuThich = new YeuThich(maHocVien, maKhoaHoc);
            yeuThichRepository.save(yeuThich);
            return true; // Đã thêm
        }
    }
    
    /**
     * Đếm số lượng yêu thích của một khóa học
     */
    public Long countFavoritesByKhoaHoc(Integer maKhoaHoc) {
        return yeuThichRepository.countByMaKhoaHoc(maKhoaHoc);
    }
    
    /**
     * Convert YeuThich sang YeuThichDTO (kèm thông tin khóa học)
     */
    private YeuThichDTO convertToDTO(YeuThich yeuThich) {
        YeuThichDTO dto = new YeuThichDTO();
        dto.setMaYeuThich(yeuThich.getMaYeuThich());
        dto.setMaHocVien(yeuThich.getMaHocVien());
        dto.setMaKhoaHoc(yeuThich.getMaKhoaHoc());
        dto.setNgayThem(yeuThich.getNgayThem());
        
        // Lấy thông tin khóa học
        khoaHocRepository.findById(yeuThich.getMaKhoaHoc()).ifPresent(khoaHoc -> {
            dto.setTenKhoaHoc(khoaHoc.getTenKhoaHoc());
            dto.setMoTa(khoaHoc.getMoTa());
            dto.setGiaTien(khoaHoc.getGiaTien() != null ? khoaHoc.getGiaTien().doubleValue() : null);
            dto.setHinhAnh(khoaHoc.getHinhAnh());
            dto.setSaoTrungBinh(khoaHoc.getSaoTrungBinh());
            dto.setSoLuongDanhGia(khoaHoc.getSoLuongDanhGia());
            dto.setCoUuDai(khoaHoc.getCoUuDai());
        });
        
        return dto;
    }
}
