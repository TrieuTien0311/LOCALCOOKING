package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApDungUuDaiResponse {
    private boolean thanhCong;
    private String message;
    private Integer maUuDai;
    private String maCode;
    private String tenUuDai;
    private BigDecimal tongTienGoc;
    private BigDecimal soTienGiam;
    private BigDecimal tongTienSauGiam;
    private String loaiGiam;
    private Double phanTramGiam;
}
