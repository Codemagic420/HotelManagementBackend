package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.ReservationGuest;
import com.kea.hotel.hotelbackend.model.ReservationGuestKey;
import com.kea.hotel.hotelbackend.service.ReservationGuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation-guests")
public class ReservationGuestController {

    private final ReservationGuestService service;

    public ReservationGuestController(ReservationGuestService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<ReservationGuest> getAllReservationGuests() {
        return service.findAll();
    }

    @GetMapping("/{reservationId}/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReservationGuest> getReservationGuestById(
            @PathVariable Long reservationId,
            @PathVariable Long guestId) {
        ReservationGuestKey key = new ReservationGuestKey(reservationId, guestId);
        return service.findById(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ReservationGuest createReservationGuest(@RequestBody ReservationGuest reservationGuest) {
        return service.save(reservationGuest);
    }

    @PutMapping("/{reservationId}/{guestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationGuest> updateReservationGuest(
            @PathVariable Long reservationId,
            @PathVariable Long guestId,
            @RequestBody ReservationGuest updated) {
        ReservationGuestKey key = new ReservationGuestKey(reservationId, guestId);
        return service.update(key, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reservationId}/{guestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReservationGuest(
            @PathVariable Long reservationId,
            @PathVariable Long guestId) {
        ReservationGuestKey key = new ReservationGuestKey(reservationId, guestId);
        service.delete(key);
        return ResponseEntity.noContent().build();
    }
}
