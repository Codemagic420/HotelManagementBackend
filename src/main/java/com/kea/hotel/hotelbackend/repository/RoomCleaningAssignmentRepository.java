package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.RoomCleaningAssignment;
import com.kea.hotel.hotelbackend.model.RoomCleaningAssignmentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCleaningAssignmentRepository extends JpaRepository<RoomCleaningAssignment, RoomCleaningAssignmentKey> {
}
