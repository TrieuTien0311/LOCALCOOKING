package com.android.be.service;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.mapper.ThongBaoMapper;
import com.android.be.model.ThongBao;
import com.android.be.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongBaoService {
    
    private final ThongBaoRepository thongBaoRepository;
    private final ThongBaoMapper thongBaoMapper;
    
    // Lấy tất cả thông báo
    public List<ThongBaoDTO> getAllThongBao() {
        return thongBaoRepository.findAll().stream()
                .map(thongBaoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Lấy thông báo theo ID
    public Optional<ThongBaoDTO> getThongBaoById(Integer id) {
        return thongBaoRepository.findById(id)
                .map(thongBaoMapper::toDTO);
    }
    
    // Lấy thông báo theo người dùng
    public List<ThongBaoDTO> getThongBaoByUser(Integer maNguoiNhan) {
        return thongBaoRepository.findByMaNguoiNhanOrderByNgayTaoDesc(maNguoiNhan).stream()
                .map(thongBaoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Lấy thông báo chưa đọc
    public List<ThongBaoDTO> getUnreadThongBao(Integer maNguoiNhan) {
        return thongBaoRepository.findByMaNguoiNhanAndDaDocOrderByNgayTaoDesc(maNguoiNhan, false).stream()
                .map(thongBaoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Đếm số thông báo chưa đọc
    public Long countUnreadThongBao(Integer maNguoiNhan) {
        return thongBaoRepository.countUnreadByUser(maNguoiNhan);
    }
    
    // Lấy thông báo theo loại
    public List<ThongBaoDTO> getThongBaoByType(Integer maNguoiNhan, String loaiThongBao) {
        return thongBaoRepository.findByMaNguoiNhanAndLoaiThongBaoOrderByNgayTaoDesc(maNguoiNhan, loaiThongBao).stream()
                .map(thongBaoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Tạo thông báo mới
    public ThongBao createThongBao(ThongBao thongBao) {
        if (thongBao.getNgayTao() == null) {
            thongBao.setNgayTao(LocalDateTime.now());
        }
        if (thongBao.getDaDoc() == null) {
            thongBao.setDaDoc(false);
        }
        return thongBaoRepository.save(thongBao);
    }
    
    // Cập nhật thông báo
    public ThongBao updateThongBao(Integer id, ThongBao thongBao) {
        thongBao.setMaThongBao(id);
        return thongBaoRepository.save(thongBao);
    }
    
    // Đánh dấu đã đọc
    public ThongBao markAsRead(Integer id) {
        Optional<ThongBao> thongBaoOpt = thongBaoRepository.findById(id);
        if (thongBaoOpt.isPresent()) {
            ThongBao thongBao = thongBaoOpt.get();
            thongBao.setDaDoc(true);
            return thongBaoRepository.save(thongBao);
        }
        throw new RuntimeException("Không tìm thấy thông báo với ID: " + id);
    }
    
    // Đánh dấu tất cả đã đọc
    public void markAllAsRead(Integer maNguoiNhan) {
        List<ThongBao> unreadList = thongBaoRepository.findByMaNguoiNhanAndDaDocOrderByNgayTaoDesc(maNguoiNhan, false);
        unreadList.forEach(tb -> tb.setDaDoc(true));
        thongBaoRepository.saveAll(unreadList);
    }
    
    // Xóa thông báo
    public void deleteThongBao(Integer id) {
        thongBaoRepository.deleteById(id);
    }
    
    // Xóa tất cả thông báo đã đọc
    public void deleteAllReadNotifications(Integer maNguoiNhan) {
        List<ThongBao> readList = thongBaoRepository.findByMaNguoiNhanAndDaDocOrderByNgayTaoDesc(maNguoiNhan, true);
        thongBaoRepository.deleteAll(readList);
    }
}
