package com.android.be.repository;

import com.android.be.model.UuDai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UuDaiRepository extends JpaRepository<UuDai, Integer> {
    
    Optional<UuDai> findByMaCode(String maCode);
    
    @Query("SELECT u FROM UuDai u WHERE u.ngayBatDau <= :today AND u.ngayKetThuc >= :today")
    List<UuDai> findActiveUuDai(@Param("today") LocalDate today);
    
    @Query("SELECT u FROM UuDai u WHERE u.loaiUuDai = :loaiUuDai " +
           "AND u.ngayBatDau <= :today AND u.ngayKetThuc >= :today")
    List<UuDai> findActiveByLoaiUuDai(@Param("loaiUuDai") String loaiUuDai, @Param("today") LocalDate today);
}
