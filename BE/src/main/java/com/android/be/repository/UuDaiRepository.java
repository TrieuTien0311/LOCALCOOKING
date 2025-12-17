package com.android.be.repository;

import com.android.be.model.UuDai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UuDaiRepository extends JpaRepository<UuDai, Integer> {
}
