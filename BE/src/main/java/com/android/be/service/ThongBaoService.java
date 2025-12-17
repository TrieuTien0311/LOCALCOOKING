package com.android.be.service;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.mapper.ThongBaoMapper;
import com.android.be.model.ThongBao;
import com.android.be.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongBaoService {
    
    private final ThongBaoRepository thongBaoRepository;
    private final ThongBaoMapper thongBaoMapper;
    
    public List<ThongBaoDTO> getAllThongBao() {
        return thongBaoRepository.findAll().stream()
                .map(thongBaoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<ThongBaoDTO> getThongBaoById(Integer id) {
        return thongBaoRepository.findById(id)
                .map(thongBaoMapper::toDTO);
    }
    
    public ThongBao createThongBao(ThongBao thongBao) {
        return thongBaoRepository.save(thongBao);
    }
    
    public ThongBao updateThongBao(Integer id, ThongBao thongBao) {
        thongBao.setMaThongBao(id);
        return thongBaoRepository.save(thongBao);
    }
    
    public void deleteThongBao(Integer id) {
        thongBaoRepository.deleteById(id);
    }
}
