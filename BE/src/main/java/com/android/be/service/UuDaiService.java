package com.android.be.service;

import com.android.be.model.UuDai;
import com.android.be.repository.UuDaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UuDaiService {
    
    private final UuDaiRepository uuDaiRepository;
    
    public List<UuDai> getAllUuDai() {
        return uuDaiRepository.findAll();
    }
}
