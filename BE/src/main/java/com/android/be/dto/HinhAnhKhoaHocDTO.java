package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhKhoaHocDTO {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan;
    private Integer thuTu;
}
