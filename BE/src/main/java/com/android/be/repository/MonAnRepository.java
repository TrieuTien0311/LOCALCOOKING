package com.android.be.repository;

import com.android.be.model.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {
    List<MonAn> findByMaLopHoc(Integer maLopHoc);
    List<MonAn> findByMaDanhMuc(Integer maDanhMuc);
    List<MonAn> findByMaLopHocAndMaDanhMuc(Integer maLopHoc, Integer maDanhMuc);
}
