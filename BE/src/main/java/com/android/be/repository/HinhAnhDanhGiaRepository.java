package com.android.be.repository;

import com.android.be.model.HinhAnhDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HinhAnhDanhGiaRepository extends JpaRepository<HinhAnhDanhGia, Integer> {
    
    // Lấy danh sách hình ảnh theo mã đánh giá
    List<HinhAnhDanhGia> findByMaDanhGiaOrderByThuTuAsc(Integer maDanhGia);
    
    // Xóa tất cả hình ảnh của một đánh giá
    void deleteByMaDanhGia(Integer maDanhGia);
}
