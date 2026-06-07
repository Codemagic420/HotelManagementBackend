package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.service.GuestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Guest> getAllGuests(
            @RequestParam(required = false) String lastName,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(lastName, pageable);
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
