package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservation;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoReservationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoReservationService {
    private final MongoReservationRepository repository;

    public MongoReservationService(MongoReservationRepository repository) {
        this.repository = repository;
    }

    public List<MongoReservation> findAll() {
        return repository.findAll();
    }

    public Optional<MongoReservation> findById(String id) {
        return repository.findById(id);
    }

    public MongoReservation save(MongoReservation reservation) {
        return repository.save(reservation);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
