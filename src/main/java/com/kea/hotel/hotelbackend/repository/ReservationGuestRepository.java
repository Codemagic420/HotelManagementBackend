package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.ReservationGuest;
import com.kea.hotel.hotelbackend.model.ReservationGuestKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, ReservationGuestKey> {
	void deleteByGuest_GuestId(Long guestId);
}
