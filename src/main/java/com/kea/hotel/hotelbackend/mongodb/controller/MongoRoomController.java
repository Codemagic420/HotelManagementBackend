package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoom;
import com.kea.hotel.hotelbackend.mongodb.service.MongoRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/rooms")
public class MongoRoomController {
    private final MongoRoomService service;

    public MongoRoomController(MongoRoomService service) {
        this.service = service;
    }

    @GetMapping
    public Page<MongoRoom> getAll(
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
    public ResponseEntity<MongoRoom> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoRoom create(@RequestBody MongoRoom room) {
        return service.save(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
