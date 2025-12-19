package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHocDTO {
    private Integer maLopHoc;
    private String tenLop;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private String thoiGian;
    
    // Lịch trình lặp lại
    private String loaiLich;  // 'HangNgay', 'HangTuan', 'MotLan'
    private String ngayBatDau;
    private String ngayKetThuc;
    private String cacNgayTrongTuan;  // '2,3,4,5,6'
    
    private String ngay;  // Deprecated - dùng ngayBatDau thay thế
    private String diaDiem;
    private String gia;
    private Float danhGia;
    private Integer soDanhGia;
    private String hinhAnh;
    private Boolean coUuDai;
    private String thoiGianKetThuc;
    private Integer suat;
    private Boolean isFavorite;
    private Boolean daDienRa;
    private List<DanhMucMonAnDTO> lichTrinhLopHoc;
    private String tenGiaoVien;
    private String trangThai;
}
