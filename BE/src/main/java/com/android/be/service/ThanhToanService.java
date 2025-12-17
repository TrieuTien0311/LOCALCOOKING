package com.android.be.service;

import com.android.be.model.ThanhToan;
import com.android.be.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThanhToanService {
    
    private final ThanhToanRepository thanhToanRepository;
    
    public List<ThanhToan> getAllThanhToan() {
        return thanhToanRepository.findAll();
    }
}
