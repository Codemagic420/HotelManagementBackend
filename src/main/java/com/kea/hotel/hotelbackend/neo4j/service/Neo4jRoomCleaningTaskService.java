package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomCleaningTask;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jRoomCleaningTaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jRoomCleaningTaskService {
    private final Neo4jRoomCleaningTaskRepository repository;

    public Neo4jRoomCleaningTaskService(Neo4jRoomCleaningTaskRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jRoomCleaningTask> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jRoomCleaningTask> findById(String id) {
        return repository.findById(id);
    }

    public Neo4jRoomCleaningTask save(Neo4jRoomCleaningTask task) {
        return repository.save(task);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
