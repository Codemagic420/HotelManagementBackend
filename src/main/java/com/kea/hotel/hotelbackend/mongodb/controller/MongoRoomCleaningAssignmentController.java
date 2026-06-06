package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningAssignment;
import com.kea.hotel.hotelbackend.mongodb.service.MongoRoomCleaningAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongo/room-cleaning-assignments")
@RequiredArgsConstructor
public class MongoRoomCleaningAssignmentController {
    private final MongoRoomCleaningAssignmentService service;

    @PostMapping
    public ResponseEntity<MongoRoomCleaningAssignment> create(@RequestBody MongoRoomCleaningAssignment assignment) {
        return ResponseEntity.ok(service.create(assignment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoRoomCleaningAssignment> findById(@PathVariable String id) {
        MongoRoomCleaningAssignment assignment = service.findById(id);
        return assignment != null ? ResponseEntity.ok(assignment) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<MongoRoomCleaningAssignment>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoRoomCleaningAssignment> update(@PathVariable String id, @RequestBody MongoRoomCleaningAssignment assignment) {
        MongoRoomCleaningAssignment updated = service.update(id, assignment);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
