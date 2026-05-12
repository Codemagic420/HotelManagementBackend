package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoGuest;
import com.kea.hotel.hotelbackend.mongodb.service.MongoGuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/guests")
public class MongoGuestController {
    private final MongoGuestService service;

    public MongoGuestController(MongoGuestService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoGuest> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoGuest> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoGuest create(@RequestBody MongoGuest guest) {
        return service.save(guest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
