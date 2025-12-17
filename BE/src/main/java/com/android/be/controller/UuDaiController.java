package com.android.be.controller;

import com.android.be.dto.UuDaiDTO;
import com.android.be.model.UuDai;
import com.android.be.service.UuDaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/uudai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UuDaiController {
    
    private final UuDaiService uuDaiService;
    
    @GetMapping
    public ResponseEntity<List<UuDaiDTO>> getAllUuDai() {
        return ResponseEntity.ok(uuDaiService.getAllUuDai());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UuDaiDTO> getUuDaiById(@PathVariable Integer id) {
        return uuDaiService.getUuDaiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UuDai> createUuDai(@RequestBody UuDai uuDai) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uuDaiService.createUuDai(uuDai));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UuDai> updateUuDai(@PathVariable Integer id, @RequestBody UuDai uuDai) {
        return ResponseEntity.ok(uuDaiService.updateUuDai(id, uuDai));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUuDai(@PathVariable Integer id) {
        uuDaiService.deleteUuDai(id);
        return ResponseEntity.noContent().build();
    }
}
