package com.android.be.service;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.dto.LopHocDTO;
import com.android.be.dto.MonAnDTO;
import com.android.be.mapper.LopHocMapper;
import com.android.be.mapper.MonAnMapper;
import com.android.be.model.LopHoc;
import com.android.be.repository.DanhGiaRepository;
import com.android.be.repository.DanhMucMonAnRepository;
import com.android.be.repository.GiaoVienRepository;
import com.android.be.repository.LopHocRepository;
import com.android.be.repository.MonAnRepository;
import com.android.be.repository.NguoiDungRepository;
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
    private final GiaoVienRepository giaoVienRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final LopHocMapper lopHocMapper;
    private final MonAnMapper monAnMapper;
    
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
    
    public List<LopHocDTO> searchLopHocByDiaDiem(String diaDiem) {
        java.time.LocalDate ngayHienTai = java.time.LocalDate.now();
        
        // Tìm các lớp học theo địa điểm và còn trong thời gian hoạt động
        List<LopHoc> lopHocs = lopHocRepository.findByDiaDiemContainingIgnoreCase(diaDiem);
        
        // Lọc các lớp học còn hiệu lực (ngayBatDau <= ngayHienTai và (ngayKetThuc >= ngayHienTai hoặc ngayKetThuc = null))
        return lopHocs.stream()
                .filter(lh -> {
                    boolean batDauOk = lh.getNgayBatDau() == null || !lh.getNgayBatDau().isAfter(ngayHienTai);
                    boolean ketThucOk = lh.getNgayKetThuc() == null || !lh.getNgayKetThuc().isBefore(ngayHienTai);
                    return batDauOk && ketThucOk;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<LopHocDTO> searchLopHocByDiaDiemAndDate(String diaDiem, java.time.LocalDate ngayTimKiem) {
        // Tìm các lớp học theo địa điểm và ngày cụ thể
        List<LopHoc> lopHocs = lopHocRepository.findByDiaDiemContainingIgnoreCase(diaDiem);
        
        // Lọc các lớp học có lịch vào ngày tìm kiếm
        return lopHocs.stream()
                .filter(lh -> {
                    // Kiểm tra ngày tìm kiếm có nằm trong khoảng thời gian của lớp học không
                    boolean trongKhoangThoiGian = !lh.getNgayBatDau().isAfter(ngayTimKiem) && 
                                                  (lh.getNgayKetThuc() == null || !lh.getNgayKetThuc().isBefore(ngayTimKiem));
                    
                    if (!trongKhoangThoiGian) return false;
                    
                    // Kiểm tra loại lịch
                    if ("MotLan".equals(lh.getLoaiLich())) {
                        // Lớp học một lần: chỉ diễn ra vào ngày bắt đầu
                        return lh.getNgayBatDau().equals(ngayTimKiem);
                    } else if ("HangNgay".equals(lh.getLoaiLich())) {
                        // Lớp học hàng ngày: diễn ra mọi ngày trong khoảng thời gian
                        return true;
                    } else if ("HangTuan".equals(lh.getLoaiLich())) {
                        // Lớp học hàng tuần: kiểm tra ngày trong tuần
                        if (lh.getCacNgayTrongTuan() == null || lh.getCacNgayTrongTuan().isEmpty()) {
                            return false;
                        }
                        
                        // Lấy thứ trong tuần (1=CN, 2=T2, ..., 7=T7)
                        int dayOfWeek = ngayTimKiem.getDayOfWeek().getValue();
                        if (dayOfWeek == 7) dayOfWeek = 1; // Chủ nhật = 1
                        else dayOfWeek++; // Thứ 2-7 = 2-7
                        
                        String[] cacNgay = lh.getCacNgayTrongTuan().split(",");
                        for (String ngay : cacNgay) {
                            if (Integer.parseInt(ngay.trim()) == dayOfWeek) {
                                return true;
                            }
                        }
                        return false;
                    }
                    
                    return false;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
