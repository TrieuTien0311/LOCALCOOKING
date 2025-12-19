package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DanhMucMonAn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhMucMonAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhMuc")
    private Integer maDanhMuc;
    
    @Column(name = "tenDanhMuc", length = 100, nullable = false)
    private String tenDanhMuc;
    
    @Column(name = "iconDanhMuc", length = 255)
    private String iconDanhMuc;
    
    @Column(name = "thuTu")
    private Integer thuTu;
}
