package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhDanhGiaDTO {
    private Integer maHinhAnh;
    private Integer maDanhGia;
    private String duongDan;
    private String loaiFile; // 'image' hoặc 'video'
    private Integer thuTu;
}
