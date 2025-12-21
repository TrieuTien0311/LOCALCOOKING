package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoaHocDTO {
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private BigDecimal giaTien;
    private String hinhAnh;
    private Integer soLuongDanhGia;
    private Float saoTrungBinh;
    private Boolean coUuDai;
    
    // Thông tin ưu đãi (tự động tính toán khi coUuDai = true)
    private Double phanTramGiam;
    private BigDecimal giaSauGiam;
    
    // Thông tin lịch trình
    private List<LichTrinhLopHocDTO> lichTrinhList;
    
    // Thông tin món ăn
    private List<DanhMucMonAnDTO> danhMucMonAnList;
    
    // THÊM MỚI: Danh sách hình ảnh slide
    private List<HinhAnhKhoaHocDTO> hinhAnhList;
}
