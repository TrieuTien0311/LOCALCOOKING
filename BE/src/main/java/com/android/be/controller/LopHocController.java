package com.android.be.controller;

import com.android.be.dto.LopHocResponse;
import com.android.be.model.LopHoc;
import com.android.be.service.LopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lophoc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LopHocController {
    
    private final LopHocService lopHocService;
    
    /**
     * GET /api/lophoc - Lấy tất cả lớp học (raw data)
     */
    @GetMapping
    public ResponseEntity<List<LopHoc>> getAllLopHoc() {
        return ResponseEntity.ok(lopHocService.getAllLopHoc());
    }
    
    /**
     * GET /api/lophoc/with-rating - Lấy tất cả lớp học kèm điểm đánh giá
     */
    @GetMapping("/with-rating")
    public ResponseEntity<List<LopHocResponse>> getAllLopHocWithRating() {
        return ResponseEntity.ok(lopHocService.getAllLopHocWithRating());
    }
    
    /**
     * GET /api/lophoc/{id} - Lấy chi tiết lớp học theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LopHoc> getLopHocById(@PathVariable Integer id) {
        return lopHocService.getLopHocById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/lophoc/{id}/detail - Lấy chi tiết lớp học kèm rating theo ID
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<LopHocResponse> getLopHocDetailById(@PathVariable Integer id) {
        return lopHocService.getLopHocResponseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/lophoc - Tạo lớp học mới
     * Body example:
     * {
     *   "tenLopHoc": "Test",
     *   "giaTien": 500000,
     *   "thoiGian": "18:00 - 20:00",
     *   "diaDiem": "Đà Nẵng",
     *   "gioBatDau": "18:00",
     *   "gioKetThuc": "20:00"
     * }
     */
    @PostMapping
    public ResponseEntity<LopHoc> createLopHoc(@RequestBody LopHoc lopHoc) {
        LopHoc created = lopHocService.createLopHoc(lopHoc);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/lophoc/{id} - Cập nhật lớp học
     */
    @PutMapping("/{id}")
    public ResponseEntity<LopHoc> updateLopHoc(
        @PathVariable Integer id,
        @RequestBody LopHoc lopHoc
    ) {
        return lopHocService.updateLopHoc(id, lopHoc)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE /api/lophoc/{id} - Xóa lớp học
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLopHoc(@PathVariable Integer id) {
        if (lopHocService.deleteLopHoc(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
