package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    
    @Id
    @Column(name = "maThanhToan")
    private Integer maThanhToan;
    
    @Column(name = "maDatLich", nullable = false)
    private Integer maDatLich;
    
    @Column(name = "soTien", precision = 10, scale = 2, nullable = false)
    private BigDecimal soTien;
    
    @Column(name = "phuongThuc", nullable = false, length = 30)
    private String phuongThuc;
    
    @Column(name = "trangThai", length = 30)
    private String trangThai = "ChuaThanhToan";
    
    @Column(name = "ngayThanhToan")
    private LocalDateTime ngayThanhToan;
    
    @Column(name = "maGiaoDich", length = 100)
    private String maGiaoDich;
    
    @Column(name = "ghiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;
}
