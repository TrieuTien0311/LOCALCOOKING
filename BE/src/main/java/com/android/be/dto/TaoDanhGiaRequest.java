package com.android.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaoDanhGiaRequest {
    private Integer maDatLich;
    private Integer diemDanhGia; // 1-5
    private String binhLuan;
    private List<String> hinhAnhUrls; // Danh sách URL hình ảnh/video
}
