package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatLichDashboardDTO {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maLichTrinh;
    private LocalDate ngayThamGia;
    private Integer soLuongNguoi;
    private BigDecimal tongTien;
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    private LocalDateTime ngayDat;
    private String trangThai;
    private String ghiChu;
    private Boolean daThanhToan;
    
    // Thông tin khóa học
    private String tenKhoaHoc;
    
    // Thông tin học viên (nếu tenNguoiDat null)
    private String hoTenHocVien;
    private String emailHocVien;
}
