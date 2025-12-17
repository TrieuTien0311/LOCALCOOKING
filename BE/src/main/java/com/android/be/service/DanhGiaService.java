package com.android.be.service;

import com.android.be.dto.DanhGiaDTO;
import com.android.be.mapper.DanhGiaMapper;
import com.android.be.model.DanhGia;
import com.android.be.repository.DanhGiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhGiaService {
    
    private final DanhGiaRepository danhGiaRepository;
    private final DanhGiaMapper danhGiaMapper;
    
    public List<DanhGiaDTO> getAllDanhGia() {
        return danhGiaRepository.findAll().stream()
                .map(danhGiaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DanhGiaDTO> getDanhGiaById(Integer id) {
        return danhGiaRepository.findById(id)
                .map(danhGiaMapper::toDTO);
    }
    
    public DanhGia createDanhGia(DanhGia danhGia) {
        return danhGiaRepository.save(danhGia);
    }
    
    public DanhGia updateDanhGia(Integer id, DanhGia danhGia) {
        danhGia.setMaDanhGia(id);
        return danhGiaRepository.save(danhGia);
    }
    
    public void deleteDanhGia(Integer id) {
        danhGiaRepository.deleteById(id);
    }
}
