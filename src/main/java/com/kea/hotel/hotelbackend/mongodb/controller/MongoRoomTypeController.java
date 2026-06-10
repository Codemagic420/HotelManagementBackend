package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomType;
import com.kea.hotel.hotelbackend.mongodb.service.MongoRoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/room-types")
public class MongoRoomTypeController {
    private final MongoRoomTypeService service;

    public MongoRoomTypeController(MongoRoomTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoRoomType> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoRoomType> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoRoomType create(@RequestBody MongoRoomType roomType) {
        return service.save(roomType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
