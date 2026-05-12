package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoGuest;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoGuestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoGuestService {
    private final MongoGuestRepository repository;

    public MongoGuestService(MongoGuestRepository repository) {
        this.repository = repository;
    }

    public List<MongoGuest> findAll() {
        return repository.findAll();
    }

    public Optional<MongoGuest> findById(String id) {
        return repository.findById(id);
    }

    public MongoGuest save(MongoGuest guest) {
        return repository.save(guest);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
