package com.android.be.service;

import com.android.be.dto.GiaoVienDTO;
import com.android.be.model.GiaoVien;
import com.android.be.repository.GiaoVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiaoVienService {
    
    private final GiaoVienRepository giaoVienRepository;
    
    public Optional<GiaoVienDTO> getGiaoVienById(Integer id) {
        try {
            List<Object[]> results = giaoVienRepository.findGiaoVienWithNguoiDung(id);
            
            if (results != null && !results.isEmpty()) {
                Object[] result = results.get(0);
                
                GiaoVienDTO dto = new GiaoVienDTO();
                dto.setMaGiaoVien((Integer) result[0]);
                dto.setMaNguoiDung((Integer) result[1]);
                dto.setChuyenMon((String) result[2]);
                dto.setKinhNghiem((String) result[3]);
                dto.setLichSuKinhNghiem((String) result[4]);
                dto.setMoTa((String) result[5]);
                dto.setHinhAnh((String) result[6]);
                dto.setHoTen((String) result[7]);
                dto.setEmail((String) result[8]);
                dto.setSoDienThoai((String) result[9]);
                
                return Optional.of(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
}
