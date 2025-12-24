package com.android.be.service;

import com.android.be.model.MonAn;
import com.android.be.repository.MonAnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonAnService {
    
    private final MonAnRepository monAnRepository;
    
    public List<MonAn> getAllMonAn() {
        return monAnRepository.findAll();
    }
    
    public Optional<MonAn> getMonAnById(Integer id) {
        return monAnRepository.findById(id);
    }
    
    public MonAn createMonAn(MonAn monAn) {
        return monAnRepository.save(monAn);
    }
    
    public MonAn updateMonAn(Integer id, MonAn monAnUpdate) {
        // Lấy món ăn hiện tại từ DB
        MonAn existingMonAn = monAnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn với id: " + id));
        
        // Chỉ update các field được gửi lên (không null)
        if (monAnUpdate.getMaKhoaHoc() != null) {
            existingMonAn.setMaKhoaHoc(monAnUpdate.getMaKhoaHoc());
        }
        if (monAnUpdate.getMaDanhMuc() != null) {
            existingMonAn.setMaDanhMuc(monAnUpdate.getMaDanhMuc());
        }
        if (monAnUpdate.getTenMon() != null) {
            existingMonAn.setTenMon(monAnUpdate.getTenMon());
        }
        if (monAnUpdate.getGioiThieu() != null) {
            existingMonAn.setGioiThieu(monAnUpdate.getGioiThieu());
        }
        if (monAnUpdate.getNguyenLieu() != null) {
            existingMonAn.setNguyenLieu(monAnUpdate.getNguyenLieu());
        }
        if (monAnUpdate.getHinhAnh() != null) {
            existingMonAn.setHinhAnh(monAnUpdate.getHinhAnh());
        }
        
        return monAnRepository.save(existingMonAn);
    }
    
    public void deleteMonAn(Integer id) {
        monAnRepository.deleteById(id);
    }
    
    public List<MonAn> getMonAnByKhoaHoc(Integer maKhoaHoc) {
        return monAnRepository.findByMaKhoaHoc(maKhoaHoc);
    }
    
    public List<MonAn> getMonAnByDanhMuc(Integer maDanhMuc) {
        return monAnRepository.findByMaDanhMuc(maDanhMuc);
    }
}
