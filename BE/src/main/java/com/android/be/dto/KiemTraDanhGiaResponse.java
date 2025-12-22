package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KiemTraDanhGiaResponse {
    private Integer maDatLich;
    private String trangThaiDon;
    private Boolean daDanhGia;
    private Integer maDanhGia;
    private String trangThaiDanhGia; // "CÓ THỂ ĐÁNH GIÁ", "ĐÃ ĐÁNH GIÁ", "KHÔNG THỂ ĐÁNH GIÁ"
    private DanhGiaDTO danhGia; // Nếu đã đánh giá thì trả về thông tin đánh giá
}
