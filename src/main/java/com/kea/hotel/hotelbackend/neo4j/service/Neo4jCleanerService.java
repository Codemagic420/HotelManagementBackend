package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jCleaner;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jCleanerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jCleanerService {
    private final Neo4jCleanerRepository repository;

    public Neo4jCleanerService(Neo4jCleanerRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jCleaner> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jCleaner> findById(String id) {
        return repository.findById(id);
    }

    public Neo4jCleaner save(Neo4jCleaner cleaner) {
        return repository.save(cleaner);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
