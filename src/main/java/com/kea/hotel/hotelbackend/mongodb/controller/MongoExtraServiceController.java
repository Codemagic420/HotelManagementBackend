package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoExtraService;
import com.kea.hotel.hotelbackend.mongodb.service.MongoExtraServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/extra-services")
public class MongoExtraServiceController {
    private final MongoExtraServiceService service;

    public MongoExtraServiceController(MongoExtraServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoExtraService> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoExtraService> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoExtraService create(@RequestBody MongoExtraService service) {
        return this.service.save(service);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
