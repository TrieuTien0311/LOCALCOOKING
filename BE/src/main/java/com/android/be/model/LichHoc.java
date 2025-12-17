package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LichHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichHoc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maLichHoc")
    private Integer maLichHoc;
    
    @Column(name = "maLopHoc", nullable = false)
    private Integer maLopHoc;
    
    @Column(name = "ngayHoc", nullable = false)
    private String ngayHoc; // SQLite stores DATE as TEXT (YYYY-MM-DD)
    
    @Column(name = "gioBatDau", nullable = false)
    private String gioBatDau; // SQLite stores TIME as TEXT (HH:MM) 
    
    @Column(name = "gioKetThuc", nullable = false)
    private String gioKetThuc; 
    
    @Column(name = "noiDung", length = 500)
    private String noiDung;
    
    @Column(name = "trangThai", length = 20)
    private String trangThai = "Chưa Học";
}
