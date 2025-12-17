package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToanDTO {
    private Integer maThanhToan;
    private Integer maDatLich;
    private Double soTien;
    private String phuongThuc;
    private String trangThai;
    private String ngayThanhToan;
    private String maGiaoDich;
    private String ghiChu;
}
