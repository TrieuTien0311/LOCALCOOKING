package com.android.be.controller;

import com.android.be.model.ThanhToan;
import com.android.be.service.ThanhToanService;
import lombok.RequiredArgsConstructor;
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
}
