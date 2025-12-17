package com.android.be.service;

import com.android.be.model.DanhGia;
import com.android.be.repository.DanhGiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhGiaService {
    
    private final DanhGiaRepository danhGiaRepository;
    
    public List<DanhGia> getAllDanhGia() {
        return danhGiaRepository.findAll();
    }
}
