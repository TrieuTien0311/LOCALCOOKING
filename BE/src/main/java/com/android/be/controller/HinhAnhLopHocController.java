package com.android.be.controller;

import com.android.be.model.HinhAnhLopHoc;
import com.android.be.service.HinhAnhLopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hinhanhlophoc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HinhAnhLopHocController {
    
    private final HinhAnhLopHocService hinhAnhLopHocService;
    
    @GetMapping
    public ResponseEntity<List<HinhAnhLopHoc>> getAllHinhAnhLopHoc() {
        return ResponseEntity.ok(hinhAnhLopHocService.getAllHinhAnhLopHoc());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HinhAnhLopHoc> getHinhAnhLopHocById(@PathVariable Integer id) {
        return hinhAnhLopHocService.getHinhAnhLopHocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<HinhAnhLopHoc> createHinhAnhLopHoc(@RequestBody HinhAnhLopHoc hinhAnhLopHoc) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hinhAnhLopHocService.createHinhAnhLopHoc(hinhAnhLopHoc));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HinhAnhLopHoc> updateHinhAnhLopHoc(@PathVariable Integer id, @RequestBody HinhAnhLopHoc hinhAnhLopHoc) {
        return ResponseEntity.ok(hinhAnhLopHocService.updateHinhAnhLopHoc(id, hinhAnhLopHoc));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHinhAnhLopHoc(@PathVariable Integer id) {
        hinhAnhLopHocService.deleteHinhAnhLopHoc(id);
        return ResponseEntity.noContent().build();
    }
}
