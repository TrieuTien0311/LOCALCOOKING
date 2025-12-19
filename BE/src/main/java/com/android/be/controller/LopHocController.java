package com.android.be.controller;

import com.android.be.dto.LopHocDTO;
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
    
    @GetMapping
    public ResponseEntity<List<LopHocDTO>> getAllLopHoc() {
        return ResponseEntity.ok(lopHocService.getAllLopHoc());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LopHocDTO> getLopHocById(@PathVariable Integer id) {
        return lopHocService.getLopHocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<LopHoc> createLopHoc(@RequestBody LopHoc lopHoc) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lopHocService.createLopHoc(lopHoc));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LopHoc> updateLopHoc(@PathVariable Integer id, @RequestBody LopHoc lopHoc) {
        return ResponseEntity.ok(lopHocService.updateLopHoc(id, lopHoc));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLopHoc(@PathVariable Integer id) {
        lopHocService.deleteLopHoc(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<LopHocDTO>> searchLopHocByDiaDiem(
            @RequestParam String diaDiem,
            @RequestParam(required = false) String ngayTimKiem) {
        
        if (ngayTimKiem != null && !ngayTimKiem.isEmpty()) {
            // Tìm kiếm theo địa điểm và ngày cụ thể
            java.time.LocalDate date = java.time.LocalDate.parse(ngayTimKiem);
            return ResponseEntity.ok(lopHocService.searchLopHocByDiaDiemAndDate(diaDiem, date));
        } else {
            // Chỉ tìm kiếm theo địa điểm (lấy các lớp còn hiệu lực)
            return ResponseEntity.ok(lopHocService.searchLopHocByDiaDiem(diaDiem));
        }
    }
}
