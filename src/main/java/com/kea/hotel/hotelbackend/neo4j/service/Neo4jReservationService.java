package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jReservation;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jReservationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jReservationService {
    private final Neo4jReservationRepository repository;

    public Neo4jReservationService(Neo4jReservationRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jReservation> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jReservation> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jReservation save(Neo4jReservation reservation) {
        return repository.save(reservation);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
