package com.android.be.controller;

import com.android.be.dto.LoginRequest;
import com.android.be.dto.LoginResponse;
import com.android.be.model.NguoiDung;
import com.android.be.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nguoidung")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NguoiDungController {
    
    private final NguoiDungService nguoiDungService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = nguoiDungService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<NguoiDung>> getAllNguoiDung() {
        return ResponseEntity.ok(nguoiDungService.getAllNguoiDung());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getNguoiDungById(@PathVariable Integer id) {
        return nguoiDungService.getNguoiDungById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<NguoiDung> createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nguoiDungService.createNguoiDung(nguoiDung));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateNguoiDung(@PathVariable Integer id, @RequestBody NguoiDung nguoiDung) {
        return ResponseEntity.ok(nguoiDungService.updateNguoiDung(id, nguoiDung));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNguoiDung(@PathVariable Integer id) {
        nguoiDungService.deleteNguoiDung(id);
        return ResponseEntity.noContent().build();
    }
}
