package com.android.be.service;

import com.android.be.dto.MomoPaymentRequest;
import com.android.be.dto.MomoPaymentResponse;
import com.android.be.model.DatLich;
import com.android.be.model.ThanhToan;
import com.android.be.repository.DatLichRepository;
import com.android.be.repository.ThanhToanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {
    
    private final ThanhToanRepository thanhToanRepository;
    private final DatLichRepository datLichRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Momo Sandbox Test Credentials (Public)
    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    private static final String MOMO_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    
    // URLs - Thay đổi theo IP backend của bạn
    private static final String REDIRECT_URL = "localcooking://payment/result";
    private static final String IPN_URL = "https://your-backend.com/api/momo/ipn"; // Callback URL
    
    /**
     * Tạo thanh toán Momo
     */
    public MomoPaymentResponse createPayment(MomoPaymentRequest request) {
        try {
            log.info("Creating Momo payment for request: {}", request);
            
            DatLich datLich;
            
            // Kiểm tra có phải thanh toán lại không
            if (request.getMaDatLich() != null && request.getMaDatLich() > 0) {
                // Thanh toán lại - lấy đơn cũ
                datLich = datLichRepository.findById(request.getMaDatLich()).orElse(null);
                if (datLich == null) {
                    MomoPaymentResponse errorResponse = new MomoPaymentResponse();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Không tìm thấy đơn đặt lịch");
                    errorResponse.setResultCode(-1);
                    return errorResponse;
                }
                
                // Cập nhật thông tin người đặt nếu có thay đổi
                if (request.getTenNguoiDat() != null) datLich.setTenNguoiDat(request.getTenNguoiDat());
                if (request.getEmailNguoiDat() != null) datLich.setEmailNguoiDat(request.getEmailNguoiDat());
                if (request.getSdtNguoiDat() != null) datLich.setSdtNguoiDat(request.getSdtNguoiDat());
                datLich.setThoiGianHetHan(LocalDateTime.now().plusMinutes(10)); // Reset thời gian hết hạn
                
                datLich = datLichRepository.save(datLich);
                log.info("Re-payment for existing DatLich ID: {}", datLich.getMaDatLich());
                
                // Xóa ThanhToan cũ nếu có (chưa thanh toán)
                ThanhToan oldThanhToan = thanhToanRepository.findByMaDatLich(datLich.getMaDatLich());
                if (oldThanhToan != null && (oldThanhToan.getTrangThai() == null || !oldThanhToan.getTrangThai())) {
                    thanhToanRepository.delete(oldThanhToan);
                    log.info("Deleted old unpaid ThanhToan for maDatLich: {}", datLich.getMaDatLich());
                }
            } else {
                // Tạo DatLich mới
                datLich = new DatLich();
                datLich.setMaHocVien(request.getMaHocVien());
                datLich.setMaLichTrinh(request.getMaLichTrinh());
                datLich.setNgayThamGia(LocalDate.parse(request.getNgayThamGia()));
                datLich.setSoLuongNguoi(request.getSoLuongNguoi() != null ? request.getSoLuongNguoi() : 1);
                datLich.setTongTien(request.getSoTien());
                datLich.setTenNguoiDat(request.getTenNguoiDat());
                datLich.setEmailNguoiDat(request.getEmailNguoiDat());
                datLich.setSdtNguoiDat(request.getSdtNguoiDat());
                datLich.setMaUuDai(request.getMaUuDai());
                datLich.setSoTienGiam(request.getSoTienGiam());
                datLich.setTrangThai("Đặt trước");
                datLich.setNgayDat(LocalDateTime.now());
                datLich.setThoiGianHetHan(LocalDateTime.now().plusMinutes(10)); // Hết hạn sau 10 phút
                
                datLich = datLichRepository.save(datLich);
                log.info("Created new DatLich with ID: {}", datLich.getMaDatLich());
            }
            
            DatLich savedDatLich = datLich;
            
            // 2. Tạo request Momo
            String requestId = UUID.randomUUID().toString();
            String orderId = "LC_" + System.currentTimeMillis();
            String orderInfo = "Thanh toan khoa hoc nau an - " + request.getTenKhoaHoc();
            long amount = request.getSoTien().longValue();
            String extraData = "";
            
            // Tạo raw signature
            String rawSignature = "accessKey=" + ACCESS_KEY +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + IPN_URL +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + PARTNER_CODE +
                    "&redirectUrl=" + REDIRECT_URL +
                    "&requestId=" + requestId +
                    "&requestType=captureWallet";
            
            log.info("Raw signature: {}", rawSignature);
            
            // Tạo signature
            String signature = hmacSHA256(rawSignature, SECRET_KEY);
            
            // Tạo request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("partnerName", "Local Cooking");
            requestBody.put("storeId", "LocalCookingStore");
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", REDIRECT_URL);
            requestBody.put("ipnUrl", IPN_URL);
            requestBody.put("lang", "vi");
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", "captureWallet");
            requestBody.put("signature", signature);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.info("Request body: {}", jsonBody);
            
            // Gọi API Momo
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(MOMO_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.info("Momo response: {}", response.body());
            
            // Parse response
            Map<String, Object> momoResponse = objectMapper.readValue(response.body(), Map.class);
            
            int resultCode = (int) momoResponse.get("resultCode");
            String message = (String) momoResponse.get("message");
            
            MomoPaymentResponse paymentResponse = new MomoPaymentResponse();
            paymentResponse.setResultCode(resultCode);
            paymentResponse.setMessage(message);
            paymentResponse.setOrderId(orderId);
            paymentResponse.setRequestId(requestId);
            paymentResponse.setMaDatLich(savedDatLich.getMaDatLich());
            
            if (resultCode == 0) {
                String payUrl = (String) momoResponse.get("payUrl");
                String deeplink = (String) momoResponse.get("deeplink");
                String qrCodeUrl = (String) momoResponse.get("qrCodeUrl");
                
                paymentResponse.setPayUrl(payUrl);
                paymentResponse.setDeeplink(deeplink);
                paymentResponse.setQrCodeUrl(qrCodeUrl);
                paymentResponse.setSuccess(true);
                
                // 3. Lưu ThanhToan vào database
                ThanhToan thanhToan = new ThanhToan();
                thanhToan.setMaDatLich(savedDatLich.getMaDatLich());
                thanhToan.setSoTien(request.getSoTien());
                thanhToan.setPhuongThuc("Momo");
                thanhToan.setRequestId(requestId);
                thanhToan.setOrderId(orderId);
                thanhToan.setPayUrl(payUrl);
                thanhToan.setDeeplink(deeplink);
                thanhToan.setQrCodeUrl(qrCodeUrl);
                thanhToan.setTrangThai(false);
                thanhToan.setGhiChu("Đang chờ thanh toán");
                
                ThanhToan saved = thanhToanRepository.save(thanhToan);
                paymentResponse.setMaThanhToan(saved.getMaThanhToan());
                
                log.info("Payment created successfully: orderId={}, payUrl={}", orderId, payUrl);
            } else {
                paymentResponse.setSuccess(false);
                // Xóa DatLich nếu tạo Momo thất bại
                datLichRepository.delete(savedDatLich);
                log.error("Momo payment failed: {}", message);
            }
            
            return paymentResponse;
            
        } catch (Exception e) {
            log.error("Error creating Momo payment", e);
            MomoPaymentResponse errorResponse = new MomoPaymentResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Lỗi tạo thanh toán: " + e.getMessage());
            errorResponse.setResultCode(-1);
            return errorResponse;
        }
    }
    
    /**
     * Xử lý callback từ Momo (IPN)
     */
    public boolean handleCallback(Map<String, Object> callbackData) {
        try {
            String orderId = (String) callbackData.get("orderId");
            String transId = String.valueOf(callbackData.get("transId"));
            int resultCode = (int) callbackData.get("resultCode");
            String message = (String) callbackData.get("message");
            String signature = (String) callbackData.get("signature");
            
            log.info("Momo callback: orderId={}, resultCode={}, message={}", orderId, resultCode, message);
            
            // Tìm giao dịch
            ThanhToan thanhToan = thanhToanRepository.findByOrderId(orderId);
            if (thanhToan == null) {
                log.error("Transaction not found: {}", orderId);
                return false;
            }
            
            // Cập nhật trạng thái
            thanhToan.setTransId(transId);
            thanhToan.setResultCode(resultCode);
            thanhToan.setMessage(message);
            thanhToan.setSignature(signature);
            thanhToan.setThoiGianCapNhat(LocalDateTime.now());
            
            if (resultCode == 0) {
                thanhToan.setTrangThai(true);
                thanhToan.setNgayThanhToan(LocalDateTime.now());
                thanhToan.setGhiChu("✅ Thanh toán thành công qua Momo. TransID: " + transId);
                // Thông báo "Đặt lịch thành công" được tạo tự động bởi trigger SQL trg_ThongBaoThanhToan
            } else {
                thanhToan.setTrangThai(false);
                thanhToan.setGhiChu("❌ Thanh toán thất bại. Mã lỗi: " + resultCode);
            }
            
            thanhToanRepository.save(thanhToan);
            return resultCode == 0;
            
        } catch (Exception e) {
            log.error("Error handling Momo callback", e);
            return false;
        }
    }
    
    /**
     * Kiểm tra trạng thái thanh toán
     */
    public MomoPaymentResponse checkPaymentStatus(String orderId) {
        ThanhToan thanhToan = thanhToanRepository.findByOrderId(orderId);
        
        MomoPaymentResponse response = new MomoPaymentResponse();
        if (thanhToan == null) {
            response.setSuccess(false);
            response.setMessage("Không tìm thấy giao dịch");
            return response;
        }
        
        response.setOrderId(orderId);
        response.setMaThanhToan(thanhToan.getMaThanhToan());
        response.setSuccess(thanhToan.getTrangThai() != null && thanhToan.getTrangThai());
        response.setResultCode(thanhToan.getResultCode() != null ? thanhToan.getResultCode() : -1);
        response.setMessage(thanhToan.getMessage());
        response.setTransId(thanhToan.getTransId());
        
        return response;
    }
    
    /**
     * HMAC SHA256
     */
    private String hmacSHA256(String data, String key) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
