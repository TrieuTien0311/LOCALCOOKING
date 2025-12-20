package com.android.be.service;

import com.android.be.dto.LichTrinhLopHocDTO;
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
public class LichTrinhLopHocService {
    
    private final LichTrinhLopHocRepository lichTrinhRepository;
    private final DatLichRepository datLichRepository;
    
    // GET - Lấy tất cả lịch trình
    public List<LichTrinhLopHoc> getAllLichTrinh() {
        return lichTrinhRepository.findAll();
    }
    
    // GET - Lấy lịch trình theo ID
    public Optional<LichTrinhLopHoc> getLichTrinhById(Integer id) {
        return lichTrinhRepository.findById(id);
    }
    
    // GET - Lấy lịch trình theo ID với thông tin số chỗ trống (DTO)
    public Optional<LichTrinhLopHocDTO> getLichTrinhDTOById(Integer id) {
        return lichTrinhRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // GET - Lấy lịch trình theo khóa học
    public List<LichTrinhLopHoc> getLichTrinhByKhoaHoc(Integer maKhoaHoc) {
        return lichTrinhRepository.findByMaKhoaHoc(maKhoaHoc);
    }
    
    // GET - Lấy lịch trình theo giáo viên
    public List<LichTrinhLopHoc> getLichTrinhByGiaoVien(Integer maGiaoVien) {
        return lichTrinhRepository.findByMaGiaoVien(maGiaoVien);
    }
    
    // GET - Lấy lịch trình theo địa điểm
    public List<LichTrinhLopHoc> getLichTrinhByDiaDiem(String diaDiem) {
        return lichTrinhRepository.findByDiaDiemContainingIgnoreCase(diaDiem);
    }
    
    // GET - Lấy lịch trình đang hoạt động
    public List<LichTrinhLopHoc> getLichTrinhActive() {
        return lichTrinhRepository.findByTrangThai(true);
    }
    
    // GET - Lấy lịch trình theo khóa học và trạng thái
    public List<LichTrinhLopHoc> getLichTrinhByKhoaHocAndTrangThai(Integer maKhoaHoc, Boolean trangThai) {
        return lichTrinhRepository.findByMaKhoaHocAndTrangThai(maKhoaHoc, trangThai);
    }
    
    // POST - Tạo lịch trình mới
    public LichTrinhLopHoc createLichTrinh(LichTrinhLopHoc lichTrinh) {
        return lichTrinhRepository.save(lichTrinh);
    }
    
    // PUT - Cập nhật lịch trình
    public LichTrinhLopHoc updateLichTrinh(Integer id, LichTrinhLopHoc lichTrinh) {
        lichTrinh.setMaLichTrinh(id);
        return lichTrinhRepository.save(lichTrinh);
    }
    
    // DELETE - Xóa lịch trình
    public void deleteLichTrinh(Integer id) {
        lichTrinhRepository.deleteById(id);
    }
    
    // GET - Lấy danh sách lớp theo ngày (Stored Procedure)
    public List<Object[]> getClassesByDate(String ngayCanXem) {
        return lichTrinhRepository.findClassesByDate(ngayCanXem);
    }
    
    // GET - Kiểm tra chỗ trống (Stored Procedure)
    public Object[] checkAvailableSeats(Integer maLichTrinh, String ngayThamGia) {
        return lichTrinhRepository.checkAvailableSeats(maLichTrinh, ngayThamGia);
    }
    
    /**
     * Convert LichTrinhLopHoc entity sang DTO với thông tin số chỗ trống
     */
    private LichTrinhLopHocDTO convertToDTO(LichTrinhLopHoc lichTrinh) {
        LichTrinhLopHocDTO dto = new LichTrinhLopHocDTO();
        dto.setMaLichTrinh(lichTrinh.getMaLichTrinh());
        dto.setMaKhoaHoc(lichTrinh.getMaKhoaHoc());
        dto.setMaGiaoVien(lichTrinh.getMaGiaoVien());
        dto.setThuTrongTuan(lichTrinh.getThuTrongTuan());
        dto.setGioBatDau(lichTrinh.getGioBatDau());
        dto.setGioKetThuc(lichTrinh.getGioKetThuc());
        dto.setDiaDiem(lichTrinh.getDiaDiem());
        dto.setSoLuongToiDa(lichTrinh.getSoLuongToiDa());
        dto.setTrangThai(lichTrinh.getTrangThai());
        
        // Tính số chỗ còn trống cho ngày mai (mặc định)
        LocalDate ngayKiemTra = LocalDate.now().plusDays(1);
        Integer soLuongDaDat = datLichRepository.countBookedSeats(lichTrinh.getMaLichTrinh(), ngayKiemTra);
        Integer soLuongToiDa = lichTrinh.getSoLuongToiDa() != null ? lichTrinh.getSoLuongToiDa() : 0;
        Integer conTrong = soLuongToiDa - soLuongDaDat;
        
        dto.setSoLuongHienTai(soLuongDaDat);
        dto.setConTrong(conTrong);
        
        // Set trạng thái hiển thị
        if (conTrong <= 0) {
            dto.setTrangThaiHienThi("Hết chỗ");
        } else if (conTrong <= 3) {
            dto.setTrangThaiHienThi("Sắp hết chỗ");
        } else {
            dto.setTrangThaiHienThi("Còn chỗ");
        }
        
        return dto;
    }
}
