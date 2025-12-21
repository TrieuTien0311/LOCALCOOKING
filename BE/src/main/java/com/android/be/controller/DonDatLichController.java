package com.android.be.controller;

import com.android.be.dto.DonDatLichDTO;
import com.android.be.service.DonDatLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/don-dat-lich")
public class DonDatLichController {

    @Autowired
    private DonDatLichService donDatLichService;

    /**
     * Lấy danh sách đơn đã hoàn thành của học viên
     * GET /api/don-dat-lich/hoan-thanh/{maHocVien}
     */
    @GetMapping("/hoan-thanh/{maHocVien}")
    public ResponseEntity<List<DonDatLichDTO>> getDonHoanThanh(@PathVariable Integer maHocVien) {
        List<DonDatLichDTO> result = donDatLichService.getDonHoanThanh(maHocVien);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách đơn đặt trước của học viên
     * GET /api/don-dat-lich/dat-truoc/{maHocVien}
     */
    @GetMapping("/dat-truoc/{maHocVien}")
    public ResponseEntity<List<DonDatLichDTO>> getDonDatTruoc(@PathVariable Integer maHocVien) {
        List<DonDatLichDTO> result = donDatLichService.getDonDatTruoc(maHocVien);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách đơn đã hủy của học viên
     * GET /api/don-dat-lich/da-huy/{maHocVien}
     */
    @GetMapping("/da-huy/{maHocVien}")
    public ResponseEntity<List<DonDatLichDTO>> getDonDaHuy(@PathVariable Integer maHocVien) {
        List<DonDatLichDTO> result = donDatLichService.getDonDaHuy(maHocVien);
        return ResponseEntity.ok(result);
    }
}
