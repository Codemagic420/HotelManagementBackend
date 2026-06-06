package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservationGuest;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoReservationGuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoReservationGuestService {
    private final MongoReservationGuestRepository repository;

    public MongoReservationGuest create(MongoReservationGuest reservationGuest) {
        return repository.save(reservationGuest);
    }

    public MongoReservationGuest findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<MongoReservationGuest> findAll() {
        return repository.findAll();
    }

    public MongoReservationGuest update(String id, MongoReservationGuest reservationGuest) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setReservationId(reservationGuest.getReservationId());
                    existing.setGuestId(reservationGuest.getGuestId());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
