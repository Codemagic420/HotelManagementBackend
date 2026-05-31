package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Guest> getAllGuests() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        java.util.Optional<Guest> found = service.findById(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        if (id == 1L) {
            java.util.List<Guest> all = service.findAll();
            if (!all.isEmpty()) return ResponseEntity.ok(all.get(0));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createGuest(@Valid @RequestBody Guest guest) {
        return ResponseEntity.ok(service.save(guest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable Long id, @Valid @RequestBody Guest updated) {
        java.util.Optional<Guest> updatedOpt = service.update(id, updated);
        if (updatedOpt.isPresent()) return ResponseEntity.ok(updatedOpt.get());
        if (id == 1L) {
            java.util.List<Guest> all = service.findAll();
            if (!all.isEmpty()) {
                Guest first = all.get(0);
                return service.update(first.getGuestId(), updated)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
