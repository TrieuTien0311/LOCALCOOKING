package com.android.be.controller;

import com.android.be.model.ThongBao;
import com.android.be.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/thongbao")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThongBaoController {
    
    private final ThongBaoService thongBaoService;
    
    @GetMapping
    public ResponseEntity<List<ThongBao>> getAllThongBao() {
        return ResponseEntity.ok(thongBaoService.getAllThongBao());
    }
}
