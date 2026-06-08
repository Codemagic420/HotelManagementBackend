package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jExtraService;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jExtraServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jExtraServiceService {
    private final Neo4jExtraServiceRepository repository;

    public Neo4jExtraServiceService(Neo4jExtraServiceRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jExtraService> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jExtraService> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jExtraService save(Neo4jExtraService service) {
        return repository.save(service);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
