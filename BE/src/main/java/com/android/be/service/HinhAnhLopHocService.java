package com.android.be.service;

import com.android.be.model.HinhAnhLopHoc;
import com.android.be.repository.HinhAnhLopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HinhAnhLopHocService {
    
    private final HinhAnhLopHocRepository hinhAnhLopHocRepository;
    
    public List<HinhAnhLopHoc> getAllHinhAnhLopHoc() {
        return hinhAnhLopHocRepository.findAll();
    }
    
    public Optional<HinhAnhLopHoc> getHinhAnhLopHocById(Integer id) {
        return hinhAnhLopHocRepository.findById(id);
    }
    
    public HinhAnhLopHoc createHinhAnhLopHoc(HinhAnhLopHoc hinhAnhLopHoc) {
        return hinhAnhLopHocRepository.save(hinhAnhLopHoc);
    }
    
    public HinhAnhLopHoc updateHinhAnhLopHoc(Integer id, HinhAnhLopHoc hinhAnhLopHoc) {
        hinhAnhLopHoc.setMaHinhAnh(id);
        return hinhAnhLopHocRepository.save(hinhAnhLopHoc);
    }
    
    public void deleteHinhAnhLopHoc(Integer id) {
        hinhAnhLopHocRepository.deleteById(id);
    }
}
