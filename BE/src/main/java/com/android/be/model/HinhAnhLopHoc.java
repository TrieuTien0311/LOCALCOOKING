package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HinhAnhLopHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhLopHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHinhAnh")
    private Integer maHinhAnh;
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "duongDan", nullable = false)
    private String duongDan;
    
    @Column(name = "thuTu")
    private Integer thuTu = 1;
}
