package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HinhAnhMonAn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhMonAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHinhAnh")
    private Integer maHinhAnh;
    
    @Column(name = "maMonAn", nullable = false)
    private Integer maMonAn;
    
    @Column(name = "duongDan", nullable = false)
    private String duongDan;
    
    @Column(name = "thuTu")
    private Integer thuTu = 1;
}
