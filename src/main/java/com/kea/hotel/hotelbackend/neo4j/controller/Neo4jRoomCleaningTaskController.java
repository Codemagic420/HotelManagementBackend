package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomCleaningTask;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jRoomCleaningTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/room-cleaning-tasks")
public class Neo4jRoomCleaningTaskController {
    private final Neo4jRoomCleaningTaskService service;

    public Neo4jRoomCleaningTaskController(Neo4jRoomCleaningTaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jRoomCleaningTask> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jRoomCleaningTask> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jRoomCleaningTask create(@RequestBody Neo4jRoomCleaningTask task) {
        return service.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jRoomCleaningTask> update(@PathVariable Long id, @RequestBody Neo4jRoomCleaningTask task) {
        return service.findById(id).map(existing -> {
            task.setTaskId(id);
            return ResponseEntity.ok(service.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
