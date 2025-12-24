package com.android.be.controller;

import com.android.be.dto.HinhAnhKhoaHocDTO;
import com.android.be.service.FileStorageService;
import com.android.be.service.HinhAnhKhoaHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hinhanh-khoahoc")
@RequiredArgsConstructor
public class HinhAnhKhoaHocController {
    
    private final HinhAnhKhoaHocService hinhAnhKhoaHocService;
    private final FileStorageService fileStorageService;
    
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<HinhAnhKhoaHocDTO>> getHinhAnhByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(hinhAnhKhoaHocService.getHinhAnhByKhoaHoc(maKhoaHoc));
    }
    
    /**
     * Upload hình ảnh khóa học (multipart/form-data)
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> uploadHinhAnh(
            @RequestParam("file") MultipartFile file,
            @RequestParam("maKhoaHoc") Integer maKhoaHoc,
            @RequestParam(value = "thuTu", defaultValue = "1") Integer thuTu) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File không được để trống");
            }
            
            // Lưu file vào thư mục courses
            String fileName = fileStorageService.storeFile(file, "courses");
            
            // Lưu vào database
            HinhAnhKhoaHocDTO dto = new HinhAnhKhoaHocDTO();
            dto.setMaKhoaHoc(maKhoaHoc);
            dto.setDuongDan(fileName);
            dto.setThuTu(thuTu);
            
            HinhAnhKhoaHocDTO saved = hinhAnhKhoaHocService.createHinhAnh(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi upload: " + e.getMessage());
        }
    }
    
    /**
     * Tạo hình ảnh (JSON - cho trường hợp đã có đường dẫn)
     */
    @PostMapping(value = "/json", consumes = "application/json")
    public ResponseEntity<HinhAnhKhoaHocDTO> createHinhAnhJson(@RequestBody HinhAnhKhoaHocDTO dto) {
        return ResponseEntity.ok(hinhAnhKhoaHocService.createHinhAnh(dto));
    }
    
    @DeleteMapping("/{maHinhAnh}")
    public ResponseEntity<Void> deleteHinhAnh(@PathVariable Integer maHinhAnh) {
        hinhAnhKhoaHocService.deleteHinhAnh(maHinhAnh);
        return ResponseEntity.ok().build();
    }
}
