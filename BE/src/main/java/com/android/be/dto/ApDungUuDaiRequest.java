package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApDungUuDaiRequest {
    private Integer maHocVien;
    private String maCode;
    private BigDecimal tongTien;
    private Integer soLuongNguoi;
}
