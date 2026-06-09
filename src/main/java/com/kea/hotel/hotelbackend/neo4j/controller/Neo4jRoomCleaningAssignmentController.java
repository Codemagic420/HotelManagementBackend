package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomCleaningAssignment;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jRoomCleaningAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/room-cleaning-assignments")
@RequiredArgsConstructor
public class Neo4jRoomCleaningAssignmentController {
    private final Neo4jRoomCleaningAssignmentService service;

    @PostMapping
    public ResponseEntity<Neo4jRoomCleaningAssignment> create(@RequestBody Neo4jRoomCleaningAssignment assignment) {
        return ResponseEntity.ok(service.create(assignment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jRoomCleaningAssignment> findById(@PathVariable Long id) {
        Neo4jRoomCleaningAssignment assignment = service.findById(id);
        return assignment != null ? ResponseEntity.ok(assignment) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Neo4jRoomCleaningAssignment>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jRoomCleaningAssignment> update(@PathVariable Long id, @RequestBody Neo4jRoomCleaningAssignment assignment) {
        Neo4jRoomCleaningAssignment updated = service.update(id, assignment);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
