package com.android.be.service;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.dto.MonAnDTO;
import com.android.be.model.DanhMucMonAn;
import com.android.be.model.MonAn;
import com.android.be.repository.DanhMucMonAnRepository;
import com.android.be.repository.MonAnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhMucMonAnService {
    
    private final DanhMucMonAnRepository danhMucMonAnRepository;
    private final MonAnRepository monAnRepository;
    
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
    
    public List<DanhMucMonAnDTO> getDanhMucMonAnByLopHoc(Integer maLopHoc) {
        List<DanhMucMonAn> danhMucList = danhMucMonAnRepository.findAll();
        
        return danhMucList.stream().map(danhMuc -> {
            DanhMucMonAnDTO dto = new DanhMucMonAnDTO();
            dto.setMaDanhMuc(danhMuc.getMaDanhMuc());
            dto.setTenDanhMuc(danhMuc.getTenDanhMuc());
            dto.setIconDanhMuc(danhMuc.getIconDanhMuc());
            dto.setThuTu(danhMuc.getThuTu());
            
            // Lấy danh sách món ăn theo lớp học và danh mục
            List<MonAn> monAnList = monAnRepository.findByMaLopHocAndMaDanhMuc(maLopHoc, danhMuc.getMaDanhMuc());
            List<MonAnDTO> monAnDTOList = monAnList.stream().map(monAn -> {
                MonAnDTO monAnDTO = new MonAnDTO();
                monAnDTO.setMaMonAn(monAn.getMaMonAn());
                monAnDTO.setMaLopHoc(monAn.getMaLopHoc());
                monAnDTO.setMaDanhMuc(monAn.getMaDanhMuc());
                monAnDTO.setTenMon(monAn.getTenMon());
                monAnDTO.setGioiThieu(monAn.getGioiThieu());
                monAnDTO.setNguyenLieu(monAn.getNguyenLieu());
                return monAnDTO;
            }).collect(Collectors.toList());
            
            dto.setDanhSachMon(monAnDTOList);
            return dto;
        }).filter(dto -> !dto.getDanhSachMon().isEmpty()) // Chỉ lấy danh mục có món ăn
        .collect(Collectors.toList());
    }
}
