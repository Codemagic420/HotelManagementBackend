package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoom;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoRoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoRoomService {
    private final MongoRoomRepository repository;

    public MongoRoomService(MongoRoomRepository repository) {
        this.repository = repository;
    }

    public List<MongoRoom> findAll() {
        return repository.findAll();
    }

    public Optional<MongoRoom> findById(String id) {
        return repository.findById(id);
    }

    public MongoRoom save(MongoRoom room) {
        return repository.save(room);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
