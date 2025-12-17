package com.android.be.repository;

import com.android.be.model.HinhAnhLopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhAnhLopHocRepository extends JpaRepository<HinhAnhLopHoc, Integer> {
}
