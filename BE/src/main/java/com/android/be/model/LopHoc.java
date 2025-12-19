package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LopHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maLopHoc")
    private Integer maLopHoc;
    
    @Column(name = "tenLopHoc", nullable = false, length = 200)
    private String tenLopHoc;
    
    @Column(name = "moTa", length = 500)
    private String moTa;
    
    @Column(name = "gioiThieu", columnDefinition = "NVARCHAR(MAX)")
    private String gioiThieu;
    
    @Column(name = "giaTriSauBuoiHoc", columnDefinition = "NVARCHAR(MAX)")
    private String giaTriSauBuoiHoc;
    
    @Column(name = "maGiaoVien")
    private Integer maGiaoVien;
    
    @Column(name = "soLuongToiDa")
    private Integer soLuongToiDa = 20;
    
    @Column(name = "soLuongHienTai")
    private Integer soLuongHienTai = 0;
    
    @Column(name = "giaTien", precision = 10, scale = 2, nullable = false)
    private BigDecimal giaTien;
    
    @Column(name = "thoiGian", length = 100)
    private String thoiGian;
    
    @Column(name = "diaDiem")
    private String diaDiem;
    
    @Column(name = "trangThai", length = 30)
    private String trangThai = "Sắp diễn ra";
    
    // *** LỊCH TRÌNH LẶP LẠI ***
    @Column(name = "loaiLich", length = 20)
    private String loaiLich = "HangNgay";  // 'HangNgay', 'HangTuan', 'MotLan'
    
    @Column(name = "ngayBatDau", nullable = false)
    private LocalDate ngayBatDau;
    
    @Column(name = "ngayKetThuc")
    private LocalDate ngayKetThuc;  // NULL = không giới hạn
    
    @Column(name = "cacNgayTrongTuan", length = 50)
    private String cacNgayTrongTuan;  // '2,3,4,5,6' (1=CN, 2=T2, ..., 7=T7)
    
    @Column(name = "gioBatDau")
    private java.sql.Time gioBatDau;
    
    @Column(name = "gioKetThuc")
    private java.sql.Time gioKetThuc;
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
    
    @Column(name = "soLuongDanhGia")
    private Integer soLuongDanhGia = 0;
    
    @Column(name = "saoTrungBinh")
    private Float saoTrungBinh = 0.0f;
    
    @Column(name = "coUuDai")
    private Boolean coUuDai = false;
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
