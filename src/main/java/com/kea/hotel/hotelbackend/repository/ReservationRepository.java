package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
