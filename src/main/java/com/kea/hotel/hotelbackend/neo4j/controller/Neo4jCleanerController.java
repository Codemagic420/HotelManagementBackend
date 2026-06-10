package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jCleaner;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jCleanerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/cleaners")
@SecurityRequirement(name = "bearerAuth")
public class Neo4jCleanerController {
    private final Neo4jCleanerService service;

    public Neo4jCleanerController(Neo4jCleanerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jCleaner> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jCleaner> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jCleaner create(@RequestBody Neo4jCleaner cleaner) {
        return service.save(cleaner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jCleaner> update(@PathVariable String id, @RequestBody Neo4jCleaner cleaner) {
        return service.findById(id).map(existing -> {
            cleaner.setElementId(existing.getElementId());
            return ResponseEntity.ok(service.save(cleaner));
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
