package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "DatLich", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_DatLich", columnNames = {"maHocVien", "maLopHoc"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatLich {
    
    @Id
    @Column(name = "maDatLich")
    private Integer maDatLich;
    
    @Column(name = "maHocVien", nullable = false)
    private Integer maHocVien;
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "ngayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();
    
    @Column(name = "trangThai", length = 30)
    private String trangThai = "ChoDuyet";
    
    @Column(name = "ghiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;
}
