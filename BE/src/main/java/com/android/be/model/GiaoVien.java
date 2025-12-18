package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GiaoVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoVien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGiaoVien")
    private Integer maGiaoVien;
    
    @Column(name = "maNguoiDung", unique = true, nullable = false)
    private Integer maNguoiDung;
    
    @Column(name = "chuyenMon", length = 200)
    private String chuyenMon;
    
    @Column(name = "kinhNghiem", columnDefinition = "NVARCHAR(MAX)")
    private String kinhNghiem;
    
    @Column(name = "lichSuKinhNghiem", columnDefinition = "NVARCHAR(MAX)")
    private String lichSuKinhNghiem;
    
    @Column(name = "moTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
}
