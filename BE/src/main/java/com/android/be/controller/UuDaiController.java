package com.android.be.controller;

import com.android.be.model.UuDai;
import com.android.be.service.UuDaiService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<UuDai>> getAllUuDai() {
        return ResponseEntity.ok(uuDaiService.getAllUuDai());
    }
}
