package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaDTO {
    private Integer maDanhGia;
    private Integer maHocVien;
    private String tenHocVien;
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private Integer maDatLich;
    private Integer diemDanhGia;
    private String binhLuan;
    private String ngayDanhGia;
    private List<HinhAnhDanhGiaDTO> hinhAnhList;
    
    // Thông tin bổ sung cho hiển thị
    private Boolean daDanhGia;
    private String trangThaiDanhGia; // "CÓ THỂ ĐÁNH GIÁ", "ĐÃ ĐÁNH GIÁ", "KHÔNG THỂ ĐÁNH GIÁ"
}
