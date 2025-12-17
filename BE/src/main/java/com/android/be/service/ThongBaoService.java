package com.android.be.service;

import com.android.be.model.ThongBao;
import com.android.be.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThongBaoService {
    
    private final ThongBaoRepository thongBaoRepository;
    
    public List<ThongBao> getAllThongBao() {
        return thongBaoRepository.findAll();
    }
}
