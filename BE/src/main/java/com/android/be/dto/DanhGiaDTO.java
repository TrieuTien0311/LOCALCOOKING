package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaDTO {
    private Integer maDanhGia;
    private Integer maHocVien;
    private String tenHocVien;
    private Integer maLopHoc;
    private String tenLopHoc;
    private Integer diemDanhGia;
    private String binhLuan;
    private String ngayDanhGia;
}
