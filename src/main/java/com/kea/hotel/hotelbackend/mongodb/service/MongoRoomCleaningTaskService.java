package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningTask;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoRoomCleaningTaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoRoomCleaningTaskService {
    private final MongoRoomCleaningTaskRepository repository;

    public MongoRoomCleaningTaskService(MongoRoomCleaningTaskRepository repository) {
        this.repository = repository;
    }

    public List<MongoRoomCleaningTask> findAll() {
        return repository.findAll();
    }

    public Optional<MongoRoomCleaningTask> findById(String id) {
        return repository.findById(id);
    }

    public MongoRoomCleaningTask save(MongoRoomCleaningTask task) {
        return repository.save(task);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
