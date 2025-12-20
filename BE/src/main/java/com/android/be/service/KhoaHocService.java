package com.android.be.service;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.dto.KhoaHocDTO;
import com.android.be.dto.LichTrinhLopHocDTO;
import com.android.be.model.KhoaHoc;
import com.android.be.model.LichTrinhLopHoc;
import com.android.be.repository.DatLichRepository;
import com.android.be.repository.KhoaHocRepository;
import com.android.be.repository.LichTrinhLopHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KhoaHocService {
    private final KhoaHocRepository khoaHocRepository;
    private final LichTrinhLopHocRepository lichTrinhRepository;
    private final DanhMucMonAnService danhMucMonAnService;
    private final DatLichRepository datLichRepository;
    
    public List<KhoaHocDTO> getAllKhoaHoc() {
        return khoaHocRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<KhoaHocDTO> getKhoaHocById(Integer id) {
        return khoaHocRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public KhoaHoc createKhoaHoc(KhoaHoc khoaHoc) {
        return khoaHocRepository.save(khoaHoc);
    }
    
    public KhoaHoc updateKhoaHoc(Integer id, KhoaHoc khoaHoc) {
        khoaHoc.setMaKhoaHoc(id);
        return khoaHocRepository.save(khoaHoc);
    }
    
    public void deleteKhoaHoc(Integer id) {
        khoaHocRepository.deleteById(id);
    }
    
    /**
     * Tìm kiếm khóa học theo địa điểm
     */
    public List<KhoaHocDTO> searchByDiaDiem(String diaDiem) {
        // Tìm các lịch trình có địa điểm chứa từ khóa
        List<LichTrinhLopHoc> lichTrinhs = lichTrinhRepository.findByDiaDiemContainingIgnoreCase(diaDiem);
        
        // Lấy danh sách khóa học từ lịch trình
        return lichTrinhs.stream()
                .map(lt -> khoaHocRepository.findById(lt.getMaKhoaHoc()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm kiếm khóa học theo địa điểm và ngày (sử dụng stored procedure)
     * Stored procedure sp_LayDanhSachLopTheoNgay trả về:
     * [0] maKhoaHoc, [1] tenKhoaHoc, [2] hinhAnh, [3] giaTien, [4] saoTrungBinh, [5] soLuongDanhGia,
     * [6] maLichTrinh, [7] gioBatDau, [8] gioKetThuc, [9] diaDiem, [10] TongCho, [11] DaDat, [12] ConTrong, [13] TrangThaiHienThi
     */
    public List<KhoaHocDTO> searchByDiaDiemAndDate(String diaDiem, String ngayTimKiem) {
        System.out.println("=== DEBUG searchByDiaDiemAndDate ===");
        System.out.println("Địa điểm: " + diaDiem);
        System.out.println("Ngày tìm kiếm: " + ngayTimKiem);
        
        // Gọi stored procedure
        List<Object[]> results = lichTrinhRepository.findClassesByDate(ngayTimKiem);
        System.out.println("Số kết quả từ stored procedure: " + results.size());
        
        // Debug: In ra kết quả đầu tiên
        if (!results.isEmpty()) {
            Object[] firstRow = results.get(0);
            System.out.println("Kết quả đầu tiên:");
            System.out.println("  [0] maKhoaHoc: " + firstRow[0]);
            System.out.println("  [6] maLichTrinh: " + firstRow[6]);
            System.out.println("  [9] diaDiem: " + firstRow[9]);
            System.out.println("  [10] TongCho: " + firstRow[10]);
            System.out.println("  [11] DaDat: " + firstRow[11]);
            System.out.println("  [12] ConTrong: " + firstRow[12]);
        }
        
        // Lọc theo địa điểm và chuyển đổi sang DTO
        return results.stream()
                .filter(row -> {
                    String diaDiemLop = (String) row[9]; // diaDiem ở vị trí 9
                    return diaDiemLop != null && diaDiemLop.toLowerCase().contains(diaDiem.toLowerCase());
                })
                .map(row -> {
                    Integer maKhoaHoc = (Integer) row[0];
                    Integer maLichTrinh = (Integer) row[6];
                    Integer conTrong = (Integer) row[12]; // Số chỗ còn trống
                    
                    System.out.println("Xử lý khóa học " + maKhoaHoc + ", lịch trình " + maLichTrinh + ", còn trống: " + conTrong);
                    
                    // Lấy khóa học từ database
                    Optional<KhoaHoc> khoaHocOpt = khoaHocRepository.findById(maKhoaHoc);
                    if (!khoaHocOpt.isPresent()) {
                        return null;
                    }
                    
                    KhoaHoc khoaHoc = khoaHocOpt.get();
                    KhoaHocDTO dto = convertToDTO(khoaHoc);
                    
                    // Lọc chỉ lấy lịch trình khớp với ngày được chọn và set số chỗ còn trống
                    if (dto.getLichTrinhList() != null) {
                        List<LichTrinhLopHocDTO> filteredLichTrinh = dto.getLichTrinhList().stream()
                                .filter(lt -> lt.getMaLichTrinh().equals(maLichTrinh))
                                .peek(lt -> {
                                    lt.setConTrong(conTrong);
                                    System.out.println("  -> Set conTrong = " + conTrong + " cho lịch trình " + lt.getMaLichTrinh());
                                })
                                .collect(Collectors.toList());
                        dto.setLichTrinhList(filteredLichTrinh);
                    }
                    
                    return dto;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
    
    private KhoaHocDTO convertToDTO(KhoaHoc khoaHoc) {
        KhoaHocDTO dto = new KhoaHocDTO();
        dto.setMaKhoaHoc(khoaHoc.getMaKhoaHoc());
        dto.setTenKhoaHoc(khoaHoc.getTenKhoaHoc());
        dto.setMoTa(khoaHoc.getMoTa());
        dto.setGioiThieu(khoaHoc.getGioiThieu());
        dto.setGiaTriSauBuoiHoc(khoaHoc.getGiaTriSauBuoiHoc());
        dto.setGiaTien(khoaHoc.getGiaTien());
        dto.setHinhAnh(khoaHoc.getHinhAnh());
        dto.setSoLuongDanhGia(khoaHoc.getSoLuongDanhGia());
        dto.setSaoTrungBinh(khoaHoc.getSaoTrungBinh());
        dto.setCoUuDai(khoaHoc.getCoUuDai());
        
        // Tính toán thông tin ưu đãi nếu khóa học có ưu đãi
        if (Boolean.TRUE.equals(khoaHoc.getCoUuDai())) {
            tinhToanUuDai(dto, khoaHoc.getGiaTien());
        }
        
        // Lấy danh sách lịch trình
        List<LichTrinhLopHoc> lichTrinhs = lichTrinhRepository.findByMaKhoaHoc(khoaHoc.getMaKhoaHoc());
        dto.setLichTrinhList(lichTrinhs.stream()
                .map(this::convertLichTrinhToDTO)
                .collect(Collectors.toList()));
        
        // Lấy danh sách danh mục món ăn (3 danh mục: khai vị, món chính, tráng miệng)
        List<DanhMucMonAnDTO> danhMucMonAnList = danhMucMonAnService.getDanhMucMonAnByKhoaHoc(khoaHoc.getMaKhoaHoc());
        dto.setDanhMucMonAnList(danhMucMonAnList);
        
        return dto;
    }
    
    /**
     * Tính toán phần trăm giảm và giá sau giảm cho khóa học có ưu đãi
     * Cố định giảm 10%
     */
    private void tinhToanUuDai(KhoaHocDTO dto, BigDecimal giaTien) {
        // Cố định phần trăm giảm là 10%
        double phanTramGiam = 10.0;
        
        // Tính số tiền giảm = giá tiền * 10%
        BigDecimal soTienGiam = giaTien.multiply(BigDecimal.valueOf(phanTramGiam))
                .divide(BigDecimal.valueOf(100), 0, java.math.RoundingMode.HALF_UP);
        
        // Tính giá sau giảm
        BigDecimal giaSauGiam = giaTien.subtract(soTienGiam);
        if (giaSauGiam.compareTo(BigDecimal.ZERO) < 0) {
            giaSauGiam = BigDecimal.ZERO;
        }
        
        dto.setPhanTramGiam(phanTramGiam);
        dto.setGiaSauGiam(giaSauGiam);
    }
    
    private LichTrinhLopHocDTO convertLichTrinhToDTO(LichTrinhLopHoc lichTrinh) {
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
        
        // Tính số chỗ còn trống cho ngày mai (hoặc ngày gần nhất)
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
