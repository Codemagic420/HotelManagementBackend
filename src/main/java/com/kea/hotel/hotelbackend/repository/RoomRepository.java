package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
