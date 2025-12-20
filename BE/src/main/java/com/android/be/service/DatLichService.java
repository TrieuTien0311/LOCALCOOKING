package com.android.be.service;

import com.android.be.model.DatLich;
import com.android.be.model.LichTrinhLopHoc;
import com.android.be.repository.DatLichRepository;
import com.android.be.repository.LichTrinhLopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatLichService {
    
    private final DatLichRepository datLichRepository;
    private final LichTrinhLopHocRepository lichTrinhRepository;
    
    // GET - Lấy tất cả đặt lịch
    public List<DatLich> getAllDatLich() {
        return datLichRepository.findAll();
    }
    
    // GET - Lấy đặt lịch theo ID
    public Optional<DatLich> getDatLichById(Integer id) {
        return datLichRepository.findById(id);
    }
    
    // GET - Lấy đặt lịch theo học viên
    public List<DatLich> getDatLichByHocVien(Integer maHocVien) {
        return datLichRepository.findByMaHocVien(maHocVien);
    }
    
    // GET - Lấy đặt lịch theo lịch trình
    public List<DatLich> getDatLichByLichTrinh(Integer maLichTrinh) {
        return datLichRepository.findByMaLichTrinh(maLichTrinh);
    }
    
    // GET - Lấy đặt lịch theo trạng thái
    public List<DatLich> getDatLichByTrangThai(String trangThai) {
        return datLichRepository.findByTrangThai(trangThai);
    }
    
    // GET - Lấy đặt lịch theo học viên và trạng thái
    public List<DatLich> getDatLichByHocVienAndTrangThai(Integer maHocVien, String trangThai) {
        return datLichRepository.findByMaHocVienAndTrangThai(maHocVien, trangThai);
    }
    
    // POST - Tạo đặt lịch mới (có kiểm tra chỗ trống)
    public DatLich createDatLich(DatLich datLich) {
        // Kiểm tra lịch trình có tồn tại không
        LichTrinhLopHoc lichTrinh = lichTrinhRepository.findById(datLich.getMaLichTrinh())
                .orElseThrow(() -> new RuntimeException("Lịch trình không tồn tại"));
        
        // Kiểm tra lịch trình có đang hoạt động không
        if (!lichTrinh.getTrangThai()) {
            throw new RuntimeException("Lịch trình này đã ngừng hoạt động");
        }
        
        // Đếm số người đã đặt
        Integer soNguoiDaDat = datLichRepository.countBookedSeats(
            datLich.getMaLichTrinh(), 
            datLich.getNgayThamGia()
        );
        
        // Kiểm tra còn chỗ trống không
        int soChoConLai = lichTrinh.getSoLuongToiDa() - soNguoiDaDat;
        if (soChoConLai < datLich.getSoLuongNguoi()) {
            throw new RuntimeException("Không đủ chỗ trống. Chỉ còn " + soChoConLai + " chỗ");
        }
        
        // Lưu đặt lịch
        return datLichRepository.save(datLich);
    }
    
    // PUT - Cập nhật đặt lịch
    public DatLich updateDatLich(Integer id, DatLich datLich) {
        DatLich existing = datLichRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đặt lịch không tồn tại"));
        
        // Nếu thay đổi số lượng người, cần kiểm tra lại chỗ trống
        if (!existing.getSoLuongNguoi().equals(datLich.getSoLuongNguoi()) ||
            !existing.getMaLichTrinh().equals(datLich.getMaLichTrinh()) ||
            !existing.getNgayThamGia().equals(datLich.getNgayThamGia())) {
            
            LichTrinhLopHoc lichTrinh = lichTrinhRepository.findById(datLich.getMaLichTrinh())
                    .orElseThrow(() -> new RuntimeException("Lịch trình không tồn tại"));
            
            Integer soNguoiDaDat = datLichRepository.countBookedSeats(
                datLich.getMaLichTrinh(), 
                datLich.getNgayThamGia()
            );
            
            // Trừ đi số người của đơn hiện tại (vì đang cập nhật)
            soNguoiDaDat -= existing.getSoLuongNguoi();
            
            int soChoConLai = lichTrinh.getSoLuongToiDa() - soNguoiDaDat;
            if (soChoConLai < datLich.getSoLuongNguoi()) {
                throw new RuntimeException("Không đủ chỗ trống. Chỉ còn " + soChoConLai + " chỗ");
            }
        }
        
        datLich.setMaDatLich(id);
        return datLichRepository.save(datLich);
    }
    
    // DELETE - Xóa đặt lịch (hoặc hủy)
    public void deleteDatLich(Integer id) {
        datLichRepository.deleteById(id);
    }
    
    // PUT - Hủy đặt lịch (đổi trạng thái thay vì xóa)
    public DatLich cancelDatLich(Integer id) {
        DatLich datLich = datLichRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đặt lịch không tồn tại"));
        
        datLich.setTrangThai("Đã Hủy");
        return datLichRepository.save(datLich);
    }
    
    // GET - Kiểm tra số chỗ trống
    public Integer getAvailableSeats(Integer maLichTrinh, LocalDate ngayThamGia) {
        LichTrinhLopHoc lichTrinh = lichTrinhRepository.findById(maLichTrinh)
                .orElseThrow(() -> new RuntimeException("Lịch trình không tồn tại"));
        
        Integer soNguoiDaDat = datLichRepository.countBookedSeats(maLichTrinh, ngayThamGia);
        
        return lichTrinh.getSoLuongToiDa() - soNguoiDaDat;
    }
}
