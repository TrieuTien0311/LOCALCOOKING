package com.android.be.repository;

import com.android.be.model.GiaoVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiaoVienRepository extends JpaRepository<GiaoVien, Integer> {
    
    // Tìm giáo viên theo maNguoiDung
    Optional<GiaoVien> findByMaNguoiDung(Integer maNguoiDung);
    
    // Lấy tất cả giáo viên kèm thông tin người dùng
    @Query(value = "SELECT g.maGiaoVien, g.maNguoiDung, g.chuyenMon, g.kinhNghiem, " +
                   "g.lichSuKinhNghiem, g.moTa, g.hinhAnh, " +
                   "n.hoTen, n.email, n.soDienThoai " +
                   "FROM GiaoVien g " +
                   "JOIN NguoiDung n ON g.maNguoiDung = n.maNguoiDung", nativeQuery = true)
    List<Object[]> findAllGiaoVienWithNguoiDung();
    
    // Lấy thông tin giáo viên kèm thông tin người dùng
    @Query(value = "SELECT g.maGiaoVien, g.maNguoiDung, g.chuyenMon, g.kinhNghiem, " +
                   "g.lichSuKinhNghiem, g.moTa, g.hinhAnh, " +
                   "n.hoTen, n.email, n.soDienThoai " +
                   "FROM GiaoVien g " +
                   "JOIN NguoiDung n ON g.maNguoiDung = n.maNguoiDung " +
                   "WHERE g.maGiaoVien = :maGiaoVien", nativeQuery = true)
    List<Object[]> findGiaoVienWithNguoiDung(@Param("maGiaoVien") Integer maGiaoVien);
}
