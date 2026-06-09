package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomType;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jRoomTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jRoomTypeService {
    private final Neo4jRoomTypeRepository repository;

    public Neo4jRoomTypeService(Neo4jRoomTypeRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jRoomType> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jRoomType> findById(String id) {
        return repository.findById(id);
    }

    public Neo4jRoomType save(Neo4jRoomType roomType) {
        return repository.save(roomType);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
