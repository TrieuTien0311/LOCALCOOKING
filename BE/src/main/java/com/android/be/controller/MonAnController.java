package com.android.be.controller;

import com.android.be.model.MonAn;
import com.android.be.service.MonAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monan")
@RequiredArgsConstructor
public class MonAnController {
    
    private final MonAnService monAnService;
    
    @GetMapping
    public ResponseEntity<List<MonAn>> getAllMonAn() {
        return ResponseEntity.ok(monAnService.getAllMonAn());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MonAn> getMonAnById(@PathVariable Integer id) {
        return monAnService.getMonAnById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MonAn> createMonAn(@RequestBody MonAn monAn) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(monAnService.createMonAn(monAn));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MonAn> updateMonAn(@PathVariable Integer id, @RequestBody MonAn monAn) {
        return ResponseEntity.ok(monAnService.updateMonAn(id, monAn));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonAn(@PathVariable Integer id) {
        monAnService.deleteMonAn(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<MonAn>> getMonAnByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(monAnService.getMonAnByKhoaHoc(maKhoaHoc));
    }
    
    @GetMapping("/danhmuc/{maDanhMuc}")
    public ResponseEntity<List<MonAn>> getMonAnByDanhMuc(@PathVariable Integer maDanhMuc) {
        return ResponseEntity.ok(monAnService.getMonAnByDanhMuc(maDanhMuc));
    }
}
