package com.android.be.repository;

import com.android.be.model.YeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, Integer> {
    
    // Lấy tất cả khóa học yêu thích của một học viên
    List<YeuThich> findByMaHocVien(Integer maHocVien);
    
    // Kiểm tra xem học viên đã yêu thích khóa học chưa
    Optional<YeuThich> findByMaHocVienAndMaKhoaHoc(Integer maHocVien, Integer maKhoaHoc);
    
    // Kiểm tra tồn tại
    boolean existsByMaHocVienAndMaKhoaHoc(Integer maHocVien, Integer maKhoaHoc);
    
    // Xóa yêu thích
    void deleteByMaHocVienAndMaKhoaHoc(Integer maHocVien, Integer maKhoaHoc);
    
    // Đếm số lượng yêu thích của một khóa học
    @Query("SELECT COUNT(y) FROM YeuThich y WHERE y.maKhoaHoc = :maKhoaHoc")
    Long countByMaKhoaHoc(@Param("maKhoaHoc") Integer maKhoaHoc);
}
