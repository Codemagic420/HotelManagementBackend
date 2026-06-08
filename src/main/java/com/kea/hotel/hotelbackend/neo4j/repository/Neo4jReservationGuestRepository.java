package com.kea.hotel.hotelbackend.neo4j.repository;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jReservationGuest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4jReservationGuestRepository extends Neo4jRepository<Neo4jReservationGuest, Long> {
}
