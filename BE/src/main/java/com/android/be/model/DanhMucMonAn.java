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
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "tenDanhMuc", nullable = false, length = 100)
    private String tenDanhMuc; // Món khai vị, Món chính, Món tráng miệng
    
    @Column(name = "thoiGian", length = 50)
    private String thoiGian; // 14:00 - 15:00
    
    @Column(name = "iconDanhMuc")
    private String iconDanhMuc;
    
    @Column(name = "thuTu")
    private Integer thuTu = 1;
}
