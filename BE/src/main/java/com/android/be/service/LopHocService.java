package com.android.be.service;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.dto.LopHocDTO;
import com.android.be.dto.MonAnDTO;
import com.android.be.mapper.LopHocMapper;
import com.android.be.mapper.MonAnMapper;
import com.android.be.model.LopHoc;
import com.android.be.repository.DanhGiaRepository;
import com.android.be.repository.DanhMucMonAnRepository;
import com.android.be.repository.LopHocRepository;
import com.android.be.repository.MonAnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LopHocService {
    
    private final LopHocRepository lopHocRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final DanhMucMonAnRepository danhMucMonAnRepository;
    private final MonAnRepository monAnRepository;
    private final LopHocMapper lopHocMapper;
    private final MonAnMapper monAnMapper;
    private final com.android.be.repository.GiaoVienRepository giaoVienRepository;
    private final com.android.be.repository.NguoiDungRepository nguoiDungRepository;
    
    public List<LopHocDTO> getAllLopHoc() {
        return lopHocRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<LopHocDTO> getLopHocById(Integer id) {
        return lopHocRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public LopHoc createLopHoc(LopHoc lopHoc) {
        return lopHocRepository.save(lopHoc);
    }
    
    public LopHoc updateLopHoc(Integer id, LopHoc lopHoc) {
        lopHoc.setMaLopHoc(id);
        return lopHocRepository.save(lopHoc);
    }
    
    public void deleteLopHoc(Integer id) {
        lopHocRepository.deleteById(id);
    }
    
    private LopHocDTO convertToDTO(LopHoc lopHoc) {
        LopHocDTO dto = lopHocMapper.toDTO(lopHoc);
        
        // Lấy tên giáo viên từ bảng GiaoVien và NguoiDung
        if (lopHoc.getMaGiaoVien() != null) {
            giaoVienRepository.findById(lopHoc.getMaGiaoVien()).ifPresent(giaoVien -> {
                nguoiDungRepository.findById(giaoVien.getMaNguoiDung()).ifPresent(nguoiDung -> {
                    dto.setTenGiaoVien(nguoiDung.getHoTen());
                });
            });
        }
        
        // Đánh giá đã được tính tự động bởi trigger trong database
        // Không cần tính lại ở đây
        
        // Load lịch trình lớp học (DanhMucMonAn + MonAn)
        // Lấy tất cả món ăn của lớp học này
        List<MonAnDTO> monAnsOfLopHoc = monAnRepository.findByMaLopHoc(lopHoc.getMaLopHoc()).stream()
                .map(monAnMapper::toDTO)
                .collect(Collectors.toList());
        
        // Nhóm món ăn theo danh mục
        List<DanhMucMonAnDTO> lichTrinh = danhMucMonAnRepository.findAll().stream()
                .map(danhMuc -> {
                    DanhMucMonAnDTO dmDTO = new DanhMucMonAnDTO();
                    dmDTO.setMaDanhMuc(danhMuc.getMaDanhMuc());
                    dmDTO.setTenDanhMuc(danhMuc.getTenDanhMuc());
                    dmDTO.setIconDanhMuc(danhMuc.getIconDanhMuc());
                    dmDTO.setThuTu(danhMuc.getThuTu());
                    
                    // Lọc món ăn thuộc danh mục này
                    List<MonAnDTO> monAns = monAnsOfLopHoc.stream()
                            .filter(ma -> ma.getMaDanhMuc().equals(danhMuc.getMaDanhMuc()))
                            .collect(Collectors.toList());
                    
                    dmDTO.setDanhSachMon(monAns);
                    return dmDTO;
                })
                .filter(dm -> !dm.getDanhSachMon().isEmpty()) // Chỉ lấy danh mục có món ăn
                .sorted((a, b) -> Integer.compare(a.getThuTu(), b.getThuTu()))
                .collect(Collectors.toList());
        
        dto.setLichTrinhLopHoc(lichTrinh);
        
        return dto;
    }
}
