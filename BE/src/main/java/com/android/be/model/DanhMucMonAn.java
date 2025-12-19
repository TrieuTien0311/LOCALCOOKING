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
    
    @Column(name = "tenDanhMuc", nullable = false, length = 100)
    private String tenDanhMuc; // Món khai vị, Món chính, Món tráng miệng
    
    @Column(name = "iconDanhMuc")
    private String iconDanhMuc;
    
    @Column(name = "thuTu")
    private Integer thuTu = 1;
}
