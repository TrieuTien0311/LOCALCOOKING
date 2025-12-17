package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongBaoDTO {
    private Integer maThongBao;
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private String anhTB;
    private Boolean trangThai; // true = đã đọc, false = chưa đọc
    private String loaiThongBao;
}
