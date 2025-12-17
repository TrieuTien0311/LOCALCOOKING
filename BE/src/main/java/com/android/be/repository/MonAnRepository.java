package com.android.be.repository;

import com.android.be.model.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {
}
