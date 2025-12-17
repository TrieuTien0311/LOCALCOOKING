package com.android.be.service;

import com.android.be.model.DatLich;
import com.android.be.repository.DatLichRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatLichService {
    
    private final DatLichRepository datLichRepository;
    
    public List<DatLich> getAllDatLich() {
        return datLichRepository.findAll();
    }
}
