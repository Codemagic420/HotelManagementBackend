package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningAssignment;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoRoomCleaningAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoRoomCleaningAssignmentService {
    private final MongoRoomCleaningAssignmentRepository repository;

    public MongoRoomCleaningAssignment create(MongoRoomCleaningAssignment assignment) {
        return repository.save(assignment);
    }

    public MongoRoomCleaningAssignment findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<MongoRoomCleaningAssignment> findAll() {
        return repository.findAll();
    }

    public MongoRoomCleaningAssignment update(String id, MongoRoomCleaningAssignment assignment) {
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

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
