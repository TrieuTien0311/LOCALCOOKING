package com.android.be.repository;

import com.android.be.model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    
    // Tính điểm đánh giá trung bình của lớp học
    @Query("SELECT AVG(d.diemDanhGia) FROM DanhGia d WHERE d.maLopHoc = ?1")
    Double getAverageRatingByLopHoc(Integer maLopHoc);
    
    // Đếm số lượt đánh giá của lớp học
    Integer countByMaLopHoc(Integer maLopHoc);
}
