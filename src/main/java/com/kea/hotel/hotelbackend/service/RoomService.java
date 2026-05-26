package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repo;
    private final AiEnrichmentService aiEnrichmentService;

    public RoomService(RoomRepository repo, AiEnrichmentService aiEnrichmentService) {
        this.repo = repo;
        this.aiEnrichmentService = aiEnrichmentService;
    }

    public Page<Room> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<Room> findAllList() {
        return repo.findAll();
    }

    public Optional<Room> findById(Long id) {
        return repo.findById(id);
    }

    public Room save(Room room) {
        return repo.save(room);
    }

    public Optional<Room> update(Long id, Room updated) {
        return repo.findById(id).map(existing -> {
            existing.setRoomNumber(updated.getRoomNumber());
            existing.setType(updated.getType());
            existing.setOccupied(updated.getOccupied());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Optional<Room> enrichWithAI(Long id) {
        return repo.findById(id).map(room -> {
            String summary = aiEnrichmentService.generateRoomAssessmentSummary(room);
            room.setAiAssessmentSummary(summary);
            room.setAiFieldsUpdatedAt(LocalDateTime.now());
            return repo.save(room);
        });
    }

    public Optional<Room> updateAIAssessment(Long id, String assessment) {
        return repo.findById(id).map(room -> {
            room.setAiAssessmentSummary(assessment);
            room.setAiFieldsUpdatedAt(LocalDateTime.now());
            return repo.save(room);
        });
    }
}
