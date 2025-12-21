package com.android.be.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MomoPaymentRequest {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maLichTrinh;
    private BigDecimal soTien;
    private String tenKhoaHoc;
    private String ngayThamGia;
    private Integer soLuongNguoi;
    
    // Thông tin liên hệ
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    
    // Ưu đãi (nếu có)
    private Integer maUuDai;
    private BigDecimal soTienGiam;
}
