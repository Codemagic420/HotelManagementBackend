package com.kea.hotel.hotelbackend.mongodb.repository;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBillRepository extends MongoRepository<MongoBill, String> {
}
