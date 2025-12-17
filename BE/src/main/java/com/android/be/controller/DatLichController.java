package com.android.be.controller;

import com.android.be.model.DatLich;
import com.android.be.service.DatLichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    
    @GetMapping("/{id}")
    public ResponseEntity<DatLich> getDatLichById(@PathVariable Integer id) {
        return datLichService.getDatLichById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DatLich> createDatLich(@RequestBody DatLich datLich) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(datLichService.createDatLich(datLich));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DatLich> updateDatLich(@PathVariable Integer id, @RequestBody DatLich datLich) {
        return ResponseEntity.ok(datLichService.updateDatLich(id, datLich));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatLich(@PathVariable Integer id) {
        datLichService.deleteDatLich(id);
        return ResponseEntity.noContent().build();
    }
}
