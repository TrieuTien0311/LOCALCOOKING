package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNguoiDung")
    private Integer maNguoiDung;
    
    @Column(name = "tenDangNhap", unique = true, nullable = false, length = 50)
    private String tenDangNhap;
    
    @Column(name = "matKhau", nullable = false)
    private String matKhau;
    
    @Column(name = "hoTen", length = 100)
    private String hoTen;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "soDienThoai", length = 15)
    private String soDienThoai;
    
    @Column(name = "diaChi")
    private String diaChi;
    
    @Column(name = "vaiTro", length = 20)
    private String vaiTro = "HocVien"; // HocVien | GiaoVien | Admin
    
    @Column(name = "trangThai", length = 20)
    private String trangThai = "HoatDong";
    
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
    
    @Column(name = "lanCapNhatCuoi")
    private LocalDateTime lanCapNhatCuoi = LocalDateTime.now();
}
