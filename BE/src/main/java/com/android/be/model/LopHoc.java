package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
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
    
    @Column(name = "moTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;
    
    @Column(name = "maGiaoVien")
    private Integer maGiaoVien;
    
    @Column(name = "tenGiaoVien", length = 100)
    private String tenGiaoVien;
    
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
    
    @Column(name = "ngayDienRa")
    private String ngayDienRa; // SQLite stores DATE as TEXT (YYYY-MM-DD)
    
    @Column(name = "gioBatDau")
    private String gioBatDau; // SQLite stores TIME as TEXT (HH:MM)
    
    @Column(name = "gioKetThuc")
    private String gioKetThuc; // SQLite stores TIME as TEXT
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
    
    @Column(name = "coUuDai")
    private Boolean coUuDai = false;
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
