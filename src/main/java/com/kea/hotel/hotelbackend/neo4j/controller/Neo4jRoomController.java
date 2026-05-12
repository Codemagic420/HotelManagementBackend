package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoom;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/rooms")
public class Neo4jRoomController {
    private final Neo4jRoomService service;

    public Neo4jRoomController(Neo4jRoomService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jRoom> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jRoom> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jRoom create(@RequestBody Neo4jRoom room) {
        return service.save(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
