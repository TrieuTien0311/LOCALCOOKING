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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhMucMonAnService {
    
    private final DanhMucMonAnRepository danhMucRepository;
    private final MonAnRepository monAnRepository;
    
    /**
     * Lấy danh mục món ăn theo khóa học
     * Mỗi danh mục chứa danh sách món ăn tương ứng
     */
    public List<DanhMucMonAnDTO> getDanhMucMonAnByKhoaHoc(Integer maKhoaHoc) {
        // Lấy tất cả danh mục (sắp xếp theo thứ tự)
        List<DanhMucMonAn> danhMucList = danhMucRepository.findAllByOrderByThuTuAsc();
        
        // Convert sang DTO và thêm món ăn
        return danhMucList.stream()
                .map(danhMuc -> {
                    DanhMucMonAnDTO dto = new DanhMucMonAnDTO();
                    dto.setMaDanhMuc(danhMuc.getMaDanhMuc());
                    dto.setTenDanhMuc(danhMuc.getTenDanhMuc());
                    dto.setIconDanhMuc(danhMuc.getIconDanhMuc());
                    dto.setThuTu(danhMuc.getThuTu());
                    
                    // Lấy danh sách món ăn của danh mục này trong khóa học
                    List<MonAn> monAnList = monAnRepository.findByMaKhoaHocAndMaDanhMuc(
                            maKhoaHoc, danhMuc.getMaDanhMuc());
                    
                    // Convert món ăn sang DTO
                    List<MonAnDTO> monAnDTOList = monAnList.stream()
                            .map(this::convertMonAnToDTO)
                            .collect(Collectors.toList());
                    
                    dto.setDanhSachMon(monAnDTOList);
                    
                    return dto;
                })
                .filter(dto -> dto.getDanhSachMon() != null && !dto.getDanhSachMon().isEmpty())
                .collect(Collectors.toList());
    }
    
    private MonAnDTO convertMonAnToDTO(MonAn monAn) {
        MonAnDTO dto = new MonAnDTO();
        dto.setMaMonAn(monAn.getMaMonAn());
        dto.setMaKhoaHoc(monAn.getMaKhoaHoc());
        dto.setMaDanhMuc(monAn.getMaDanhMuc());
        dto.setTenMon(monAn.getTenMon());
        dto.setGioiThieu(monAn.getGioiThieu());
        dto.setNguyenLieu(monAn.getNguyenLieu());
        dto.setHinhAnh(monAn.getHinhAnh());
        return dto;
    }
}
