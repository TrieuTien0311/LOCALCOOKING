package com.android.be.repository;

import com.android.be.model.GiaoVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaoVienRepository extends JpaRepository<GiaoVien, Integer> {
}
