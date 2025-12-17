package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatLichDTO {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maLopHoc;
    private String tenLopHoc;
    private Integer soLuongNguoi;
    private Double tongTien;
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    private String ngayDat;
    private String trangThai;
    private String ghiChu;
    private String hinhAnhLopHoc;
}
