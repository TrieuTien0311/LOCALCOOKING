package com.android.be.controller;

import com.android.be.dto.DanhGiaDTO;
import com.android.be.model.DanhGia;
import com.android.be.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/danhgia")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DanhGiaController {
    
    private final DanhGiaService danhGiaService;
    
    @GetMapping
    public ResponseEntity<List<DanhGiaDTO>> getAllDanhGia() {
        return ResponseEntity.ok(danhGiaService.getAllDanhGia());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DanhGiaDTO> getDanhGiaById(@PathVariable Integer id) {
        return danhGiaService.getDanhGiaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DanhGia> createDanhGia(@RequestBody DanhGia danhGia) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(danhGiaService.createDanhGia(danhGia));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DanhGia> updateDanhGia(@PathVariable Integer id, @RequestBody DanhGia danhGia) {
        return ResponseEntity.ok(danhGiaService.updateDanhGia(id, danhGia));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhGia(@PathVariable Integer id) {
        danhGiaService.deleteDanhGia(id);
        return ResponseEntity.noContent().build();
    }
}
