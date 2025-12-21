package com.android.be.repository;

import com.android.be.model.DatLich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DatLichRepository extends JpaRepository<DatLich, Integer> {
    
    // Tìm đặt lịch theo học viên
    List<DatLich> findByMaHocVien(Integer maHocVien);
    
    // Tìm đặt lịch theo lịch trình
    List<DatLich> findByMaLichTrinh(Integer maLichTrinh);
    
    // Tìm đặt lịch theo lịch trình và ngày tham gia
    List<DatLich> findByMaLichTrinhAndNgayThamGia(Integer maLichTrinh, LocalDate ngayThamGia);
    
    // Tìm đặt lịch theo trạng thái
    List<DatLich> findByTrangThai(String trangThai);
    
    // Tìm đặt lịch theo học viên và trạng thái
    List<DatLich> findByMaHocVienAndTrangThai(Integer maHocVien, String trangThai);
    
    // Đếm số người đã đặt cho lịch trình trong ngày cụ thể (không tính đơn đã hủy)
    @Query("SELECT COALESCE(SUM(d.soLuongNguoi), 0) FROM DatLich d " +
           "WHERE d.maLichTrinh = :maLichTrinh " +
           "AND d.ngayThamGia = :ngayThamGia " +
           "AND d.trangThai <> 'Đã Hủy'")
    Integer countBookedSeats(@Param("maLichTrinh") Integer maLichTrinh, 
                             @Param("ngayThamGia") LocalDate ngayThamGia);
    // Đếm số đơn đặt lịch của user (để check đơn đầu tiên)
    @Query("SELECT COUNT(d) FROM DatLich d WHERE d.maHocVien = :maHocVien")
    long countByMaHocVien(@Param("maHocVien") Integer maHocVien);
    
    // Check user đã có đơn nào chưa (để xác định tài khoản mới)
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DatLich d WHERE d.maHocVien = :maHocVien")
    boolean existsByMaHocVien(@Param("maHocVien") Integer maHocVien);
}
