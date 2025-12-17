package com.android.be.controller;

import com.android.be.model.GiaoVien;
import com.android.be.service.GiaoVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/giaovien")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GiaoVienController {
    
    private final GiaoVienService giaoVienService;
    
    @GetMapping
    public ResponseEntity<List<GiaoVien>> getAllGiaoVien() {
        return ResponseEntity.ok(giaoVienService.getAllGiaoVien());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GiaoVien> getGiaoVienById(@PathVariable Integer id) {
        return giaoVienService.getGiaoVienById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<GiaoVien> createGiaoVien(@RequestBody GiaoVien giaoVien) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(giaoVienService.createGiaoVien(giaoVien));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GiaoVien> updateGiaoVien(@PathVariable Integer id, @RequestBody GiaoVien giaoVien) {
        return ResponseEntity.ok(giaoVienService.updateGiaoVien(id, giaoVien));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiaoVien(@PathVariable Integer id) {
        giaoVienService.deleteGiaoVien(id);
        return ResponseEntity.noContent().build();
    }
}
