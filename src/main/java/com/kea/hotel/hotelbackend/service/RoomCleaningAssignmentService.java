package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.RoomCleaningAssignment;
import com.kea.hotel.hotelbackend.model.RoomCleaningAssignmentKey;
import com.kea.hotel.hotelbackend.repository.RoomCleaningAssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomCleaningAssignmentService {

    private final RoomCleaningAssignmentRepository repo;

    public RoomCleaningAssignmentService(RoomCleaningAssignmentRepository repo) {
        this.repo = repo;
    }

    public List<RoomCleaningAssignment> findAll() {
        return repo.findAll();
    }

    public Page<RoomCleaningAssignment> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<RoomCleaningAssignment> findById(RoomCleaningAssignmentKey id) {
        return repo.findById(id);
    }

    public RoomCleaningAssignment save(RoomCleaningAssignment assignment) {
        return repo.save(assignment);
    }

    public Optional<RoomCleaningAssignment> update(RoomCleaningAssignmentKey id, RoomCleaningAssignment updated) {
        return repo.findById(id).map(existing -> {
            existing.setAssignedAt(updated.getAssignedAt());
            return repo.save(existing);
        });
    }

    public void delete(RoomCleaningAssignmentKey id) {
        repo.deleteById(id);
    }
}
