package com.android.be.repository;

import com.android.be.model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    
    // Tìm đánh giá theo mã đặt lịch
    Optional<DanhGia> findByMaDatLich(Integer maDatLich);
    
    // Kiểm tra đơn đặt lịch đã được đánh giá chưa
    boolean existsByMaDatLich(Integer maDatLich);
    
    // Lấy danh sách đánh giá theo khóa học
    List<DanhGia> findByMaKhoaHocOrderByNgayDanhGiaDesc(Integer maKhoaHoc);
    
    // Lấy danh sách đánh giá theo học viên
    List<DanhGia> findByMaHocVienOrderByNgayDanhGiaDesc(Integer maHocVien);
    
    // Đếm số đánh giá của khóa học
    long countByMaKhoaHoc(Integer maKhoaHoc);
    
    // Tính điểm trung bình của khóa học
    @Query("SELECT AVG(d.diemDanhGia) FROM DanhGia d WHERE d.maKhoaHoc = :maKhoaHoc")
    Double getAverageRatingByKhoaHoc(@Param("maKhoaHoc") Integer maKhoaHoc);
}
