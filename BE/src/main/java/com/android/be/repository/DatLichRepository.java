package com.android.be.repository;

import com.android.be.model.DatLich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatLichRepository extends JpaRepository<DatLich, Integer> {
}
