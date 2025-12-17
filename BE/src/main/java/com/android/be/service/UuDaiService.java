package com.android.be.service;

import com.android.be.dto.UuDaiDTO;
import com.android.be.mapper.UuDaiMapper;
import com.android.be.model.UuDai;
import com.android.be.repository.UuDaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UuDaiService {
    
    private final UuDaiRepository uuDaiRepository;
    private final UuDaiMapper uuDaiMapper;
    
    public List<UuDaiDTO> getAllUuDai() {
        return uuDaiRepository.findAll().stream()
                .map(uuDaiMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UuDaiDTO> getUuDaiById(Integer id) {
        return uuDaiRepository.findById(id)
                .map(uuDaiMapper::toDTO);
    }
    
    public UuDai createUuDai(UuDai uuDai) {
        return uuDaiRepository.save(uuDai);
    }
    
    public UuDai updateUuDai(Integer id, UuDai uuDai) {
        uuDai.setMaUuDai(id);
        return uuDaiRepository.save(uuDai);
    }
    
    public void deleteUuDai(Integer id) {
        uuDaiRepository.deleteById(id);
    }
}
