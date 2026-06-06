package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservationGuest;
import com.kea.hotel.hotelbackend.mongodb.service.MongoReservationGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongo/reservation-guests")
@RequiredArgsConstructor
public class MongoReservationGuestController {
    private final MongoReservationGuestService service;

    @PostMapping
    public ResponseEntity<MongoReservationGuest> create(@RequestBody MongoReservationGuest reservationGuest) {
        return ResponseEntity.ok(service.create(reservationGuest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoReservationGuest> findById(@PathVariable String id) {
        MongoReservationGuest reservationGuest = service.findById(id);
        return reservationGuest != null ? ResponseEntity.ok(reservationGuest) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<MongoReservationGuest>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoReservationGuest> update(@PathVariable String id, @RequestBody MongoReservationGuest reservationGuest) {
        MongoReservationGuest updated = service.update(id, reservationGuest);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
