package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoCleaner;
import com.kea.hotel.hotelbackend.mongodb.service.MongoCleanerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/cleaners")
public class MongoCleanerController {
    private final MongoCleanerService service;

    public MongoCleanerController(MongoCleanerService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoCleaner> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoCleaner> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoCleaner create(@RequestBody MongoCleaner cleaner) {
        return service.save(cleaner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
