package com.android.be.service;

import com.android.be.model.LopHoc;
import com.android.be.repository.LopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LopHocService {
    
    private final LopHocRepository lopHocRepository;
    
    public List<LopHoc> getAllLopHoc() {
        return lopHocRepository.findAll();
    }
}
