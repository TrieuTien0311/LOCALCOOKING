package com.android.be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThanhToan")
    private Integer maThanhToan;
    
    @Column(name = "maDatLich", nullable = false)
    private Integer maDatLich;
    
    @Column(name = "soTien", precision = 10, scale = 2, nullable = false)
    private BigDecimal soTien;
    
    @Column(name = "phuongThuc", nullable = false, length = 30)
    private String phuongThuc;
    
    // Momo fields
    @Column(name = "requestId", length = 100)
    private String requestId;
    
    @Column(name = "orderId", length = 100)
    private String orderId;
    
    @Column(name = "transId", length = 100)
    private String transId;
    
    @Column(name = "payUrl", columnDefinition = "TEXT")
    private String payUrl;
    
    @Column(name = "deeplink", columnDefinition = "TEXT")
    private String deeplink;
    
    @Column(name = "qrCodeUrl", columnDefinition = "TEXT")
    private String qrCodeUrl;
    
    @Column(name = "resultCode")
    private Integer resultCode;
    
    @Column(name = "message", length = 255)
    private String message;
    
    @Column(name = "trangThai")
    private Boolean trangThai = false;
    
    @Column(name = "thoiGianTao")
    private LocalDateTime thoiGianTao;
    
    @Column(name = "ngayThanhToan")
    private LocalDateTime ngayThanhToan;
    
    @Column(name = "thoiGianCapNhat")
    private LocalDateTime thoiGianCapNhat;
    
    @Column(name = "signature", length = 255)
    private String signature;
    
    @Column(name = "ghiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;
    
    @PrePersist
    protected void onCreate() {
        thoiGianTao = LocalDateTime.now();
    }
}
