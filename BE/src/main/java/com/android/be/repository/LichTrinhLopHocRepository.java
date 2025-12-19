package com.android.be.repository;

import com.android.be.model.LichTrinhLopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LichTrinhLopHocRepository extends JpaRepository<LichTrinhLopHoc, Integer> {
    
    // Tìm lịch trình theo khóa học
    List<LichTrinhLopHoc> findByMaKhoaHoc(Integer maKhoaHoc);
    
    // Tìm lịch trình theo giáo viên
    List<LichTrinhLopHoc> findByMaGiaoVien(Integer maGiaoVien);
    
    // Tìm lịch trình theo địa điểm
    List<LichTrinhLopHoc> findByDiaDiemContainingIgnoreCase(String diaDiem);
    
    // Tìm lịch trình đang hoạt động theo địa điểm
    @Query("SELECT lt FROM LichTrinhLopHoc lt WHERE lt.diaDiem LIKE %:diaDiem% AND lt.trangThai = true")
    List<LichTrinhLopHoc> findActiveByDiaDiem(@Param("diaDiem") String diaDiem);
    
    // Tìm lịch trình đang hoạt động
    List<LichTrinhLopHoc> findByTrangThai(Boolean trangThai);
    
    // Tìm lịch trình theo khóa học và trạng thái
    List<LichTrinhLopHoc> findByMaKhoaHocAndTrangThai(Integer maKhoaHoc, Boolean trangThai);
    
    // Gọi stored procedure để lấy danh sách lớp theo ngày
    @Query(value = "EXEC sp_LayDanhSachLopTheoNgay :ngayCanXem", nativeQuery = true)
    List<Object[]> findClassesByDate(@Param("ngayCanXem") String ngayCanXem);
    
    // Gọi stored procedure để kiểm tra chỗ trống
    @Query(value = "EXEC sp_KiemTraChoTrong :maLichTrinh, :ngayThamGia", nativeQuery = true)
    Object[] checkAvailableSeats(@Param("maLichTrinh") Integer maLichTrinh, 
                                  @Param("ngayThamGia") String ngayThamGia);
}
