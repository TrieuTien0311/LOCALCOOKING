package com.android.be.controller;

import com.android.be.dto.GiaoVienDTO;
import com.android.be.service.GiaoVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/giaovien")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GiaoVienController {
    
    private final GiaoVienService giaoVienService;
    
    @GetMapping("/{id}")
    public ResponseEntity<GiaoVienDTO> getGiaoVienById(@PathVariable Integer id) {
        return giaoVienService.getGiaoVienById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
