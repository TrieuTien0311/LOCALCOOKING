package com.android.be.controller;

import com.android.be.model.LichTrinhLopHoc;
import com.android.be.service.LichTrinhLopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lichtrinh")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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
    public ResponseEntity<Object[]> checkAvailableSeats(
            @RequestParam Integer maLichTrinh,
            @RequestParam String ngayThamGia) {
        return ResponseEntity.ok(lichTrinhService.checkAvailableSeats(maLichTrinh, ngayThamGia));
    }
}
