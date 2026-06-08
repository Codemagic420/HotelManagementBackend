package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Page<Guest> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
