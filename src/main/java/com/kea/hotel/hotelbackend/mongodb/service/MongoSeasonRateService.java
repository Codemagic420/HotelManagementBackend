package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoSeasonRate;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoSeasonRateRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoSeasonRateService {
    private final MongoSeasonRateRepository repository;

    public MongoSeasonRateService(MongoSeasonRateRepository repository) {
        this.repository = repository;
    }

    public List<MongoSeasonRate> findAll() {
        return repository.findAll();
    }

    public Optional<MongoSeasonRate> findById(String id) {
        return repository.findById(id);
    }

    public MongoSeasonRate save(MongoSeasonRate rate) {
        return repository.save(rate);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
