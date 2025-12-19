package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichTrinhLopHocDTO {
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private Integer maGiaoVien;
    private String thuTrongTuan;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private String diaDiem;
    private Integer soLuongToiDa;
    private Boolean trangThai;
    
    // Thông tin bổ sung
    private String tenGiaoVien;
    private Integer soLuongHienTai;
    private Integer conTrong;
    private String trangThaiHienThi;
}
