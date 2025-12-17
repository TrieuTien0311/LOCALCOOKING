package com.android.be.service;

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
    
    public List<GiaoVien> getAllGiaoVien() {
        return giaoVienRepository.findAll();
    }
    
    public Optional<GiaoVien> getGiaoVienById(Integer id) {
        return giaoVienRepository.findById(id);
    }
    
    public GiaoVien createGiaoVien(GiaoVien giaoVien) {
        return giaoVienRepository.save(giaoVien);
    }
    
    public GiaoVien updateGiaoVien(Integer id, GiaoVien giaoVien) {
        giaoVien.setMaGiaoVien(id);
        return giaoVienRepository.save(giaoVien);
    }
    
    public void deleteGiaoVien(Integer id) {
        giaoVienRepository.deleteById(id);
    }
}
