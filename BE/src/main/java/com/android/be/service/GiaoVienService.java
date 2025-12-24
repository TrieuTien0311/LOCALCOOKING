package com.android.be.service;

import com.android.be.dto.GiaoVienDTO;
import com.android.be.model.GiaoVien;
import com.android.be.repository.GiaoVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiaoVienService {
    
    private final GiaoVienRepository giaoVienRepository;
    
    public List<GiaoVienDTO> getAllGiaoVien() {
        List<GiaoVienDTO> result = new ArrayList<>();
        try {
            List<Object[]> results = giaoVienRepository.findAllGiaoVienWithNguoiDung();
            
            for (Object[] row : results) {
                GiaoVienDTO dto = new GiaoVienDTO();
                dto.setMaGiaoVien((Integer) row[0]);
                dto.setMaNguoiDung((Integer) row[1]);
                dto.setChuyenMon((String) row[2]);
                dto.setKinhNghiem((String) row[3]);
                dto.setLichSuKinhNghiem((String) row[4]);
                dto.setMoTa((String) row[5]);
                dto.setHinhAnh((String) row[6]);
                dto.setHoTen((String) row[7]);
                dto.setEmail((String) row[8]);
                dto.setSoDienThoai((String) row[9]);
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
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
    
    public GiaoVienDTO createGiaoVien(GiaoVienDTO dto) {
        try {
            GiaoVien giaoVien = new GiaoVien();
            giaoVien.setChuyenMon(dto.getChuyenMon());
            giaoVien.setKinhNghiem(dto.getKinhNghiem());
            giaoVien.setLichSuKinhNghiem(dto.getLichSuKinhNghiem());
            giaoVien.setMoTa(dto.getMoTa());
            giaoVien.setHinhAnh(dto.getHinhAnh());
            giaoVien.setMaNguoiDung(dto.getMaNguoiDung());
            
            GiaoVien saved = giaoVienRepository.save(giaoVien);
            dto.setMaGiaoVien(saved.getMaGiaoVien());
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public GiaoVienDTO updateGiaoVien(Integer id, GiaoVienDTO dto) {
        try {
            Optional<GiaoVien> optGiaoVien = giaoVienRepository.findById(id);
            if (optGiaoVien.isPresent()) {
                GiaoVien giaoVien = optGiaoVien.get();
                giaoVien.setChuyenMon(dto.getChuyenMon());
                giaoVien.setKinhNghiem(dto.getKinhNghiem());
                giaoVien.setLichSuKinhNghiem(dto.getLichSuKinhNghiem());
                giaoVien.setMoTa(dto.getMoTa());
                if (dto.getHinhAnh() != null) {
                    giaoVien.setHinhAnh(dto.getHinhAnh());
                }
                
                giaoVienRepository.save(giaoVien);
                dto.setMaGiaoVien(id);
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteGiaoVien(Integer id) {
        try {
            if (giaoVienRepository.existsById(id)) {
                giaoVienRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
