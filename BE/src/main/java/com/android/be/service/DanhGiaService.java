package com.android.be.service;

import com.android.be.dto.*;
import com.android.be.mapper.DanhGiaMapper;
import com.android.be.model.DanhGia;
import com.android.be.model.DatLich;
import com.android.be.model.HinhAnhDanhGia;
import com.android.be.model.LichTrinhLopHoc;
import com.android.be.repository.DanhGiaRepository;
import com.android.be.repository.DatLichRepository;
import com.android.be.repository.HinhAnhDanhGiaRepository;
import com.android.be.repository.LichTrinhLopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhGiaService {
    
    private final DanhGiaRepository danhGiaRepository;
    private final HinhAnhDanhGiaRepository hinhAnhDanhGiaRepository;
    private final DatLichRepository datLichRepository;
    private final LichTrinhLopHocRepository lichTrinhLopHocRepository;
    private final DanhGiaMapper danhGiaMapper;
    
    private static final String TRANG_THAI_HOAN_THANH = "Đã hoàn thành";
    
    /**
     * Kiểm tra trạng thái đánh giá của đơn đặt lịch
     */
    public KiemTraDanhGiaResponse kiemTraDaDanhGia(Integer maDatLich) {
        KiemTraDanhGiaResponse response = new KiemTraDanhGiaResponse();
        response.setMaDatLich(maDatLich);
        
        System.out.println("=== KIỂM TRA ĐÁNH GIÁ ===");
        System.out.println("maDatLich: " + maDatLich);
        
        // Lấy thông tin đơn đặt lịch
        Optional<DatLich> datLichOpt = datLichRepository.findById(maDatLich);
        if (datLichOpt.isEmpty()) {
            System.out.println("Không tìm thấy đơn đặt lịch với maDatLich: " + maDatLich);
            response.setTrangThaiDanhGia("KHÔNG TÌM THẤY ĐƠN");
            response.setDaDanhGia(false);
            return response;
        }
        
        DatLich datLich = datLichOpt.get();
        String trangThai = datLich.getTrangThai();
        System.out.println("Trạng thái đơn: [" + trangThai + "]");
        System.out.println("Trạng thái cần so sánh: [" + TRANG_THAI_HOAN_THANH + "]");
        System.out.println("So sánh equals: " + TRANG_THAI_HOAN_THANH.equals(trangThai));
        System.out.println("So sánh trim: " + TRANG_THAI_HOAN_THANH.equals(trangThai != null ? trangThai.trim() : null));
        
        response.setTrangThaiDon(trangThai);
        
        // Kiểm tra đã đánh giá chưa
        Optional<DanhGia> danhGiaOpt = danhGiaRepository.findByMaDatLich(maDatLich);
        
        if (danhGiaOpt.isPresent()) {
            // Đã đánh giá
            DanhGia danhGia = danhGiaOpt.get();
            response.setDaDanhGia(true);
            response.setMaDanhGia(danhGia.getMaDanhGia());
            response.setTrangThaiDanhGia("ĐÃ ĐÁNH GIÁ");
            
            // Map DTO (không có hình ảnh vì LAZY)
            DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
            
            // Load hình ảnh riêng từ repository
            List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                    .findByMaDanhGiaOrderByThuTuAsc(danhGia.getMaDanhGia());
            
            System.out.println("Số hình ảnh tìm được: " + (hinhAnhList != null ? hinhAnhList.size() : 0));
            
            if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                List<HinhAnhDanhGiaDTO> hinhAnhDTOList = hinhAnhList.stream()
                        .map(img -> {
                            HinhAnhDanhGiaDTO imgDTO = danhGiaMapper.toHinhAnhDTO(img);
                            System.out.println("Hình ảnh: " + imgDTO.getDuongDan());
                            return imgDTO;
                        })
                        .collect(java.util.stream.Collectors.toList());
                dto.setHinhAnhList(hinhAnhDTOList);
            }
            response.setDanhGia(dto);
        } else {
            // Chưa đánh giá
            response.setDaDanhGia(false);
            
            // So sánh linh hoạt hơn (trim và ignore case)
            boolean isHoanThanh = trangThai != null && 
                    trangThai.trim().equalsIgnoreCase(TRANG_THAI_HOAN_THANH.trim());
            
            System.out.println("isHoanThanh: " + isHoanThanh);
            
            if (isHoanThanh) {
                response.setTrangThaiDanhGia("CÓ THỂ ĐÁNH GIÁ");
            } else {
                response.setTrangThaiDanhGia("KHÔNG THỂ ĐÁNH GIÁ");
            }
        }
        
        System.out.println("Kết quả: " + response.getTrangThaiDanhGia());
        System.out.println("=========================");
        
        return response;
    }
    
    /**
     * Tạo đánh giá mới
     */
    @Transactional
    public DanhGiaDTO taoDanhGia(TaoDanhGiaRequest request) {
        // Kiểm tra đơn đặt lịch
        DatLich datLich = datLichRepository.findById(request.getMaDatLich())
                .orElseThrow(() -> new RuntimeException("Đơn đặt lịch không tồn tại"));
        
        // Kiểm tra trạng thái đơn (so sánh linh hoạt)
        String trangThai = datLich.getTrangThai();
        boolean isHoanThanh = trangThai != null && 
                trangThai.trim().equalsIgnoreCase(TRANG_THAI_HOAN_THANH.trim());
        
        if (!isHoanThanh) {
            throw new RuntimeException("Chỉ được đánh giá khi đơn đã hoàn thành");
        }
        
        // Kiểm tra đã đánh giá chưa
        if (danhGiaRepository.existsByMaDatLich(request.getMaDatLich())) {
            throw new RuntimeException("Đơn này đã được đánh giá");
        }
        
        // Lấy maKhoaHoc từ lịch trình
        LichTrinhLopHoc lichTrinh = lichTrinhLopHocRepository.findById(datLich.getMaLichTrinh())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch trình"));
        
        // Tạo đánh giá
        DanhGia danhGia = new DanhGia();
        danhGia.setMaHocVien(datLich.getMaHocVien());
        danhGia.setMaKhoaHoc(lichTrinh.getMaKhoaHoc());
        danhGia.setMaDatLich(request.getMaDatLich());
        danhGia.setDiemDanhGia(request.getDiemDanhGia());
        danhGia.setBinhLuan(request.getBinhLuan());
        danhGia.setNgayDanhGia(LocalDateTime.now());
        
        DanhGia savedDanhGia = danhGiaRepository.save(danhGia);
        
        // Lưu hình ảnh nếu có
        if (request.getHinhAnhUrls() != null && !request.getHinhAnhUrls().isEmpty()) {
            System.out.println("=== LƯU HÌNH ẢNH ĐÁNH GIÁ ===");
            System.out.println("Số URL ảnh nhận được: " + request.getHinhAnhUrls().size());
            
            int thuTu = 1;
            for (String url : request.getHinhAnhUrls()) {
                System.out.println("URL " + thuTu + ": " + url);
                if (url != null && !url.trim().isEmpty()) {
                    HinhAnhDanhGia hinhAnh = new HinhAnhDanhGia();
                    hinhAnh.setMaDanhGia(savedDanhGia.getMaDanhGia());
                    hinhAnh.setDuongDan(url);
                    hinhAnh.setLoaiFile(detectFileType(url));
                    hinhAnh.setThuTu(thuTu++);
                    hinhAnh.setNgayTao(LocalDateTime.now());
                    HinhAnhDanhGia saved = hinhAnhDanhGiaRepository.save(hinhAnh);
                    System.out.println("Đã lưu hình ảnh ID: " + saved.getMaHinhAnh());
                }
            }
            System.out.println("=========================");
        } else {
            System.out.println("Không có hình ảnh để lưu");
        }
        
        return danhGiaMapper.toDTO(savedDanhGia);
    }
    
    /**
     * Lấy đánh giá theo mã đặt lịch
     */
    public Optional<DanhGiaDTO> getDanhGiaByMaDatLich(Integer maDatLich) {
        return danhGiaRepository.findByMaDatLich(maDatLich)
                .map(danhGia -> {
                    DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
                    // Load hình ảnh
                    List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                            .findByMaDanhGiaOrderByThuTuAsc(danhGia.getMaDanhGia());
                    dto.setHinhAnhList(hinhAnhList.stream()
                            .map(danhGiaMapper::toHinhAnhDTO)
                            .collect(Collectors.toList()));
                    return dto;
                });
    }
    
    /**
     * Lấy danh sách đánh giá theo khóa học
     */
    public List<DanhGiaDTO> getDanhGiaByKhoaHoc(Integer maKhoaHoc) {
        return danhGiaRepository.findByMaKhoaHocOrderByNgayDanhGiaDesc(maKhoaHoc).stream()
                .map(danhGia -> {
                    DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
                    List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                            .findByMaDanhGiaOrderByThuTuAsc(danhGia.getMaDanhGia());
                    dto.setHinhAnhList(hinhAnhList.stream()
                            .map(danhGiaMapper::toHinhAnhDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy tất cả đánh giá
     */
    public List<DanhGiaDTO> getAllDanhGia() {
        return danhGiaRepository.findAll().stream()
                .map(danhGiaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy đánh giá theo ID
     */
    public Optional<DanhGiaDTO> getDanhGiaById(Integer id) {
        return danhGiaRepository.findById(id)
                .map(danhGia -> {
                    DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
                    List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                            .findByMaDanhGiaOrderByThuTuAsc(danhGia.getMaDanhGia());
                    dto.setHinhAnhList(hinhAnhList.stream()
                            .map(danhGiaMapper::toHinhAnhDTO)
                            .collect(Collectors.toList()));
                    return dto;
                });
    }
    
    /**
     * Xóa đánh giá
     */
    @Transactional
    public void deleteDanhGia(Integer id) {
        danhGiaRepository.deleteById(id);
    }
    
    /**
     * Detect loại file từ URL
     */
    private String detectFileType(String url) {
        if (url == null) return "image";
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".mov") || 
            lowerUrl.endsWith(".avi") || lowerUrl.endsWith(".webm") ||
            lowerUrl.contains("video")) {
            return "video";
        }
        return "image";
    }
}
