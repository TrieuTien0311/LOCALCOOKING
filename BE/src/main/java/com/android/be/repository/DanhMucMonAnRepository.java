package com.android.be.repository;

import com.android.be.model.DanhMucMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhMucMonAnRepository extends JpaRepository<DanhMucMonAn, Integer> {
}
