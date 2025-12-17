package com.android.be.controller;

import com.android.be.model.DanhGia;
import com.android.be.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<DanhGia>> getAllDanhGia() {
        return ResponseEntity.ok(danhGiaService.getAllDanhGia());
    }
}
