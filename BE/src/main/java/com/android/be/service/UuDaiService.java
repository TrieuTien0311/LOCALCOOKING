package com.android.be.service;

import com.android.be.dto.ApDungUuDaiRequest;
import com.android.be.dto.ApDungUuDaiResponse;
import com.android.be.dto.UuDaiDTO;
import com.android.be.mapper.UuDaiMapper;
import com.android.be.model.UuDai;
import com.android.be.repository.DatLichRepository;
import com.android.be.repository.UuDaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UuDaiService {
    
    private final UuDaiRepository uuDaiRepository;
    private final DatLichRepository datLichRepository;
    private final UuDaiMapper uuDaiMapper;
    
    public List<UuDaiDTO> getAllUuDai() {
        return uuDaiRepository.findAll().stream()
                .map(uuDaiMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UuDaiDTO> getUuDaiById(Integer id) {
        return uuDaiRepository.findById(id)
                .map(uuDaiMapper::toDTO);
    }
    
    public UuDai createUuDai(UuDai uuDai) {
        return uuDaiRepository.save(uuDai);
    }
    
    public UuDai updateUuDai(Integer id, UuDai uuDai) {
        uuDai.setMaUuDai(id);
        return uuDaiRepository.save(uuDai);
    }
    
    public void deleteUuDai(Integer id) {
        uuDaiRepository.deleteById(id);
    }
    
    /**
     * Lấy danh sách ưu đãi khả dụng cho user
     */
    public List<UuDaiDTO> getAvailableUuDaiForUser(Integer maHocVien, Integer soLuongNguoi) {
        LocalDate today = LocalDate.now();
        List<UuDai> result = new ArrayList<>();
        
        // Check nếu là đơn đầu tiên → có thể dùng mã NEWUSER
        boolean isFirstOrder = !datLichRepository.existsByMaHocVien(maHocVien);
        if (isFirstOrder) {
            List<UuDai> newUserUuDai = uuDaiRepository.findActiveByLoaiUuDai("NEWUSER", today);
            result.addAll(newUserUuDai);
        }
        
        // Check nếu số lượng >= điều kiện → có thể dùng mã GROUP
        List<UuDai> groupUuDai = uuDaiRepository.findActiveByLoaiUuDai("GROUP", today);
        for (UuDai uuDai : groupUuDai) {
            if (uuDai.getDieuKienSoLuong() != null && soLuongNguoi >= uuDai.getDieuKienSoLuong()) {
                result.add(uuDai);
            }
        }
        
        // Thêm các mã NORMAL (không có điều kiện đặc biệt)
        List<UuDai> normalUuDai = uuDaiRepository.findActiveByLoaiUuDai("NORMAL", today);
        result.addAll(normalUuDai);
        
        return result.stream()
                .map(uuDaiMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Áp dụng mã ưu đãi và tính toán giảm giá
     */
    public ApDungUuDaiResponse apDungUuDai(ApDungUuDaiRequest request) {
        ApDungUuDaiResponse response = new ApDungUuDaiResponse();
        response.setTongTienGoc(request.getTongTien());
        
        // Tìm mã ưu đãi
        Optional<UuDai> optUuDai = uuDaiRepository.findByMaCode(request.getMaCode());
        if (optUuDai.isEmpty()) {
            response.setThanhCong(false);
            response.setMessage("Mã ưu đãi không tồn tại");
            response.setTongTienSauGiam(request.getTongTien());
            response.setSoTienGiam(BigDecimal.ZERO);
            return response;
        }
        
        UuDai uuDai = optUuDai.get();
        LocalDate today = LocalDate.now();
        
        // Validate mã ưu đãi
        String validationError = validateUuDai(uuDai, request, today);
        if (validationError != null) {
            response.setThanhCong(false);
            response.setMessage(validationError);
            response.setTongTienSauGiam(request.getTongTien());
            response.setSoTienGiam(BigDecimal.ZERO);
            return response;
        }
        
        // Tính toán giảm giá
        BigDecimal soTienGiam = tinhSoTienGiam(uuDai, request.getTongTien());
        BigDecimal tongTienSauGiam = request.getTongTien().subtract(soTienGiam);
        if (tongTienSauGiam.compareTo(BigDecimal.ZERO) < 0) {
            tongTienSauGiam = BigDecimal.ZERO;
        }
        
        response.setThanhCong(true);
        response.setMessage("Áp dụng mã ưu đãi thành công");
        response.setMaUuDai(uuDai.getMaUuDai());
        response.setMaCode(uuDai.getMaCode());
        response.setTenUuDai(uuDai.getTenUuDai());
        response.setSoTienGiam(soTienGiam);
        response.setTongTienSauGiam(tongTienSauGiam);
        response.setLoaiGiam(uuDai.getLoaiGiam());
        response.setPhanTramGiam(uuDai.getGiaTriGiam() != null ? uuDai.getGiaTriGiam().doubleValue() : null);
        
        return response;
    }
    
    /**
     * Validate mã ưu đãi
     */
    private String validateUuDai(UuDai uuDai, ApDungUuDaiRequest request, LocalDate today) {
        // Check trạng thái
        if (!"Hoạt Động".equals(uuDai.getTrangThai())) {
            return "Mã ưu đãi đã ngừng hoạt động";
        }
        
        // Check thời hạn
        if (uuDai.getNgayBatDau() != null && today.isBefore(uuDai.getNgayBatDau())) {
            return "Mã ưu đãi chưa đến thời gian sử dụng";
        }
        if (uuDai.getNgayKetThuc() != null && today.isAfter(uuDai.getNgayKetThuc())) {
            return "Mã ưu đãi đã hết hạn";
        }
        
        // Check số lượng
        if (uuDai.getSoLuong() != null && uuDai.getSoLuongDaSuDung() != null 
            && uuDai.getSoLuongDaSuDung() >= uuDai.getSoLuong()) {
            return "Mã ưu đãi đã hết lượt sử dụng";
        }
        
        // Check điều kiện loại ưu đãi
        if ("NEWUSER".equals(uuDai.getLoaiUuDai())) {
            boolean hasOrder = datLichRepository.existsByMaHocVien(request.getMaHocVien());
            if (hasOrder) {
                return "Mã ưu đãi chỉ dành cho đơn hàng đầu tiên";
            }
        }
        
        if ("GROUP".equals(uuDai.getLoaiUuDai())) {
            if (uuDai.getDieuKienSoLuong() != null 
                && request.getSoLuongNguoi() < uuDai.getDieuKienSoLuong()) {
                return "Cần đặt từ " + uuDai.getDieuKienSoLuong() + " người trở lên để sử dụng mã này";
            }
        }
        
        return null; // Hợp lệ
    }
    
    /**
     * Tính số tiền giảm
     */
    private BigDecimal tinhSoTienGiam(UuDai uuDai, BigDecimal tongTien) {
        BigDecimal soTienGiam;
        
        if ("PhanTram".equals(uuDai.getLoaiGiam())) {
            // Giảm theo phần trăm
            soTienGiam = tongTien.multiply(uuDai.getGiaTriGiam())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
            
            // Áp dụng giảm tối đa nếu có
            if (uuDai.getGiamToiDa() != null && soTienGiam.compareTo(uuDai.getGiamToiDa()) > 0) {
                soTienGiam = uuDai.getGiamToiDa();
            }
        } else {
            // Giảm số tiền cố định
            soTienGiam = uuDai.getGiaTriGiam();
        }
        
        return soTienGiam;
    }
    
    /**
     * Tăng số lượng đã sử dụng khi thanh toán thành công
     */
    @Transactional
    public void tangSoLuongDaSuDung(Integer maUuDai) {
        uuDaiRepository.findById(maUuDai).ifPresent(uuDai -> {
            int current = uuDai.getSoLuongDaSuDung() != null ? uuDai.getSoLuongDaSuDung() : 0;
            uuDai.setSoLuongDaSuDung(current + 1);
            uuDaiRepository.save(uuDai);
        });
    }
}
