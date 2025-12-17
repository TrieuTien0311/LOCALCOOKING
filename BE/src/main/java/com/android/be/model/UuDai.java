package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "UuDai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UuDai {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maUuDai")
    private Integer maUuDai;
    
    @Column(name = "maCode", unique = true, nullable = false, length = 50)
    private String maCode;
    
    @Column(name = "tenUuDai", nullable = false, length = 200)
    private String tenUuDai;
    
    @Column(name = "moTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;
    
    @Column(name = "loaiGiam", nullable = false, length = 20)
    private String loaiGiam; // PhanTram | SoTien
    
    @Column(name = "giaTriGiam", precision = 10, scale = 2, nullable = false)
    private BigDecimal giaTriGiam;
    
    @Column(name = "giamToiDa", precision = 10, scale = 2)
    private BigDecimal giamToiDa;
    
    @Column(name = "soLuong")
    private Integer soLuong;
    
    @Column(name = "soLuongDaSuDung")
    private Integer soLuongDaSuDung = 0;
    
    @Column(name = "ngayBatDau", nullable = false)
    private String ngayBatDau; // SQLite stores DATE as TEXT (YYYY-MM-DD)
    
    @Column(name = "ngayKetThuc", nullable = false)
    private String ngayKetThuc; // SQLite stores DATE as TEXT (YYYY-MM-DD)
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
    
    @Column(name = "trangThai", length = 20)
    private String trangThai = "Hoạt Động";
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
