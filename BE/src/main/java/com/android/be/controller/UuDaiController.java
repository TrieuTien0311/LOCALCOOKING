package com.android.be.controller;

import com.android.be.dto.ApDungUuDaiRequest;
import com.android.be.dto.ApDungUuDaiResponse;
import com.android.be.dto.UuDaiDTO;
import com.android.be.model.UuDai;
import com.android.be.service.UuDaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/uudai")
@RequiredArgsConstructor
public class UuDaiController {
    
    private final UuDaiService uuDaiService;
    
    @GetMapping
    public ResponseEntity<List<UuDaiDTO>> getAllUuDai() {
        return ResponseEntity.ok(uuDaiService.getAllUuDai());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UuDaiDTO> getUuDaiById(@PathVariable Integer id) {
        return uuDaiService.getUuDaiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UuDai> createUuDai(@RequestBody UuDai uuDai) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uuDaiService.createUuDai(uuDai));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UuDai> updateUuDai(@PathVariable Integer id, @RequestBody UuDai uuDai) {
        return ResponseEntity.ok(uuDaiService.updateUuDai(id, uuDai));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUuDai(@PathVariable Integer id) {
        uuDaiService.deleteUuDai(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Lấy danh sách ưu đãi khả dụng cho user (dùng cho Activity Vouchers - chọn áp dụng)
     * CHỈ hiển thị voucher ĐỦ ĐIỀU KIỆN
     * GET /api/uudai/available?maHocVien=1&soLuongNguoi=5
     */
    @GetMapping("/available")
    public ResponseEntity<List<UuDaiDTO>> getAvailableUuDai(
            @RequestParam Integer maHocVien,
            @RequestParam(defaultValue = "1") Integer soLuongNguoi) {
        return ResponseEntity.ok(uuDaiService.getAvailableUuDaiForUser(maHocVien, soLuongNguoi));
    }
    
    /**
     * Lấy danh sách ưu đãi để HIỂN THỊ cho user (dùng cho DetailVoucherFragment - xem thông tin)
     * Hiển thị TẤT CẢ voucher để khách biết có ưu đãi (dù chưa đủ điều kiện)
     * GET /api/uudai/display?maHocVien=1
     */
    @GetMapping("/display")
    public ResponseEntity<List<UuDaiDTO>> getDisplayUuDai(@RequestParam Integer maHocVien) {
        return ResponseEntity.ok(uuDaiService.getDisplayUuDaiForUser(maHocVien));
    }
    
    /**
     * Áp dụng mã ưu đãi và tính toán giảm giá
     * POST /api/uudai/apply
     */
    @PostMapping("/apply")
    public ResponseEntity<ApDungUuDaiResponse> apDungUuDai(@RequestBody ApDungUuDaiRequest request) {
        ApDungUuDaiResponse response = uuDaiService.apDungUuDai(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Xác nhận sử dụng mã ưu đãi (gọi sau khi thanh toán thành công)
     * POST /api/uudai/confirm/{maUuDai}
     */
    @PostMapping("/confirm/{maUuDai}")
    public ResponseEntity<?> confirmUuDai(@PathVariable Integer maUuDai) {
        uuDaiService.tangSoLuongDaSuDung(maUuDai);
        return ResponseEntity.ok().build();
    }
}
