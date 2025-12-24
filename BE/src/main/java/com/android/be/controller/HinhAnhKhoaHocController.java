package com.android.be.controller;

import com.android.be.dto.HinhAnhKhoaHocDTO;
import com.android.be.service.HinhAnhKhoaHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hinhanh-khoahoc")
@RequiredArgsConstructor
public class HinhAnhKhoaHocController {
    
    private final HinhAnhKhoaHocService hinhAnhKhoaHocService;
    
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<HinhAnhKhoaHocDTO>> getHinhAnhByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(hinhAnhKhoaHocService.getHinhAnhByKhoaHoc(maKhoaHoc));
    }
    
    @PostMapping
    public ResponseEntity<HinhAnhKhoaHocDTO> createHinhAnh(@RequestBody HinhAnhKhoaHocDTO dto) {
        return ResponseEntity.ok(hinhAnhKhoaHocService.createHinhAnh(dto));
    }
    
    @DeleteMapping("/{maHinhAnh}")
    public ResponseEntity<Void> deleteHinhAnh(@PathVariable Integer maHinhAnh) {
        hinhAnhKhoaHocService.deleteHinhAnh(maHinhAnh);
        return ResponseEntity.ok().build();
    }
}
