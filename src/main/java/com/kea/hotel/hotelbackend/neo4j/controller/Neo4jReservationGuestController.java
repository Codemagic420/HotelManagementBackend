package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jReservationGuest;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jReservationGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/reservation-guests")
@RequiredArgsConstructor
public class Neo4jReservationGuestController {
    private final Neo4jReservationGuestService service;

    @PostMapping
    public ResponseEntity<Neo4jReservationGuest> create(@RequestBody Neo4jReservationGuest reservationGuest) {
        return ResponseEntity.ok(service.create(reservationGuest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jReservationGuest> findById(@PathVariable Long id) {
        Neo4jReservationGuest reservationGuest = service.findById(id);
        return reservationGuest != null ? ResponseEntity.ok(reservationGuest) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Neo4jReservationGuest>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jReservationGuest> update(@PathVariable Long id, @RequestBody Neo4jReservationGuest reservationGuest) {
        Neo4jReservationGuest updated = service.update(id, reservationGuest);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
