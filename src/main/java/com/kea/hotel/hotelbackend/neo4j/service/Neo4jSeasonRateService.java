package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jSeasonRate;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jSeasonRateRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jSeasonRateService {
    private final Neo4jSeasonRateRepository repository;

    public Neo4jSeasonRateService(Neo4jSeasonRateRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jSeasonRate> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jSeasonRate> findById(String id) {
        return repository.findById(id);
    }

    public Neo4jSeasonRate save(Neo4jSeasonRate seasonRate) {
        return repository.save(seasonRate);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
