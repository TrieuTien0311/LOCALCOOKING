package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DanhGia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhGia")
    private Integer maDanhGia;
    
    @Column(name = "maHocVien", nullable = false)
    private Integer maHocVien;
    
    @Column(name = "maKhoaHoc", nullable = false)
    private Integer maKhoaHoc;
    
    @Column(name = "maDatLich")
    private Integer maDatLich;
    
    @Column(name = "diemDanhGia")
    private Integer diemDanhGia; // 1-5
    
    @Column(name = "binhLuan", columnDefinition = "NVARCHAR(MAX)")
    private String binhLuan;
    
    @Column(name = "ngayDanhGia")
    private LocalDateTime ngayDanhGia = LocalDateTime.now();
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHocVien", insertable = false, updatable = false)
    private NguoiDung hocVien;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhoaHoc", insertable = false, updatable = false)
    private KhoaHoc khoaHoc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDatLich", insertable = false, updatable = false)
    private DatLich datLich;
    
    @OneToMany(mappedBy = "danhGia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HinhAnhDanhGia> hinhAnhList;
}
