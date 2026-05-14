package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.RoomCleaningTask;
import com.kea.hotel.hotelbackend.repository.RoomCleaningTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomCleaningTaskService {

    private final RoomCleaningTaskRepository repo;

    public RoomCleaningTaskService(RoomCleaningTaskRepository repo) {
        this.repo = repo;
    }

    public List<RoomCleaningTask> findAll() {
        return repo.findAll();
    }

    public Optional<RoomCleaningTask> findById(Long id) {
        return repo.findById(id);
    }

    public RoomCleaningTask save(RoomCleaningTask task) {
        return repo.save(task);
    }

    public Optional<RoomCleaningTask> update(Long id, RoomCleaningTask updated) {
        return repo.findById(id).map(existing -> {
            existing.setRoom(updated.getRoom());
            existing.setTaskStatus(updated.getTaskStatus());
            existing.setNote(updated.getNote());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
