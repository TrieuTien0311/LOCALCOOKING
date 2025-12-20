package com.android.be.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "YeuThich")
public class YeuThich {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maYeuThich;
    
    @Column(nullable = false)
    private Integer maHocVien;
    
    @Column(nullable = false)
    private Integer maKhoaHoc;
    
    @Column(nullable = false)
    private LocalDateTime ngayThem;
    
    @PrePersist
    protected void onCreate() {
        ngayThem = LocalDateTime.now();
    }
    
    // Constructors
    public YeuThich() {
    }
    
    public YeuThich(Integer maHocVien, Integer maKhoaHoc) {
        this.maHocVien = maHocVien;
        this.maKhoaHoc = maKhoaHoc;
    }
    
    // Getters and Setters
    public Integer getMaYeuThich() {
        return maYeuThich;
    }
    
    public void setMaYeuThich(Integer maYeuThich) {
        this.maYeuThich = maYeuThich;
    }
    
    public Integer getMaHocVien() {
        return maHocVien;
    }
    
    public void setMaHocVien(Integer maHocVien) {
        this.maHocVien = maHocVien;
    }
    
    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }
    
    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }
    
    public LocalDateTime getNgayThem() {
        return ngayThem;
    }
    
    public void setNgayThem(LocalDateTime ngayThem) {
        this.ngayThem = ngayThem;
    }
}
