package com.android.be.controller;

import com.android.be.dto.YeuThichDTO;
import com.android.be.service.YeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/yeuthich")
public class YeuThichController {
    
    @Autowired
    private YeuThichService yeuThichService;
    
    /**
     * Lấy danh sách khóa học yêu thích của học viên (trả về KhoaHoc đầy đủ)
     * GET /api/yeuthich/hocvien/{maHocVien}/khoahoc
     */
    @GetMapping("/hocvien/{maHocVien}/khoahoc")
    public ResponseEntity<List<com.android.be.model.KhoaHoc>> getFavoriteKhoaHocByHocVien(@PathVariable Integer maHocVien) {
        try {
            List<com.android.be.model.KhoaHoc> favorites = yeuThichService.getFavoriteKhoaHocByHocVien(maHocVien);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lấy danh sách khóa học yêu thích của học viên
     * GET /api/yeuthich/hocvien/{maHocVien}
     */
    @GetMapping("/hocvien/{maHocVien}")
    public ResponseEntity<List<YeuThichDTO>> getFavoritesByHocVien(@PathVariable Integer maHocVien) {
        try {
            List<YeuThichDTO> favorites = yeuThichService.getFavoritesByHocVien(maHocVien);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kiểm tra khóa học đã được yêu thích chưa
     * GET /api/yeuthich/check?maHocVien=4&maKhoaHoc=1
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @RequestParam Integer maHocVien,
            @RequestParam Integer maKhoaHoc) {
        try {
            boolean isFavorite = yeuThichService.isFavorite(maHocVien, maKhoaHoc);
            Map<String, Boolean> response = new HashMap<>();
            response.put("isFavorite", isFavorite);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Thêm khóa học vào danh sách yêu thích
     * POST /api/yeuthich
     * Body: { "maHocVien": 4, "maKhoaHoc": 1 }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody Map<String, Integer> request) {
        try {
            Integer maHocVien = request.get("maHocVien");
            Integer maKhoaHoc = request.get("maKhoaHoc");
            
            YeuThichDTO favorite = yeuThichService.addFavorite(maHocVien, maKhoaHoc);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã thêm vào danh sách yêu thích");
            response.put("data", favorite);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Xóa khóa học khỏi danh sách yêu thích
     * DELETE /api/yeuthich?maHocVien=4&maKhoaHoc=1
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> removeFavorite(
            @RequestParam Integer maHocVien,
            @RequestParam Integer maKhoaHoc) {
        try {
            yeuThichService.removeFavorite(maHocVien, maKhoaHoc);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã xóa khỏi danh sách yêu thích");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Toggle yêu thích (thêm nếu chưa có, xóa nếu đã có)
     * POST /api/yeuthich/toggle
     * Body: { "maHocVien": 4, "maKhoaHoc": 1 }
     */
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestBody Map<String, Integer> request) {
        try {
            Integer maHocVien = request.get("maHocVien");
            Integer maKhoaHoc = request.get("maKhoaHoc");
            
            boolean isAdded = yeuThichService.toggleFavorite(maHocVien, maKhoaHoc);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("isFavorite", isAdded);
            response.put("message", isAdded ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Đếm số lượng yêu thích của một khóa học
     * GET /api/yeuthich/count/{maKhoaHoc}
     */
    @GetMapping("/count/{maKhoaHoc}")
    public ResponseEntity<Map<String, Long>> countFavorites(@PathVariable Integer maKhoaHoc) {
        try {
            Long count = yeuThichService.countFavoritesByKhoaHoc(maKhoaHoc);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
