package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByRoomStatus(String roomStatus, Pageable pageable);
}
