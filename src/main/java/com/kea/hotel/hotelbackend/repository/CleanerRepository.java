package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Cleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner, Long> {
    Page<Cleaner> findByActive(Boolean active, Pageable pageable);
}
