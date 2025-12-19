package com.android.be.service;

import com.android.be.model.MonAn;
import com.android.be.repository.MonAnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonAnService {
    
    private final MonAnRepository monAnRepository;
    
    public List<MonAn> getAllMonAn() {
        return monAnRepository.findAll();
    }
    
    public Optional<MonAn> getMonAnById(Integer id) {
        return monAnRepository.findById(id);
    }
    
    public MonAn createMonAn(MonAn monAn) {
        return monAnRepository.save(monAn);
    }
    
    public MonAn updateMonAn(Integer id, MonAn monAn) {
        monAn.setMaMonAn(id);
        return monAnRepository.save(monAn);
    }
    
    public void deleteMonAn(Integer id) {
        monAnRepository.deleteById(id);
    }
    
    public List<MonAn> getMonAnByKhoaHoc(Integer maKhoaHoc) {
        return monAnRepository.findByMaKhoaHoc(maKhoaHoc);
    }
    
    public List<MonAn> getMonAnByDanhMuc(Integer maDanhMuc) {
        return monAnRepository.findByMaDanhMuc(maDanhMuc);
    }
}
