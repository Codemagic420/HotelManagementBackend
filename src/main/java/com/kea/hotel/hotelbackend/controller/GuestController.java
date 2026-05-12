package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.service.GuestService;
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
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Guest createGuest(@RequestBody Guest guest) {
        return service.save(guest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @RequestBody Guest updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
