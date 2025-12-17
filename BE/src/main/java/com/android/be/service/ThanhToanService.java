package com.android.be.service;

import com.android.be.model.ThanhToan;
import com.android.be.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThanhToanService {
    
    private final ThanhToanRepository thanhToanRepository;
    
    public List<ThanhToan> getAllThanhToan() {
        return thanhToanRepository.findAll();
    }
    
    public Optional<ThanhToan> getThanhToanById(Integer id) {
        return thanhToanRepository.findById(id);
    }
    
    public ThanhToan createThanhToan(ThanhToan thanhToan) {
        return thanhToanRepository.save(thanhToan);
    }
    
    public ThanhToan updateThanhToan(Integer id, ThanhToan thanhToan) {
        thanhToan.setMaThanhToan(id);
        return thanhToanRepository.save(thanhToan);
    }
    
    public void deleteThanhToan(Integer id) {
        thanhToanRepository.deleteById(id);
    }
}
