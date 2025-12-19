package com.android.be.controller;

import com.android.be.model.DanhMucMonAn;
import com.android.be.service.DanhMucMonAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/danhmucmonan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DanhMucMonAnController {
    
    private final DanhMucMonAnService danhMucMonAnService;
    
    @GetMapping
    public ResponseEntity<List<DanhMucMonAn>> getAllDanhMucMonAn() {
        return ResponseEntity.ok(danhMucMonAnService.getAllDanhMucMonAn());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DanhMucMonAn> getDanhMucMonAnById(@PathVariable Integer id) {
        return danhMucMonAnService.getDanhMucMonAnById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DanhMucMonAn> createDanhMucMonAn(@RequestBody DanhMucMonAn danhMucMonAn) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(danhMucMonAnService.createDanhMucMonAn(danhMucMonAn));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DanhMucMonAn> updateDanhMucMonAn(@PathVariable Integer id, @RequestBody DanhMucMonAn danhMucMonAn) {
        return ResponseEntity.ok(danhMucMonAnService.updateDanhMucMonAn(id, danhMucMonAn));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhMucMonAn(@PathVariable Integer id) {
        danhMucMonAnService.deleteDanhMucMonAn(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<com.android.be.dto.DanhMucMonAnDTO>> getDanhMucMonAnByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(danhMucMonAnService.getDanhMucMonAnByKhoaHoc(maKhoaHoc));
    }
}
