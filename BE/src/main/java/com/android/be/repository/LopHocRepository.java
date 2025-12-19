package com.android.be.repository;

import com.android.be.model.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {
    List<LopHoc> findByDiaDiemContainingIgnoreCase(String diaDiem);
    List<LopHoc> findByDiaDiemContainingIgnoreCaseAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
        String diaDiem, java.time.LocalDate ngayBatDau, java.time.LocalDate ngayKetThuc);
    List<LopHoc> findByDiaDiemContainingIgnoreCaseAndNgayBatDauLessThanEqual(
        String diaDiem, java.time.LocalDate ngayHienTai);
}
