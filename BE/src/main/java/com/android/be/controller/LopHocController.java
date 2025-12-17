package com.android.be.controller;

import com.android.be.model.LopHoc;
import com.android.be.service.LopHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lophoc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LopHocController {
    
    private final LopHocService lopHocService;
    
    @GetMapping
    public ResponseEntity<List<LopHoc>> getAllLopHoc() {
        return ResponseEntity.ok(lopHocService.getAllLopHoc());
    }
}
