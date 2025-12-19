package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonAnDTO {
    private Integer maMonAn;
    private Integer maLopHoc;
    private Integer maDanhMuc;
    private String tenMon;
    private String gioiThieu;
    private String nguyenLieu;
}
