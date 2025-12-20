package com.android.be.repository;

import com.android.be.model.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    
    // Lấy thông báo theo người nhận
    List<ThongBao> findByMaNguoiNhanOrderByNgayTaoDesc(Integer maNguoiNhan);
    
    // Lấy thông báo chưa đọc của người dùng
    List<ThongBao> findByMaNguoiNhanAndDaDocOrderByNgayTaoDesc(Integer maNguoiNhan, Boolean daDoc);
    
    // Đếm số thông báo chưa đọc
    @Query("SELECT COUNT(t) FROM ThongBao t WHERE t.maNguoiNhan = :maNguoiNhan AND t.daDoc = false")
    Long countUnreadByUser(@Param("maNguoiNhan") Integer maNguoiNhan);
    
    // Lấy thông báo theo loại
    List<ThongBao> findByMaNguoiNhanAndLoaiThongBaoOrderByNgayTaoDesc(Integer maNguoiNhan, String loaiThongBao);
}
