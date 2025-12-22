package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "HinhAnhDanhGia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhDanhGia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHinhAnh")
    private Integer maHinhAnh;
    
    @Column(name = "maDanhGia", nullable = false)
    private Integer maDanhGia;
    
    @Column(name = "duongDan", length = 500, nullable = false)
    private String duongDan;
    
    @Column(name = "loaiFile", length = 20)
    private String loaiFile = "image"; // 'image' hoặc 'video'
    
    @Column(name = "thuTu")
    private Integer thuTu = 1;
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
    
    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDanhGia", insertable = false, updatable = false)
    private DanhGia danhGia;
}
