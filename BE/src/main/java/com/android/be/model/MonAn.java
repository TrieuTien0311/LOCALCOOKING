package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MonAn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maMonAn")
    private Integer maMonAn;
    
    @Column(name = "maDanhMuc", nullable = false)
    private Integer maDanhMuc;
    
    @Column(name = "tenMon", nullable = false, length = 200)
    private String tenMon;
    
    @Column(name = "gioiThieu", columnDefinition = "NVARCHAR(MAX)")
    private String gioiThieu;
    
    @Column(name = "nguyenLieu", columnDefinition = "NVARCHAR(MAX)")
    private String nguyenLieu;
    
    @Column(name = "hinhAnh")
    private String hinhAnh;
}
