package com.android.be.repository;

import com.android.be.model.HinhAnhMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HinhAnhMonAnRepository extends JpaRepository<HinhAnhMonAn, Integer> {
    List<HinhAnhMonAn> findByMaMonAnOrderByThuTuAsc(Integer maMonAn);
}
