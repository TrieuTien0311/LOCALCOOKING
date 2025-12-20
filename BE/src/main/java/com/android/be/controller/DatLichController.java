package com.android.be.controller;

import com.android.be.model.DatLich;
import com.android.be.service.DatLichService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/datlich")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DatLichController {
    
    private final DatLichService datLichService;
    
    // GET - Lấy tất cả đặt lịch
    @GetMapping
    public ResponseEntity<List<DatLich>> getAllDatLich() {
        return ResponseEntity.ok(datLichService.getAllDatLich());
    }
    
    // GET - Lấy đặt lịch theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DatLich> getDatLichById(@PathVariable Integer id) {
        return datLichService.getDatLichById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET - Lấy đặt lịch theo học viên
    @GetMapping("/hocvien/{maHocVien}")
    public ResponseEntity<List<DatLich>> getDatLichByHocVien(@PathVariable Integer maHocVien) {
        return ResponseEntity.ok(datLichService.getDatLichByHocVien(maHocVien));
    }
    
    // GET - Lấy đặt lịch theo lịch trình
    @GetMapping("/lichtrinh/{maLichTrinh}")
    public ResponseEntity<List<DatLich>> getDatLichByLichTrinh(@PathVariable Integer maLichTrinh) {
        return ResponseEntity.ok(datLichService.getDatLichByLichTrinh(maLichTrinh));
    }
    
    // GET - Lấy đặt lịch theo trạng thái
    @GetMapping("/trangthai/{trangThai}")
    public ResponseEntity<List<DatLich>> getDatLichByTrangThai(@PathVariable String trangThai) {
        return ResponseEntity.ok(datLichService.getDatLichByTrangThai(trangThai));
    }
    
    // GET - Lấy đặt lịch theo học viên và trạng thái
    @GetMapping("/hocvien/{maHocVien}/trangthai/{trangThai}")
    public ResponseEntity<List<DatLich>> getDatLichByHocVienAndTrangThai(
            @PathVariable Integer maHocVien,
            @PathVariable String trangThai) {
        return ResponseEntity.ok(datLichService.getDatLichByHocVienAndTrangThai(maHocVien, trangThai));
    }
    
    // GET - Kiểm tra số chỗ trống
    @GetMapping("/check-seats")
    public ResponseEntity<?> checkAvailableSeats(
            @RequestParam Integer maLichTrinh,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayThamGia) {
        try {
            Integer availableSeats = datLichService.getAvailableSeats(maLichTrinh, ngayThamGia);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "soChoConLai", availableSeats,
                "message", availableSeats > 0 ? "Còn chỗ trống" : "Đã hết chỗ"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // POST - Tạo đặt lịch mới
    @PostMapping
    public ResponseEntity<?> createDatLich(@RequestBody DatLich datLich) {
        try {
            DatLich created = datLichService.createDatLich(datLich);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Đặt lịch thành công",
                "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // PUT - Cập nhật đặt lịch
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDatLich(@PathVariable Integer id, @RequestBody DatLich datLich) {
        try {
            DatLich updated = datLichService.updateDatLich(id, datLich);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cập nhật đặt lịch thành công",
                "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // PUT - Hủy đặt lịch
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelDatLich(@PathVariable Integer id) {
        try {
            DatLich cancelled = datLichService.cancelDatLich(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Hủy đặt lịch thành công",
                "data", cancelled
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    // DELETE - Xóa đặt lịch
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatLich(@PathVariable Integer id) {
        datLichService.deleteDatLich(id);
        return ResponseEntity.noContent().build();
    }
}
