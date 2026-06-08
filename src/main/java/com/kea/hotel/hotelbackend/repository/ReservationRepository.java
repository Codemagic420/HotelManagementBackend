package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByStatus(String status, Pageable pageable);
}
