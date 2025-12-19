package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HinhAnhKhoaHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhKhoaHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maHinhAnh;
    
    @Column(nullable = false)
    private Integer maKhoaHoc;
    
    @Column(nullable = false, length = 255)
    private String duongDan;
    
    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer thuTu = 1;
    
    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhoaHoc", insertable = false, updatable = false)
    private KhoaHoc khoaHoc;
}
