package com.android.be.controller;

import com.android.be.model.LichHoc;
import com.android.be.service.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lichhoc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LichHocController {
    
    private final LichHocService lichHocService;
    
    @GetMapping
    public ResponseEntity<List<LichHoc>> getAllLichHoc() {
        return ResponseEntity.ok(lichHocService.getAllLichHoc());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LichHoc> getLichHocById(@PathVariable Integer id) {
        return lichHocService.getLichHocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<LichHoc> createLichHoc(@RequestBody LichHoc lichHoc) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lichHocService.createLichHoc(lichHoc));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LichHoc> updateLichHoc(@PathVariable Integer id, @RequestBody LichHoc lichHoc) {
        return ResponseEntity.ok(lichHocService.updateLichHoc(id, lichHoc));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLichHoc(@PathVariable Integer id) {
        lichHocService.deleteLichHoc(id);
        return ResponseEntity.noContent().build();
    }
}
