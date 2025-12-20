package com.android.be.repository;

import com.android.be.model.DatLich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DatLichRepository extends JpaRepository<DatLich, Integer> {
    
    // Đếm số đơn đặt lịch của user (để check đơn đầu tiên)
    @Query("SELECT COUNT(d) FROM DatLich d WHERE d.maHocVien = :maHocVien")
    long countByMaHocVien(@Param("maHocVien") Integer maHocVien);
    
    // Check user đã có đơn nào chưa (để xác định tài khoản mới)
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DatLich d WHERE d.maHocVien = :maHocVien")
    boolean existsByMaHocVien(@Param("maHocVien") Integer maHocVien);
}
