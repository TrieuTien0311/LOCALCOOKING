package com.android.be.service;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.dto.LopHocDTO;
import com.android.be.dto.MonAnDTO;
import com.android.be.mapper.LopHocMapper;
import com.android.be.mapper.MonAnMapper;
import com.android.be.model.LopHoc;
import com.android.be.repository.DanhGiaRepository;
import com.android.be.repository.DanhMucMonAnRepository;
import com.android.be.repository.LopHocRepository;
import com.android.be.repository.MonAnRepository;
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
    private final DanhMucMonAnRepository danhMucMonAnRepository;
    private final MonAnRepository monAnRepository;
    private final LopHocMapper lopHocMapper;
    private final MonAnMapper monAnMapper;
    
    public List<LopHocDTO> getAllLopHoc() {
        return lopHocRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<LopHocDTO> getLopHocById(Integer id) {
        return lopHocRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public LopHoc createLopHoc(LopHoc lopHoc) {
        return lopHocRepository.save(lopHoc);
    }
    
    public LopHoc updateLopHoc(Integer id, LopHoc lopHoc) {
        lopHoc.setMaLopHoc(id);
        return lopHocRepository.save(lopHoc);
    }
    
    public void deleteLopHoc(Integer id) {
        lopHocRepository.deleteById(id);
    }
    
    private LopHocDTO convertToDTO(LopHoc lopHoc) {
        LopHocDTO dto = lopHocMapper.toDTO(lopHoc);
        
        // Tính điểm đánh giá trung bình
        List<Integer> danhGias = danhGiaRepository.findAll().stream()
                .filter(dg -> dg.getMaLopHoc().equals(lopHoc.getMaLopHoc()))
                .map(dg -> dg.getDiemDanhGia())
                .collect(Collectors.toList());
        
        if (!danhGias.isEmpty()) {
            float avg = (float) danhGias.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
            dto.setDanhGia(avg);
            dto.setSoDanhGia(danhGias.size());
        }
        
        // Load lịch trình lớp học (DanhMucMonAn + MonAn)
        List<DanhMucMonAnDTO> lichTrinh = danhMucMonAnRepository.findAll().stream()
                .filter(dm -> dm.getMaLopHoc().equals(lopHoc.getMaLopHoc()))
                .map(danhMuc -> {
                    DanhMucMonAnDTO dmDTO = new DanhMucMonAnDTO();
                    dmDTO.setMaDanhMuc(danhMuc.getMaDanhMuc());
                    dmDTO.setTenDanhMuc(danhMuc.getTenDanhMuc());
                    dmDTO.setThoiGian(danhMuc.getThoiGian());
                    dmDTO.setIconDanhMuc(danhMuc.getIconDanhMuc());
                    dmDTO.setThuTu(danhMuc.getThuTu());
                    
                    // Load món ăn của danh mục này
                    List<MonAnDTO> monAns = monAnRepository.findAll().stream()
                            .filter(ma -> ma.getMaDanhMuc().equals(danhMuc.getMaDanhMuc()))
                            .map(monAnMapper::toDTO)
                            .collect(Collectors.toList());
                    
                    dmDTO.setDanhSachMon(monAns);
                    return dmDTO;
                })
                .sorted((a, b) -> Integer.compare(a.getThuTu(), b.getThuTu()))
                .collect(Collectors.toList());
        
        dto.setLichTrinhLopHoc(lichTrinh);
        
        return dto;
    }
}
