package com.android.be.service;

import com.android.be.dto.LopHocResponse;
import com.android.be.model.LopHoc;
import com.android.be.repository.DanhGiaRepository;
import com.android.be.repository.LopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LopHocService {
    
    private final LopHocRepository lopHocRepository;
    private final DanhGiaRepository danhGiaRepository;
    
    public List<LopHoc> getAllLopHoc() {
        return lopHocRepository.findAll();
    }
    
    public List<LopHocResponse> getAllLopHocWithRating() {
        List<LopHoc> lopHocs = lopHocRepository.findAll();
        return lopHocs.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public Optional<LopHoc> getLopHocById(Integer id) {
        return lopHocRepository.findById(id);
    }
    
    public Optional<LopHocResponse> getLopHocResponseById(Integer id) {
        return lopHocRepository.findById(id)
            .map(this::convertToResponse);
    }
    
    public LopHoc createLopHoc(LopHoc lopHoc) {
        return lopHocRepository.save(lopHoc);
    }
    
    public Optional<LopHoc> updateLopHoc(Integer id, LopHoc lopHocDetails) {
        return lopHocRepository.findById(id)
            .map(lopHoc -> {
                lopHoc.setTenLopHoc(lopHocDetails.getTenLopHoc());
                lopHoc.setMoTa(lopHocDetails.getMoTa());
                lopHoc.setGiaTien(lopHocDetails.getGiaTien());
                lopHoc.setThoiGian(lopHocDetails.getThoiGian());
                lopHoc.setDiaDiem(lopHocDetails.getDiaDiem());
                lopHoc.setTrangThai(lopHocDetails.getTrangThai());
                return lopHocRepository.save(lopHoc);
            });
    }
    
    public boolean deleteLopHoc(Integer id) {
        return lopHocRepository.findById(id)
            .map(lopHoc -> {
                lopHocRepository.delete(lopHoc);
                return true;
            })
            .orElse(false);
    }
    
    private LopHocResponse convertToResponse(LopHoc lopHoc) {
        LopHocResponse response = new LopHocResponse();
        response.setMaLopHoc(lopHoc.getMaLopHoc());
        response.setTenLopHoc(lopHoc.getTenLopHoc());
        response.setMoTa(lopHoc.getMoTa());
        response.setTenGiaoVien(lopHoc.getTenGiaoVien());
        response.setSoLuongToiDa(lopHoc.getSoLuongToiDa());
        response.setSoLuongHienTai(lopHoc.getSoLuongHienTai());
        response.setSoLuongConLai(lopHoc.getSoLuongToiDa() - lopHoc.getSoLuongHienTai());
        response.setGiaTien(lopHoc.getGiaTien());
        response.setThoiGian(lopHoc.getThoiGian());
        response.setDiaDiem(lopHoc.getDiaDiem());
        response.setTrangThai(lopHoc.getTrangThai());
        response.setNgayDienRa(lopHoc.getNgayDienRa());
        response.setGioBatDau(lopHoc.getGioBatDau());
        response.setGioKetThuc(lopHoc.getGioKetThuc());
        response.setHinhAnh(lopHoc.getHinhAnh());
        response.setCoUuDai(lopHoc.getCoUuDai());
        
        // Tính điểm đánh giá trung bình
        Double avgRating = danhGiaRepository.getAverageRatingByLopHoc(lopHoc.getMaLopHoc());
        response.setDiemDanhGia(avgRating != null ? avgRating : 0.0);
        
        // Đếm số lượt đánh giá
        Integer countRating = danhGiaRepository.countByMaLopHoc(lopHoc.getMaLopHoc());
        response.setSoDanhGia(countRating != null ? countRating : 0);
        
        return response;
    }
}
