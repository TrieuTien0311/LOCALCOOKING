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
    
    @Column(name = "maKhoaHoc")
    private Integer maKhoaHoc;
    
    @Column(name = "maDanhMuc")
    private Integer maDanhMuc;
    
    @Column(name = "tenMon", length = 200)
    private String tenMon;
    
    @Column(name = "gioiThieu", columnDefinition = "NVARCHAR(MAX)")
    private String gioiThieu;

    @Column(name = "nguyenLieu", columnDefinition = "NVARCHAR(MAX)")
    private String nguyenLieu;

    @Column(name = "hinhAnh", length = 255)
    private String hinhAnh;
}
