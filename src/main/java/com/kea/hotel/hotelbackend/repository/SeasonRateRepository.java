package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.SeasonRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRateRepository extends JpaRepository<SeasonRate, Long> {
    Page<SeasonRate> findBySeason(String season, Pageable pageable);
}
