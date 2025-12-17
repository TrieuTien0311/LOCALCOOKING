package com.android.be.service;

import com.android.be.model.DatLich;
import com.android.be.repository.DatLichRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatLichService {
    
    private final DatLichRepository datLichRepository;
    
    public List<DatLich> getAllDatLich() {
        return datLichRepository.findAll();
    }
    
    public Optional<DatLich> getDatLichById(Integer id) {
        return datLichRepository.findById(id);
    }
    
    public DatLich createDatLich(DatLich datLich) {
        return datLichRepository.save(datLich);
    }
    
    public DatLich updateDatLich(Integer id, DatLich datLich) {
        datLich.setMaDatLich(id);
        return datLichRepository.save(datLich);
    }
    
    public void deleteDatLich(Integer id) {
        datLichRepository.deleteById(id);
    }
}
