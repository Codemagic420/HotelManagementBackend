package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jGuest;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jGuestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/guests")
@SecurityRequirement(name = "bearerAuth")
public class Neo4jGuestController {
    private final Neo4jGuestService service;

    public Neo4jGuestController(Neo4jGuestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jGuest> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jGuest> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jGuest create(@RequestBody Neo4jGuest guest) {
        return service.save(guest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
