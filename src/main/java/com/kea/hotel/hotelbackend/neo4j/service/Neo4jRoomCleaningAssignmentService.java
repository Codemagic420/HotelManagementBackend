package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomCleaningAssignment;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jRoomCleaningAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Neo4jRoomCleaningAssignmentService {
    private final Neo4jRoomCleaningAssignmentRepository repository;

    public Neo4jRoomCleaningAssignment create(Neo4jRoomCleaningAssignment assignment) {
        return repository.save(assignment);
    }

    public Neo4jRoomCleaningAssignment findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Neo4jRoomCleaningAssignment> findAll() {
        return repository.findAll();
    }

    public Neo4jRoomCleaningAssignment update(Long id, Neo4jRoomCleaningAssignment assignment) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCleanerId(assignment.getCleanerId());
                    existing.setTaskId(assignment.getTaskId());
                    existing.setAssignedAt(assignment.getAssignedAt());
                    existing.setStatus(assignment.getStatus());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
