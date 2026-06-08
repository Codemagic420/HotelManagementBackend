package com.kea.hotel.hotelbackend.mongodb.repository;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRoomCleaningAssignmentRepository extends MongoRepository<MongoRoomCleaningAssignment, String> {
}
