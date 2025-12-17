package com.android.be.controller;

import com.android.be.model.ThanhToan;
import com.android.be.service.ThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/thanhtoan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThanhToanController {
    
    private final ThanhToanService thanhToanService;
    
    @GetMapping
    public ResponseEntity<List<ThanhToan>> getAllThanhToan() {
        return ResponseEntity.ok(thanhToanService.getAllThanhToan());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ThanhToan> getThanhToanById(@PathVariable Integer id) {
        return thanhToanService.getThanhToanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ThanhToan> createThanhToan(@RequestBody ThanhToan thanhToan) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(thanhToanService.createThanhToan(thanhToan));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ThanhToan> updateThanhToan(@PathVariable Integer id, @RequestBody ThanhToan thanhToan) {
        return ResponseEntity.ok(thanhToanService.updateThanhToan(id, thanhToan));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThanhToan(@PathVariable Integer id) {
        thanhToanService.deleteThanhToan(id);
        return ResponseEntity.noContent().build();
    }
}
