package com.android.be.service;

import com.android.be.model.NguoiDung;
import com.android.be.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }
}
