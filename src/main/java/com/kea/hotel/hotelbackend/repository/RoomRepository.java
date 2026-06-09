package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByRoomStatus(String roomStatus, Pageable pageable);
    Optional<Room> findByRoomNumber(String roomNumber);
}
