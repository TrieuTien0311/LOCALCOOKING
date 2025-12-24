package com.android.be.controller;

import com.android.be.dto.*;
import com.android.be.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/danhgia")
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    /**
     * Kiểm tra trạng thái đánh giá của đơn đặt lịch
     * GET /api/danhgia/kiemtra/{maDatLich}
     */
    @GetMapping("/kiemtra/{maDatLich}")
    public ResponseEntity<KiemTraDanhGiaResponse> kiemTraDaDanhGia(@PathVariable Integer maDatLich) {
        KiemTraDanhGiaResponse response = danhGiaService.kiemTraDaDanhGia(maDatLich);
        return ResponseEntity.ok(response);
    }

    /**
     * Tạo đánh giá mới
     * POST /api/danhgia/tao
     */
    @PostMapping("/tao")
    public ResponseEntity<?> taoDanhGia(@RequestBody TaoDanhGiaRequest request) {
        try {
            DanhGiaDTO danhGia = danhGiaService.taoDanhGia(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đánh giá thành công");
            response.put("data", danhGia);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Lấy đánh giá theo mã đặt lịch
     * GET /api/danhgia/datlich/{maDatLich}
     */
    @GetMapping("/datlich/{maDatLich}")
    public ResponseEntity<DanhGiaDTO> getDanhGiaByDatLich(@PathVariable Integer maDatLich) {
        return danhGiaService.getDanhGiaByMaDatLich(maDatLich)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách đánh giá theo khóa học
     * GET /api/danhgia/khoahoc/{maKhoaHoc}
     */
    @GetMapping("/khoahoc/{maKhoaHoc}")
    public ResponseEntity<List<DanhGiaDTO>> getDanhGiaByKhoaHoc(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(danhGiaService.getDanhGiaByKhoaHoc(maKhoaHoc));
    }

    /**
     * Lấy thống kê đánh giá của khóa học
     * GET /api/danhgia/thongke/{maKhoaHoc}
     */
    @GetMapping("/thongke/{maKhoaHoc}")
    public ResponseEntity<ThongKeDanhGiaDTO> getThongKeDanhGia(@PathVariable Integer maKhoaHoc) {
        return ResponseEntity.ok(danhGiaService.getThongKeDanhGia(maKhoaHoc));
    }

    /**
     * Lấy danh sách đánh giá với filter
     * GET /api/danhgia/khoahoc/{maKhoaHoc}/filter?type=co_nhan_xet&sao=5
     */
    @GetMapping("/khoahoc/{maKhoaHoc}/filter")
    public ResponseEntity<List<DanhGiaDTO>> getDanhGiaWithFilter(
            @PathVariable Integer maKhoaHoc,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer sao) {
        return ResponseEntity.ok(danhGiaService.getDanhGiaByKhoaHocWithFilter(maKhoaHoc, type, sao));
    }

    /**
     * Lấy tất cả đánh giá
     * GET /api/danhgia
     */
    @GetMapping
    public ResponseEntity<List<DanhGiaDTO>> getAllDanhGia() {
        return ResponseEntity.ok(danhGiaService.getAllDanhGia());
    }

    /**
     * Lấy đánh giá theo ID
     * GET /api/danhgia/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DanhGiaDTO> getDanhGiaById(@PathVariable Integer id) {
        return danhGiaService.getDanhGiaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa đánh giá
     * DELETE /api/danhgia/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhGia(@PathVariable Integer id) {
        danhGiaService.deleteDanhGia(id);
        return ResponseEntity.noContent().build();
    }
}
