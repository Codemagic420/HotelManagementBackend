package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomType;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoRoomTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoRoomTypeService {
    private final MongoRoomTypeRepository repository;

    public MongoRoomTypeService(MongoRoomTypeRepository repository) {
        this.repository = repository;
    }

    public List<MongoRoomType> findAll() {
        return repository.findAll();
    }

    public Optional<MongoRoomType> findById(String id) {
        return repository.findById(id);
    }

    public MongoRoomType save(MongoRoomType roomType) {
        return repository.save(roomType);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
