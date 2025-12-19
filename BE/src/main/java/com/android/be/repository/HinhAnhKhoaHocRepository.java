package com.android.be.repository;

import com.android.be.model.HinhAnhKhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HinhAnhKhoaHocRepository extends JpaRepository<HinhAnhKhoaHoc, Integer> {
    List<HinhAnhKhoaHoc> findByMaKhoaHocOrderByThuTuAsc(Integer maKhoaHoc);
}
