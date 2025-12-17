package com.android.be.service;

import com.android.be.model.DanhMucMonAn;
import com.android.be.repository.DanhMucMonAnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhMucMonAnService {
    
    private final DanhMucMonAnRepository danhMucMonAnRepository;
    
    public List<DanhMucMonAn> getAllDanhMucMonAn() {
        return danhMucMonAnRepository.findAll();
    }
    
    public Optional<DanhMucMonAn> getDanhMucMonAnById(Integer id) {
        return danhMucMonAnRepository.findById(id);
    }
    
    public DanhMucMonAn createDanhMucMonAn(DanhMucMonAn danhMucMonAn) {
        return danhMucMonAnRepository.save(danhMucMonAn);
    }
    
    public DanhMucMonAn updateDanhMucMonAn(Integer id, DanhMucMonAn danhMucMonAn) {
        danhMucMonAn.setMaDanhMuc(id);
        return danhMucMonAnRepository.save(danhMucMonAn);
    }
    
    public void deleteDanhMucMonAn(Integer id) {
        danhMucMonAnRepository.deleteById(id);
    }
}
