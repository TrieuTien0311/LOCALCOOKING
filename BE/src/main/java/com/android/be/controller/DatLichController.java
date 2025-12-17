package com.android.be.controller;

import com.android.be.model.DatLich;
import com.android.be.service.DatLichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/datlich")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DatLichController {
    
    private final DatLichService datLichService;
    
    @GetMapping
    public ResponseEntity<List<DatLich>> getAllDatLich() {
        return ResponseEntity.ok(datLichService.getAllDatLich());
    }
}
