package com.android.be.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckSeatsResponse {
    private boolean success;
    private String message;
    
    // Dữ liệu từ stored procedure sp_KiemTraChoTrong
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private Integer tongCho;
    private Integer daDat;
    private Integer conTrong;
    private String trangThai; // "Hết Chỗ", "Sắp Hết", "Còn Nhiều"
    
    // Constructor cho success response
    public CheckSeatsResponse(boolean success, String message, Integer maLichTrinh, 
                             Integer maKhoaHoc, String tenKhoaHoc, Integer tongCho, 
                             Integer daDat, Integer conTrong, String trangThai) {
        this.success = success;
        this.message = message;
        this.maLichTrinh = maLichTrinh;
        this.maKhoaHoc = maKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
        this.tongCho = tongCho;
        this.daDat = daDat;
        this.conTrong = conTrong;
        this.trangThai = trangThai;
    }
    
    // Constructor cho error response
    public CheckSeatsResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
