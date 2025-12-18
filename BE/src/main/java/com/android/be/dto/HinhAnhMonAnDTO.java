package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhMonAnDTO {
    private Integer maHinhAnh;
    private Integer maMonAn;
    private String duongDan;
    private Integer thuTu;
}
