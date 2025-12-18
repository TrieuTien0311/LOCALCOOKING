package com.android.be.controller;

import com.android.be.dto.HinhAnhMonAnDTO;
import com.android.be.service.HinhAnhMonAnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hinhanh-monan")
public class HinhAnhMonAnController {
    
    @Autowired
    private HinhAnhMonAnService hinhAnhMonAnService;
    
    @GetMapping("/monan/{maMonAn}")
    public ResponseEntity<List<HinhAnhMonAnDTO>> getHinhAnhByMonAn(@PathVariable Integer maMonAn) {
        return ResponseEntity.ok(hinhAnhMonAnService.getHinhAnhByMonAn(maMonAn));
    }
    
    @PostMapping
    public ResponseEntity<HinhAnhMonAnDTO> createHinhAnh(@RequestBody HinhAnhMonAnDTO dto) {
        return ResponseEntity.ok(hinhAnhMonAnService.createHinhAnh(dto));
    }
    
    @DeleteMapping("/{maHinhAnh}")
    public ResponseEntity<Void> deleteHinhAnh(@PathVariable Integer maHinhAnh) {
        hinhAnhMonAnService.deleteHinhAnh(maHinhAnh);
        return ResponseEntity.ok().build();
    }
}
