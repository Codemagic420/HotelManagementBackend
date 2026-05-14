package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoom;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jRoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jRoomService {
    private final Neo4jRoomRepository repository;

    public Neo4jRoomService(Neo4jRoomRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jRoom> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jRoom> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jRoom save(Neo4jRoom room) {
        return repository.save(room);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
