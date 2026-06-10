package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoomType;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jRoomTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/room-types")
@SecurityRequirement(name = "bearerAuth")
public class Neo4jRoomTypeController {
    private final Neo4jRoomTypeService service;

    public Neo4jRoomTypeController(Neo4jRoomTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jRoomType> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jRoomType> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jRoomType create(@RequestBody Neo4jRoomType roomType) {
        return service.save(roomType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jRoomType> update(@PathVariable String id, @RequestBody Neo4jRoomType roomType) {
        return service.findById(id).map(existing -> {
            roomType.setElementId(existing.getElementId());
            return ResponseEntity.ok(service.save(roomType));
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
