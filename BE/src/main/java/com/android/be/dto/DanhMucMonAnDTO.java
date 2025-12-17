package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhMucMonAnDTO {
    private Integer maDanhMuc;
    private String tenDanhMuc;
    private String thoiGian;
    private String iconDanhMuc;
    private Integer thuTu;
    private List<MonAnDTO> danhSachMon;
}
