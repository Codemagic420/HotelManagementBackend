package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByGuest_GuestId(Long guestId);
}
