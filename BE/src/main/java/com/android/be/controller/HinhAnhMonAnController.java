package com.android.be.controller;

import com.android.be.dto.HinhAnhMonAnDTO;
import com.android.be.service.HinhAnhMonAnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hinhanh-monan")
public class HinhAnhMonAnController {
    
    @Autowired
    private HinhAnhMonAnService hinhAnhMonAnService;
    
    @Value("${file.upload-dir:uploads}")
    private String uploadPath;
    
    @GetMapping("/monan/{maMonAn}")
    public ResponseEntity<List<HinhAnhMonAnDTO>> getHinhAnhByMonAn(@PathVariable Integer maMonAn) {
        return ResponseEntity.ok(hinhAnhMonAnService.getHinhAnhByMonAn(maMonAn));
    }
    
    /**
     * Upload hình ảnh món ăn (multipart/form-data)
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> uploadHinhAnh(
            @RequestParam("file") MultipartFile file,
            @RequestParam("maMonAn") Integer maMonAn,
            @RequestParam(value = "thuTu", defaultValue = "1") Integer thuTu) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File không được để trống");
            }
            
            // Tạo tên file unique
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = "monan_" + maMonAn + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            
            // Tạo thư mục dishes nếu chưa có
            Path uploadDir = Paths.get(uploadPath, "dishes");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Lưu file vào thư mục dishes
            Path filePath = uploadDir.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Lưu vào database
            HinhAnhMonAnDTO dto = new HinhAnhMonAnDTO();
            dto.setMaMonAn(maMonAn);
            dto.setDuongDan(newFilename);
            dto.setThuTu(thuTu);
            
            HinhAnhMonAnDTO saved = hinhAnhMonAnService.createHinhAnh(dto);
            return ResponseEntity.ok(saved);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi upload file: " + e.getMessage());
        }
    }
    
    /**
     * Tạo hình ảnh món ăn (JSON - cho trường hợp đã có đường dẫn)
     */
    @PostMapping(value = "/json", consumes = "application/json")
    public ResponseEntity<HinhAnhMonAnDTO> createHinhAnhJson(@RequestBody HinhAnhMonAnDTO dto) {
        return ResponseEntity.ok(hinhAnhMonAnService.createHinhAnh(dto));
    }
    
    @DeleteMapping("/{maHinhAnh}")
    public ResponseEntity<Void> deleteHinhAnh(@PathVariable Integer maHinhAnh) {
        hinhAnhMonAnService.deleteHinhAnh(maHinhAnh);
        return ResponseEntity.ok().build();
    }
}
