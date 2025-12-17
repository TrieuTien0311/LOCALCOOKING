package com.android.be.service;

import com.android.be.model.LichHoc;
import com.android.be.repository.LichHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LichHocService {
    
    private final LichHocRepository lichHocRepository;
    
    public List<LichHoc> getAllLichHoc() {
        return lichHocRepository.findAll();
    }
    
    public Optional<LichHoc> getLichHocById(Integer id) {
        return lichHocRepository.findById(id);
    }
    
    public LichHoc createLichHoc(LichHoc lichHoc) {
        return lichHocRepository.save(lichHoc);
    }
    
    public LichHoc updateLichHoc(Integer id, LichHoc lichHoc) {
        lichHoc.setMaLichHoc(id);
        return lichHocRepository.save(lichHoc);
    }
    
    public void deleteLichHoc(Integer id) {
        lichHocRepository.deleteById(id);
    }
}
