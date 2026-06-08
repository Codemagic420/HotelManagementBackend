package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jReservationGuest;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jReservationGuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Neo4jReservationGuestService {
    private final Neo4jReservationGuestRepository repository;

    public Neo4jReservationGuest create(Neo4jReservationGuest reservationGuest) {
        return repository.save(reservationGuest);
    }

    public Neo4jReservationGuest findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Neo4jReservationGuest> findAll() {
        return repository.findAll();
    }

    public Neo4jReservationGuest update(Long id, Neo4jReservationGuest reservationGuest) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setReservationId(reservationGuest.getReservationId());
                    existing.setGuestId(reservationGuest.getGuestId());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
