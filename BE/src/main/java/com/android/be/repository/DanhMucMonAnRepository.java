package com.android.be.repository;

import com.android.be.model.DanhMucMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucMonAnRepository extends JpaRepository<DanhMucMonAn, Integer> {
    
    // Lấy danh mục theo thứ tự
    List<DanhMucMonAn> findAllByOrderByThuTuAsc();
}
