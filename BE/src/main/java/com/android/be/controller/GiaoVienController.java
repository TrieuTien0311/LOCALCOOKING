package com.android.be.controller;

import com.android.be.dto.GiaoVienDTO;
import com.android.be.service.GiaoVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/giaovien")
@RequiredArgsConstructor
public class GiaoVienController {
    
    private final GiaoVienService giaoVienService;
    
    @GetMapping
    public ResponseEntity<List<GiaoVienDTO>> getAllGiaoVien() {
        List<GiaoVienDTO> list = giaoVienService.getAllGiaoVien();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GiaoVienDTO> getGiaoVienById(@PathVariable Integer id) {
        return giaoVienService.getGiaoVienById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<GiaoVienDTO> createGiaoVien(@RequestBody GiaoVienDTO dto) {
        GiaoVienDTO created = giaoVienService.createGiaoVien(dto);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GiaoVienDTO> updateGiaoVien(@PathVariable Integer id, @RequestBody GiaoVienDTO dto) {
        GiaoVienDTO updated = giaoVienService.updateGiaoVien(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiaoVien(@PathVariable Integer id) {
        boolean deleted = giaoVienService.deleteGiaoVien(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
