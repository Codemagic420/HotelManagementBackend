package com.kea.hotel.hotelbackend.mongodb.repository;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservationGuest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoReservationGuestRepository extends MongoRepository<MongoReservationGuest, String> {
}
