package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KhoaHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maKhoaHoc;
    
    @Column(nullable = false, length = 200)
    private String tenKhoaHoc;
    
    @Column(length = 500)
    private String moTa;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String gioiThieu;
    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String giaTriSauBuoiHoc;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal giaTien;
    
    @Column(length = 255)
    private String hinhAnh;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer soLuongDanhGia = 0;
    
    @Column(columnDefinition = "FLOAT DEFAULT 0")
    private Float saoTrungBinh = 0.0f;
    
    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean coUuDai = false;
    
    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;
    
    // Danh sách lịch trình (không lưu trong DB, chỉ dùng để trả về API)
    @Transient
    private List<LichTrinhLopHoc> lichTrinhList;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}
