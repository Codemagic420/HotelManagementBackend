package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoCleaner;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoCleanerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoCleanerService {
    private final MongoCleanerRepository repository;

    public MongoCleanerService(MongoCleanerRepository repository) {
        this.repository = repository;
    }

    public List<MongoCleaner> findAll() {
        return repository.findAll();
    }

    public Optional<MongoCleaner> findById(String id) {
        return repository.findById(id);
    }

    public MongoCleaner save(MongoCleaner cleaner) {
        return repository.save(cleaner);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
