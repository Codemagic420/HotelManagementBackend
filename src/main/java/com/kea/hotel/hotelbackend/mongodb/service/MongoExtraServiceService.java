package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoExtraService;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoExtraServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoExtraServiceService {
    private final MongoExtraServiceRepository repository;

    public MongoExtraServiceService(MongoExtraServiceRepository repository) {
        this.repository = repository;
    }

    public List<MongoExtraService> findAll() {
        return repository.findAll();
    }

    public Optional<MongoExtraService> findById(String id) {
        return repository.findById(id);
    }

    public MongoExtraService save(MongoExtraService service) {
        return repository.save(service);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
