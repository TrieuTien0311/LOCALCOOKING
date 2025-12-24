package com.android.be.controller;

import com.android.be.dto.KhoaHocDTO;
import com.android.be.model.KhoaHoc;
import com.android.be.service.KhoaHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khoahoc")
@RequiredArgsConstructor
public class KhoaHocController {
    
    private final KhoaHocService khoaHocService;
    
    @GetMapping
    public ResponseEntity<List<KhoaHocDTO>> getAllKhoaHoc() {
        return ResponseEntity.ok(khoaHocService.getAllKhoaHoc());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<KhoaHocDTO> getKhoaHocById(@PathVariable Integer id) {
        return khoaHocService.getKhoaHocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<KhoaHoc> createKhoaHoc(@RequestBody KhoaHoc khoaHoc) {
        return ResponseEntity.ok(khoaHocService.createKhoaHoc(khoaHoc));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<KhoaHoc> updateKhoaHoc(@PathVariable Integer id, @RequestBody KhoaHoc khoaHoc) {
        return ResponseEntity.ok(khoaHocService.updateKhoaHoc(id, khoaHoc));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKhoaHoc(@PathVariable Integer id) {
        khoaHocService.deleteKhoaHoc(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Tìm kiếm khóa học theo địa điểm và ngày (sử dụng stored procedure)
     * Endpoint: GET /api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
     */
    @GetMapping("/search")
    public ResponseEntity<List<KhoaHocDTO>> searchKhoaHoc(
            @RequestParam String diaDiem,
            @RequestParam(required = false) String ngayTimKiem) {
        
        if (ngayTimKiem != null && !ngayTimKiem.isEmpty()) {
            return ResponseEntity.ok(khoaHocService.searchByDiaDiemAndDate(diaDiem, ngayTimKiem));
        } else {
            return ResponseEntity.ok(khoaHocService.searchByDiaDiem(diaDiem));
        }
    }
}
