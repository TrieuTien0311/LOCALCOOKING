package com.android.be.repository;

import com.android.be.model.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Integer> {
    
    // Tìm khóa học theo tên
    List<KhoaHoc> findByTenKhoaHocContainingIgnoreCase(String tenKhoaHoc);
    
    // Tìm khóa học có ưu đãi
    List<KhoaHoc> findByCoUuDai(Boolean coUuDai);
    
    // Tìm khóa học theo giá tiền (từ - đến)
    List<KhoaHoc> findByGiaTienBetween(Double giaTienMin, Double giaTienMax);
    
    // Tìm khóa học theo đánh giá tối thiểu
    List<KhoaHoc> findBySaoTrungBinhGreaterThanEqual(Float saoTrungBinh);
}
