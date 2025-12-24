package com.android.be.controller;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.model.ThongBao;
import com.android.be.service.ThongBaoService;
import com.android.be.service.ThongBaoSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thongbao")
@RequiredArgsConstructor
public class ThongBaoController {
    
    private final ThongBaoService thongBaoService;
    private final ThongBaoSchedulerService thongBaoSchedulerService;
    
    // Lấy tất cả thông báo
    @GetMapping
    public ResponseEntity<List<ThongBaoDTO>> getAllThongBao() {
        return ResponseEntity.ok(thongBaoService.getAllThongBao());
    }
    
    // Lấy thông báo theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ThongBaoDTO> getThongBaoById(@PathVariable Integer id) {
        return thongBaoService.getThongBaoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Lấy thông báo theo người dùng
    @GetMapping("/user/{maNguoiNhan}")
    public ResponseEntity<List<ThongBaoDTO>> getThongBaoByUser(@PathVariable Integer maNguoiNhan) {
        return ResponseEntity.ok(thongBaoService.getThongBaoByUser(maNguoiNhan));
    }
    
    // Lấy thông báo chưa đọc
    @GetMapping("/user/{maNguoiNhan}/unread")
    public ResponseEntity<List<ThongBaoDTO>> getUnreadThongBao(@PathVariable Integer maNguoiNhan) {
        return ResponseEntity.ok(thongBaoService.getUnreadThongBao(maNguoiNhan));
    }
    
    // Đếm số thông báo chưa đọc
    @GetMapping("/user/{maNguoiNhan}/unread-count")
    public ResponseEntity<Map<String, Long>> countUnreadThongBao(@PathVariable Integer maNguoiNhan) {
        Long count = thongBaoService.countUnreadThongBao(maNguoiNhan);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // Lấy thông báo theo loại
    @GetMapping("/user/{maNguoiNhan}/type/{loaiThongBao}")
    public ResponseEntity<List<ThongBaoDTO>> getThongBaoByType(
            @PathVariable Integer maNguoiNhan,
            @PathVariable String loaiThongBao) {
        return ResponseEntity.ok(thongBaoService.getThongBaoByType(maNguoiNhan, loaiThongBao));
    }
    
    // Tạo thông báo mới
    @PostMapping
    public ResponseEntity<ThongBao> createThongBao(@RequestBody ThongBao thongBao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(thongBaoService.createThongBao(thongBao));
    }
    
    // Cập nhật thông báo
    @PutMapping("/{id}")
    public ResponseEntity<ThongBao> updateThongBao(@PathVariable Integer id, @RequestBody ThongBao thongBao) {
        return ResponseEntity.ok(thongBaoService.updateThongBao(id, thongBao));
    }
    
    // Đánh dấu đã đọc
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<ThongBaoDTO> markAsRead(@PathVariable Integer id) {
        ThongBao thongBao = thongBaoService.markAsRead(id);
        return ResponseEntity.ok(thongBaoService.getThongBaoById(id).orElse(null));
    }
    
    // Đánh dấu tất cả đã đọc
    @PutMapping("/user/{maNguoiNhan}/mark-all-read")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable Integer maNguoiNhan) {
        thongBaoService.markAllAsRead(maNguoiNhan);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã đánh dấu tất cả thông báo là đã đọc");
        return ResponseEntity.ok(response);
    }
    
    // Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThongBao(@PathVariable Integer id) {
        thongBaoService.deleteThongBao(id);
        return ResponseEntity.noContent().build();
    }
    
    // Xóa tất cả thông báo đã đọc
    @DeleteMapping("/user/{maNguoiNhan}/delete-read")
    public ResponseEntity<Map<String, String>> deleteAllReadNotifications(@PathVariable Integer maNguoiNhan) {
        thongBaoService.deleteAllReadNotifications(maNguoiNhan);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã xóa tất cả thông báo đã đọc");
        return ResponseEntity.ok(response);
    }
    
    // API test: Tạo thông báo nhắc nhở trước 1 ngày (gọi thủ công)
    @GetMapping("/trigger/truoc-1-ngay")
    public ResponseEntity<Map<String, String>> triggerThongBaoTruoc1Ngay() {
        thongBaoSchedulerService.taoThongBaoTruoc1Ngay();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã chạy tạo thông báo nhắc nhở trước 1 ngày");
        return ResponseEntity.ok(response);
    }
    
    // API test: Tạo thông báo nhắc nhở trước 30 phút (gọi thủ công)
    @GetMapping("/trigger/truoc-30-phut")
    public ResponseEntity<Map<String, String>> triggerThongBaoTruoc30Phut() {
        thongBaoSchedulerService.taoThongBaoTruoc30Phut();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã chạy tạo thông báo nhắc nhở trước 30 phút");
        return ResponseEntity.ok(response);
    }
    
    // API test: Tạo thông báo test không kiểm tra trùng
    @GetMapping("/trigger/test-truoc-1-ngay")
    public ResponseEntity<Map<String, Object>> testTriggerThongBao() {
        Map<String, Object> response = new HashMap<>();
        try {
            int count = thongBaoSchedulerService.taoThongBaoTruoc1NgayKhongKiemTra();
            response.put("success", true);
            response.put("message", "Đã tạo " + count + " thông báo");
            response.put("count", count);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
