package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningTask;
import com.kea.hotel.hotelbackend.mongodb.service.MongoRoomCleaningTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/room-cleaning-tasks")
public class MongoRoomCleaningTaskController {
    private final MongoRoomCleaningTaskService service;

    public MongoRoomCleaningTaskController(MongoRoomCleaningTaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoRoomCleaningTask> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoRoomCleaningTask> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoRoomCleaningTask create(@RequestBody MongoRoomCleaningTask task) {
        return service.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoRoomCleaningTask> update(@PathVariable String id, @RequestBody MongoRoomCleaningTask task) {
        return service.findById(id).map(existing -> {
            task.setTaskId(existing.getTaskId());
            return ResponseEntity.ok(service.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
