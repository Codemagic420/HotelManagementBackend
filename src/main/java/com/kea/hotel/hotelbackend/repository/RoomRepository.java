package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findByRoomNumber(String roomNumber);
}
