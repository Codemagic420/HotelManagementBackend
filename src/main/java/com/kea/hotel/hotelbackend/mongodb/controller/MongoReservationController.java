package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservation;
import com.kea.hotel.hotelbackend.mongodb.service.MongoReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/reservations")
public class MongoReservationController {
    private final MongoReservationService service;

    public MongoReservationController(MongoReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoReservation> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoReservation> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoReservation create(@RequestBody MongoReservation reservation) {
        return service.save(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoReservation> update(@PathVariable String id, @RequestBody MongoReservation reservation) {
        return service.findById(id).map(existing -> {
            reservation.setReservationId(existing.getReservationId());
            return ResponseEntity.ok(service.save(reservation));
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
