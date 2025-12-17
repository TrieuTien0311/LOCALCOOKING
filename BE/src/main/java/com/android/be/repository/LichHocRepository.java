package com.android.be.repository;

import com.android.be.model.LichHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichHocRepository extends JpaRepository<LichHoc, Integer> {
}
