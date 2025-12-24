package com.android.be.controller;

import com.android.be.dto.CheckSeatsResponse;
import com.android.be.dto.LichTrinhLopHocDTO;
import com.android.be.model.LichTrinhLopHoc;
import com.android.be.service.LichTrinhLopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lichtrinh")
@RequiredArgsConstructor
public class LichTrinhLopHocController {
    
    private final LichTrinhLopHocService lichTrinhService;
    
    // GET - Lấy tất cả lịch trình
    @GetMapping
    public ResponseEntity<List<LichTrinhLopHoc>> getAllLichTrinh() {
        return ResponseEntity.ok(lichTrinhService.getAllLichTrinh());
    }
    
    // GET - Lấy lịch trình theo ID
    @GetMapping("/{id}")
    public ResponseEntity<LichTrinhLopHoc> getLichTrinhById(@PathVariable Integer id) {
        return lichTrinhService.getLichTrinhById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET - Lấy lịch trình theo ID với thông tin đầy đủ (DTO có số chỗ trống)
    @GetMapping("/{id}/detail")
    public ResponseEntity<LichTrinhLopHocDTO> getLichTrinhDetailById(
            @PathVariable Integer id,
            @RequestParam(required = false) String ngayThamGia) {
        
        // Parse ngày tham gia nếu có
        java.time.LocalDate ngay = null;
        if (ngayThamGia != null && !ngayThamGia.isEmpty()) {
            try {
                ngay = java.time.LocalDate.parse(ngayThamGia);
            } catch (Exception e) {
                // Nếu parse lỗi, để null (sẽ dùng ngày mai)
            }
        }
        
        return lichTrinhService.getLichTrinhDTOById(id, ngay)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET - Lấy lịch trình theo khóa học
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<LichTrinhLopHoc>> getLichTrinhByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(lichTrinhService.getLichTrinhByKhoaHoc(maKhoaHoc));
    }
    
    // GET - Lấy lịch trình theo giáo viên
    @GetMapping("/giaovien/{maGiaoVien}")
    public ResponseEntity<List<LichTrinhLopHoc>> getLichTrinhByGiaoVien(@PathVariable Integer maGiaoVien) {
        return ResponseEntity.ok(lichTrinhService.getLichTrinhByGiaoVien(maGiaoVien));
    }
    
    // GET - Lấy lịch trình theo địa điểm
    @GetMapping("/diadiem")
    public ResponseEntity<List<LichTrinhLopHoc>> getLichTrinhByDiaDiem(@RequestParam String diaDiem) {
        return ResponseEntity.ok(lichTrinhService.getLichTrinhByDiaDiem(diaDiem));
    }
    
    // GET - Lấy lịch trình đang hoạt động
    @GetMapping("/active")
    public ResponseEntity<List<LichTrinhLopHoc>> getLichTrinhActive() {
        return ResponseEntity.ok(lichTrinhService.getLichTrinhActive());
    }
    
    // POST - Tạo lịch trình mới
    @PostMapping
    public ResponseEntity<LichTrinhLopHoc> createLichTrinh(@RequestBody LichTrinhLopHoc lichTrinh) {
        return ResponseEntity.ok(lichTrinhService.createLichTrinh(lichTrinh));
    }
    
    // PUT - Cập nhật lịch trình
    @PutMapping("/{id}")
    public ResponseEntity<LichTrinhLopHoc> updateLichTrinh(
            @PathVariable Integer id, 
            @RequestBody LichTrinhLopHoc lichTrinh) {
        return ResponseEntity.ok(lichTrinhService.updateLichTrinh(id, lichTrinh));
    }
    
    // DELETE - Xóa lịch trình
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLichTrinh(@PathVariable Integer id) {
        lichTrinhService.deleteLichTrinh(id);
        return ResponseEntity.ok().build();
    }
    
    // GET - Lấy danh sách lớp theo ngày (Stored Procedure)
    @GetMapping("/by-date")
    public ResponseEntity<List<Object[]>> getClassesByDate(@RequestParam String ngayCanXem) {
        return ResponseEntity.ok(lichTrinhService.getClassesByDate(ngayCanXem));
    }
    
    // GET - Kiểm tra chỗ trống (Stored Procedure)
    @GetMapping("/check-seats")
    public ResponseEntity<CheckSeatsResponse> checkAvailableSeats(
            @RequestParam Integer maLichTrinh,
            @RequestParam String ngayThamGia) {
        try {
            Object[] result = lichTrinhService.checkAvailableSeats(maLichTrinh, ngayThamGia);
            
            if (result != null && result.length > 0) {
                // Parse result từ stored procedure
                // sp_KiemTraChoTrong trả về: maLichTrinh, maKhoaHoc, tenKhoaHoc, TongCho, DaDat, ConTrong, TrangThai
                Integer maLT = result[0] != null ? ((Number) result[0]).intValue() : null;
                Integer maKH = result[1] != null ? ((Number) result[1]).intValue() : null;
                String tenKH = result[2] != null ? result[2].toString() : null;
                Integer tongCho = result[3] != null ? ((Number) result[3]).intValue() : 0;
                Integer daDat = result[4] != null ? ((Number) result[4]).intValue() : 0;
                Integer conTrong = result[5] != null ? ((Number) result[5]).intValue() : 0;
                String trangThai = result[6] != null ? result[6].toString() : "Không xác định";
                
                CheckSeatsResponse response = new CheckSeatsResponse(
                    true,
                    "Kiểm tra chỗ trống thành công",
                    maLT, maKH, tenKH, tongCho, daDat, conTrong, trangThai
                );
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(new CheckSeatsResponse(false, "Không tìm thấy thông tin lịch trình"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new CheckSeatsResponse(false, "Lỗi: " + e.getMessage()));
        }
    }
}
