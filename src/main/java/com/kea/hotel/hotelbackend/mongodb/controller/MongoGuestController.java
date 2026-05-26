package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoGuest;
import com.kea.hotel.hotelbackend.mongodb.service.MongoGuestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<MongoGuest> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(",");
            String field = parts[0];
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return service.findAll(pageable);
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
