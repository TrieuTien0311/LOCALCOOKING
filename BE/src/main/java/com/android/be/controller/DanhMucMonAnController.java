package com.android.be.controller;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.service.DanhMucMonAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danhmucmonan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DanhMucMonAnController {
    
    private final DanhMucMonAnService danhMucMonAnService;
    
    /**
     * Lấy danh mục món ăn theo khóa học
     * Endpoint: GET /api/danhmucmonan/khoahoc/{maKhoaHoc}
     */
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<DanhMucMonAnDTO>> getDanhMucMonAnByKhoaHoc(
            @PathVariable Integer maKhoaHoc) {
        List<DanhMucMonAnDTO> result = danhMucMonAnService.getDanhMucMonAnByKhoaHoc(maKhoaHoc);
        return ResponseEntity.ok(result);
    }
}
