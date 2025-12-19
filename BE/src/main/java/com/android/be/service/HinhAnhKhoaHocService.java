package com.android.be.service;

import com.android.be.dto.HinhAnhKhoaHocDTO;
import com.android.be.model.HinhAnhKhoaHoc;
import com.android.be.repository.HinhAnhKhoaHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HinhAnhKhoaHocService {
    
    private final HinhAnhKhoaHocRepository hinhAnhKhoaHocRepository;
    
    public List<HinhAnhKhoaHocDTO> getHinhAnhByKhoaHoc(Integer maKhoaHoc) {
        return hinhAnhKhoaHocRepository.findByMaKhoaHocOrderByThuTuAsc(maKhoaHoc)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public HinhAnhKhoaHocDTO createHinhAnh(HinhAnhKhoaHocDTO dto) {
        HinhAnhKhoaHoc hinhAnh = convertToEntity(dto);
        HinhAnhKhoaHoc saved = hinhAnhKhoaHocRepository.save(hinhAnh);
        return convertToDTO(saved);
    }
    
    public void deleteHinhAnh(Integer maHinhAnh) {
        hinhAnhKhoaHocRepository.deleteById(maHinhAnh);
    }
    
    private HinhAnhKhoaHocDTO convertToDTO(HinhAnhKhoaHoc entity) {
        HinhAnhKhoaHocDTO dto = new HinhAnhKhoaHocDTO();
        dto.setMaHinhAnh(entity.getMaHinhAnh());
        dto.setMaKhoaHoc(entity.getMaKhoaHoc());
        dto.setDuongDan(entity.getDuongDan());
        dto.setThuTu(entity.getThuTu());
        return dto;
    }
    
    private HinhAnhKhoaHoc convertToEntity(HinhAnhKhoaHocDTO dto) {
        HinhAnhKhoaHoc entity = new HinhAnhKhoaHoc();
        entity.setMaHinhAnh(dto.getMaHinhAnh());
        entity.setMaKhoaHoc(dto.getMaKhoaHoc());
        entity.setDuongDan(dto.getDuongDan());
        entity.setThuTu(dto.getThuTu());
        return entity;
    }
}
