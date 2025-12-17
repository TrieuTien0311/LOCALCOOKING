package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "DatLich")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatLich {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDatLich")
    private Integer maDatLich;
    
    @Column(name = "maHocVien", nullable = false)
    private Integer maHocVien;
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "soLuongNguoi")
    private Integer soLuongNguoi = 1;
    
    @Column(name = "tongTien", precision = 10, scale = 2)
    private java.math.BigDecimal tongTien;
    
    @Column(name = "tenNguoiDat", length = 100)
    private String tenNguoiDat;
    
    @Column(name = "emailNguoiDat", length = 100)
    private String emailNguoiDat;
    
    @Column(name = "sdtNguoiDat", length = 15)
    private String sdtNguoiDat;
    
    @Column(name = "ngayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();
    
    @Column(name = "trangThai", length = 30)
    private String trangThai = "Chờ Duyệt";
    
    @Column(name = "ghiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;
}
