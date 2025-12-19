package com.android.be.repository;

import com.android.be.model.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {
    
    // Lấy món ăn theo khóa học
    List<MonAn> findByMaKhoaHoc(Integer maKhoaHoc);
    
    // Lấy món ăn theo danh mục
    List<MonAn> findByMaDanhMuc(Integer maDanhMuc);
    
    // Lấy món ăn theo khóa học và danh mục
    List<MonAn> findByMaKhoaHocAndMaDanhMuc(Integer maKhoaHoc, Integer maDanhMuc);
}
