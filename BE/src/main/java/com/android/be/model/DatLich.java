package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
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
    
    @Column(name = "maLichTrinh", nullable = false)
    private Integer maLichTrinh;

    @Column(name = "ngayThamGia")
    private java.time.LocalDate ngayThamGia;

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
    private String trangThai = "Đặt trước";
    
    @Column(name = "ghiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHocVien", insertable = false, updatable = false)
    private NguoiDung hocVien;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maLichTrinh", insertable = false, updatable = false)
    private LichTrinhLopHoc lichTrinh;
    @Column(name = "maUuDai")
    private Integer maUuDai;
    
    @Column(name = "soTienGiam", precision = 10, scale = 2)
    private java.math.BigDecimal soTienGiam;
    
    @Column(name = "thoiGianHetHan")
    private LocalDateTime thoiGianHetHan;
    
    @Column(name = "thoiGianHuy")
    private LocalDateTime thoiGianHuy;
}
