package com.android.be.service;

import com.android.be.model.NguoiDung;
import com.android.be.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }
    
    public Optional<NguoiDung> getNguoiDungById(Integer id) {
        return nguoiDungRepository.findById(id);
    }
    
    public NguoiDung createNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public NguoiDung updateNguoiDung(Integer id, NguoiDung nguoiDung) {
        nguoiDung.setMaNguoiDung(id);
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public void deleteNguoiDung(Integer id) {
        nguoiDungRepository.deleteById(id);
    }
}
