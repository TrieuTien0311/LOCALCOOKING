package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "LichTrinhLopHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichTrinhLopHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maLichTrinh;
    
    @Column(nullable = false)
    private Integer maKhoaHoc;
    
    @Column(nullable = false)
    private Integer maGiaoVien;
    
    @Column(length = 50)
    private String thuTrongTuan; // '2,3,4,5,6,7,CN'
    
    private LocalTime gioBatDau;
    
    private LocalTime gioKetThuc;
    
    @Column(length = 255)
    private String diaDiem;
    
    @Column(columnDefinition = "INT DEFAULT 20")
    private Integer soLuongToiDa = 20;
    
    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai = true;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhoaHoc", insertable = false, updatable = false)
    private KhoaHoc khoaHoc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maGiaoVien", insertable = false, updatable = false)
    private GiaoVien giaoVien;
}
