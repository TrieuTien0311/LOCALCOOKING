package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThongBao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongBao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThongBao")
    private Integer maThongBao;
    
    @Column(name = "maNguoiNhan")
    private Integer maNguoiNhan;
    
    @Column(name = "tieuDe", nullable = false)
    private String tieuDe;
    
    @Column(name = "noiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;
    
    @Column(name = "loaiThongBao", length = 30)
    private String loaiThongBao = "Hệ Thống";
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
    
    @Column(name = "daDoc")
    private Boolean daDoc = false;
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
