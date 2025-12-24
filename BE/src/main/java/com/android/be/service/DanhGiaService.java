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

        // Lấy thông tin đơn đặt lịch
        Optional<DatLich> datLichOpt = datLichRepository.findById(maDatLich);
        if (datLichOpt.isEmpty()) {
            response.setTrangThaiDanhGia("KHÔNG TÌM THẤY ĐƠN");
            response.setDaDanhGia(false);
            return response;
        }

        DatLich datLich = datLichOpt.get();
        String trangThai = datLich.getTrangThai();
        response.setTrangThaiDon(trangThai);

        // Kiểm tra đã đánh giá chưa
        Optional<DanhGia> danhGiaOpt = danhGiaRepository.findByMaDatLich(maDatLich);
        if (danhGiaOpt.isPresent()) {
            // Đã đánh giá
            DanhGia danhGia = danhGiaOpt.get();
            response.setDaDanhGia(true);
            response.setMaDanhGia(danhGia.getMaDanhGia());
            response.setTrangThaiDanhGia("ĐÃ ĐÁNH GIÁ");

            // Map DTO
            DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);

            // Load hình ảnh
            List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                    .findByMaDanhGiaOrderByThuTuAsc(danhGia.getMaDanhGia());
            if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                List<HinhAnhDanhGiaDTO> hinhAnhDTOList = hinhAnhList.stream()
                        .map(danhGiaMapper::toHinhAnhDTO)
                        .collect(Collectors.toList());
                dto.setHinhAnhList(hinhAnhDTOList);
            }
            response.setDanhGia(dto);
        } else {
            // Chưa đánh giá
            response.setDaDanhGia(false);
            boolean isHoanThanh = trangThai != null && 
                    trangThai.trim().equalsIgnoreCase(TRANG_THAI_HOAN_THANH.trim());
            if (isHoanThanh) {
                response.setTrangThaiDanhGia("CÓ THỂ ĐÁNH GIÁ");
            } else {
                response.setTrangThaiDanhGia("KHÔNG THỂ ĐÁNH GIÁ");
            }
        }
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

        // Kiểm tra trạng thái đơn
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
        LichTrinhLopHoc lichTrinh = lichTrinhLopHocRepository
                .findById(datLich.getMaLichTrinh())
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
            int thuTu = 1;
            for (String url : request.getHinhAnhUrls()) {
                if (url != null && !url.trim().isEmpty()) {
                    HinhAnhDanhGia hinhAnh = new HinhAnhDanhGia();
                    hinhAnh.setMaDanhGia(savedDanhGia.getMaDanhGia());
                    hinhAnh.setDuongDan(url);
                    hinhAnh.setLoaiFile(detectFileType(url));
                    hinhAnh.setThuTu(thuTu++);
                    hinhAnh.setNgayTao(LocalDateTime.now());
                    hinhAnhDanhGiaRepository.save(hinhAnh);
                }
            }
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
        return danhGiaRepository.findByMaKhoaHocOrderByNgayDanhGiaDesc(maKhoaHoc)
                .stream()
                .map(danhGia -> {
                    DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
                    
                    // Lấy ngày tham gia từ DatLich
                    if (danhGia.getMaDatLich() != null) {
                        datLichRepository.findById(danhGia.getMaDatLich())
                                .ifPresent(datLich -> {
                                    if (datLich.getNgayThamGia() != null) {
                                        dto.setNgayThamGia(datLich.getNgayThamGia()
                                                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                    }
                                });
                    }
                    
                    // Lấy hình ảnh
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
     * Lấy thống kê đánh giá của khóa học
     */
    public ThongKeDanhGiaDTO getThongKeDanhGia(Integer maKhoaHoc) {
        List<DanhGia> danhGiaList = danhGiaRepository.findByMaKhoaHocOrderByNgayDanhGiaDesc(maKhoaHoc);
        
        ThongKeDanhGiaDTO dto = new ThongKeDanhGiaDTO();
        dto.setMaKhoaHoc(maKhoaHoc);
        dto.setTongSoDanhGia(danhGiaList.size());
        
        if (danhGiaList.isEmpty()) {
            dto.setDiemTrungBinh(0.0);
            dto.setSoLuong1Sao(0);
            dto.setSoLuong2Sao(0);
            dto.setSoLuong3Sao(0);
            dto.setSoLuong4Sao(0);
            dto.setSoLuong5Sao(0);
            dto.setSoLuongCoNhanXet(0);
            dto.setSoLuongCoHinhAnh(0);
            return dto;
        }

        // Tính điểm trung bình
        double avg = danhGiaList.stream()
                .mapToInt(DanhGia::getDiemDanhGia)
                .average()
                .orElse(0.0);
        dto.setDiemTrungBinh(Math.round(avg * 10.0) / 10.0);

        // Đếm số lượng theo sao
        dto.setSoLuong1Sao((int) danhGiaList.stream().filter(d -> d.getDiemDanhGia() == 1).count());
        dto.setSoLuong2Sao((int) danhGiaList.stream().filter(d -> d.getDiemDanhGia() == 2).count());
        dto.setSoLuong3Sao((int) danhGiaList.stream().filter(d -> d.getDiemDanhGia() == 3).count());
        dto.setSoLuong4Sao((int) danhGiaList.stream().filter(d -> d.getDiemDanhGia() == 4).count());
        dto.setSoLuong5Sao((int) danhGiaList.stream().filter(d -> d.getDiemDanhGia() == 5).count());

        // Đếm có nhận xét
        dto.setSoLuongCoNhanXet((int) danhGiaList.stream()
                .filter(d -> d.getBinhLuan() != null && !d.getBinhLuan().trim().isEmpty())
                .count());

        // Đếm có hình ảnh
        int soLuongCoHinhAnh = 0;
        for (DanhGia dg : danhGiaList) {
            List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                    .findByMaDanhGiaOrderByThuTuAsc(dg.getMaDanhGia());
            if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                soLuongCoHinhAnh++;
            }
        }
        dto.setSoLuongCoHinhAnh(soLuongCoHinhAnh);

        return dto;
    }

    /**
     * Lấy danh sách đánh giá với filter
     */
    public List<DanhGiaDTO> getDanhGiaByKhoaHocWithFilter(Integer maKhoaHoc, String type, Integer sao) {
        List<DanhGia> danhGiaList = danhGiaRepository.findByMaKhoaHocOrderByNgayDanhGiaDesc(maKhoaHoc);

        // Filter theo số sao
        if (sao != null && sao >= 1 && sao <= 5) {
            danhGiaList = danhGiaList.stream()
                    .filter(d -> d.getDiemDanhGia().equals(sao))
                    .collect(Collectors.toList());
        }

        // Filter theo loại
        if ("co_nhan_xet".equals(type)) {
            danhGiaList = danhGiaList.stream()
                    .filter(d -> d.getBinhLuan() != null && !d.getBinhLuan().trim().isEmpty())
                    .collect(Collectors.toList());
        } else if ("co_hinh_anh".equals(type)) {
            danhGiaList = danhGiaList.stream()
                    .filter(d -> {
                        List<HinhAnhDanhGia> hinhAnhList = hinhAnhDanhGiaRepository
                                .findByMaDanhGiaOrderByThuTuAsc(d.getMaDanhGia());
                        return hinhAnhList != null && !hinhAnhList.isEmpty();
                    })
                    .collect(Collectors.toList());
        }

        return danhGiaList.stream()
                .map(danhGia -> {
                    DanhGiaDTO dto = danhGiaMapper.toDTO(danhGia);
                    
                    // Lấy ngày tham gia từ DatLich
                    if (danhGia.getMaDatLich() != null) {
                        datLichRepository.findById(danhGia.getMaDatLich())
                                .ifPresent(datLich -> {
                                    if (datLich.getNgayThamGia() != null) {
                                        dto.setNgayThamGia(datLich.getNgayThamGia()
                                                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                    }
                                });
                    }
                    
                    // Lấy hình ảnh
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
