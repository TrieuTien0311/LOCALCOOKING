package com.android.be.repository;

import com.android.be.model.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
    
    // Tìm lớp học theo trạng thái
    List<LopHoc> findByTrangThai(String trangThai);
    
    // Tìm lớp học theo giáo viên
    List<LopHoc> findByMaGiaoVien(Integer maGiaoVien);
    
    // Tìm lớp học theo tên (like)
    @Query("SELECT l FROM LopHoc l WHERE l.tenLopHoc LIKE %?1%")
    List<LopHoc> searchByTenLopHoc(String keyword);
}
