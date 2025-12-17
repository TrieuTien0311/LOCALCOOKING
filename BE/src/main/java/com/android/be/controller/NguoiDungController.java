package com.android.be.controller;

import com.android.be.model.NguoiDung;
import com.android.be.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nguoidung")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NguoiDungController {
    
    private final NguoiDungService nguoiDungService;
    
    @GetMapping
    public ResponseEntity<List<NguoiDung>> getAllNguoiDung() {
        return ResponseEntity.ok(nguoiDungService.getAllNguoiDung());
    }
}
