package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UuDaiDTO {
    private Integer maUuDai;
    private String maCode;
    private String hinhAnh;
    private String tieuDe;
    private String moTa;
    private String HSD; // Hạn sử dụng
    private Boolean duocChon;
    private String loaiGiam;
    private Double giaTriGiam;
    private Double giamToiDa;
    private Integer soLuong;
    private Integer soLuongDaSuDung;
    private String trangThai;
}
