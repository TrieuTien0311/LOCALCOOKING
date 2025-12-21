package com.android.be.dto;

import lombok.Data;

@Data
public class MomoPaymentResponse {
    private boolean success;
    private int resultCode;
    private String message;
    
    private String orderId;
    private String requestId;
    private String transId;
    
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
    
    private Integer maThanhToan;
    private Integer maDatLich;
}
