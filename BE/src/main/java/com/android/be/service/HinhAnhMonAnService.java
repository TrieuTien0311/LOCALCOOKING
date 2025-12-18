package com.android.be.service;

import com.android.be.dto.HinhAnhMonAnDTO;
import com.android.be.model.HinhAnhMonAn;
import com.android.be.repository.HinhAnhMonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HinhAnhMonAnService {
    
    @Autowired
    private HinhAnhMonAnRepository hinhAnhMonAnRepository;
    
    public List<HinhAnhMonAnDTO> getHinhAnhByMonAn(Integer maMonAn) {
        return hinhAnhMonAnRepository.findByMaMonAnOrderByThuTuAsc(maMonAn)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public HinhAnhMonAnDTO createHinhAnh(HinhAnhMonAnDTO dto) {
        HinhAnhMonAn hinhAnh = convertToEntity(dto);
        HinhAnhMonAn saved = hinhAnhMonAnRepository.save(hinhAnh);
        return convertToDTO(saved);
    }
    
    public void deleteHinhAnh(Integer maHinhAnh) {
        hinhAnhMonAnRepository.deleteById(maHinhAnh);
    }
    
    private HinhAnhMonAnDTO convertToDTO(HinhAnhMonAn entity) {
        HinhAnhMonAnDTO dto = new HinhAnhMonAnDTO();
        dto.setMaHinhAnh(entity.getMaHinhAnh());
        dto.setMaMonAn(entity.getMaMonAn());
        dto.setDuongDan(entity.getDuongDan());
        dto.setThuTu(entity.getThuTu());
        return dto;
    }
    
    private HinhAnhMonAn convertToEntity(HinhAnhMonAnDTO dto) {
        HinhAnhMonAn entity = new HinhAnhMonAn();
        entity.setMaHinhAnh(dto.getMaHinhAnh());
        entity.setMaMonAn(dto.getMaMonAn());
        entity.setDuongDan(dto.getDuongDan());
        entity.setThuTu(dto.getThuTu());
        return entity;
    }
}
