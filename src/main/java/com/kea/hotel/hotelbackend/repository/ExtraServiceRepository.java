package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.ExtraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraServiceRepository extends JpaRepository<ExtraService, Long> {
    Page<ExtraService> findByActive(Boolean active, Pageable pageable);
}
