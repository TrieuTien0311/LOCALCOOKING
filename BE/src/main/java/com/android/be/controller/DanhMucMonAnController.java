package com.android.be.controller;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.model.DanhMucMonAn;
import com.android.be.repository.DanhMucMonAnRepository;
import com.android.be.service.DanhMucMonAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danhmucmonan")
@RequiredArgsConstructor
public class DanhMucMonAnController {
    
    private final DanhMucMonAnService danhMucMonAnService;
    private final DanhMucMonAnRepository danhMucMonAnRepository;
    
    /**
     * Lấy tất cả danh mục món ăn
     * Endpoint: GET /api/danhmucmonan
     */
    @GetMapping
    public ResponseEntity<List<DanhMucMonAn>> getAllDanhMuc() {
        List<DanhMucMonAn> result = danhMucMonAnRepository.findAllByOrderByThuTuAsc();
        return ResponseEntity.ok(result);
    }
    
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
