package com.android.be.controller;

import com.android.be.dto.ThongBaoDTO;
import com.android.be.model.ThongBao;
import com.android.be.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ThongBaoDTO>> getAllThongBao() {
        return ResponseEntity.ok(thongBaoService.getAllThongBao());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ThongBaoDTO> getThongBaoById(@PathVariable Integer id) {
        return thongBaoService.getThongBaoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ThongBao> createThongBao(@RequestBody ThongBao thongBao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(thongBaoService.createThongBao(thongBao));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ThongBao> updateThongBao(@PathVariable Integer id, @RequestBody ThongBao thongBao) {
        return ResponseEntity.ok(thongBaoService.updateThongBao(id, thongBao));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThongBao(@PathVariable Integer id) {
        thongBaoService.deleteThongBao(id);
        return ResponseEntity.noContent().build();
    }
}
