package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jReservation;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/reservations")
@SecurityRequirement(name = "bearerAuth")
public class Neo4jReservationController {
    private final Neo4jReservationService service;

    public Neo4jReservationController(Neo4jReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jReservation> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jReservation> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jReservation create(@RequestBody Neo4jReservation reservation) {
        return service.save(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
