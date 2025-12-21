package com.android.be.controller;

import com.android.be.dto.MomoPaymentRequest;
import com.android.be.dto.MomoPaymentResponse;
import com.android.be.service.MomoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/momo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MomoController {
    
    private final MomoService momoService;
    
    /**
     * Tạo thanh toán Momo
     * POST /api/momo/create
     */
    @PostMapping("/create")
    public ResponseEntity<MomoPaymentResponse> createPayment(@RequestBody MomoPaymentRequest request) {
        log.info("Creating Momo payment: {}", request);
        MomoPaymentResponse response = momoService.createPayment(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Callback từ Momo (IPN)
     * POST /api/momo/ipn
     */
    @PostMapping("/ipn")
    public ResponseEntity<Map<String, Object>> handleIPN(@RequestBody Map<String, Object> callbackData) {
        log.info("Received Momo IPN: {}", callbackData);
        boolean success = momoService.handleCallback(callbackData);
        return ResponseEntity.ok(Map.of(
            "status", success ? "success" : "failed",
            "message", success ? "Cập nhật thành công" : "Cập nhật thất bại"
        ));
    }
    
    /**
     * Kiểm tra trạng thái thanh toán
     * GET /api/momo/status/{orderId}
     */
    @GetMapping("/status/{orderId}")
    public ResponseEntity<MomoPaymentResponse> checkStatus(@PathVariable String orderId) {
        log.info("Checking payment status: {}", orderId);
        MomoPaymentResponse response = momoService.checkPaymentStatus(orderId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Simulate callback thành công (cho testing)
     * POST /api/momo/simulate-success/{orderId}
     */
    @PostMapping("/simulate-success/{orderId}")
    public ResponseEntity<Map<String, Object>> simulateSuccess(@PathVariable String orderId) {
        log.info("Simulating success for: {}", orderId);
        Map<String, Object> fakeCallback = Map.of(
            "orderId", orderId,
            "transId", "TRANS_" + System.currentTimeMillis(),
            "resultCode", 0,
            "message", "Thành công",
            "signature", "fake_signature"
        );
        boolean success = momoService.handleCallback(fakeCallback);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Đã cập nhật thanh toán thành công" : "Lỗi cập nhật"
        ));
    }
}
