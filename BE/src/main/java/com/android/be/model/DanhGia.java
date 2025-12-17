package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhGia")
    private Integer maDanhGia;
    
    @Column(name = "maHocVien", nullable = false)
    private Integer maHocVien;
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "diemDanhGia")
    private Integer diemDanhGia; // 1-5
    
    @Column(name = "binhLuan", columnDefinition = "NVARCHAR(MAX)")
    private String binhLuan;
    
    @Column(name = "ngayDanhGia")
    private LocalDateTime ngayDanhGia = LocalDateTime.now();
}
