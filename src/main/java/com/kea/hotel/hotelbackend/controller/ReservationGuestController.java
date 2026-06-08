package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.ReservationGuest;
import com.kea.hotel.hotelbackend.model.ReservationGuestKey;
import com.kea.hotel.hotelbackend.service.ReservationGuestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/reservation-guests")
public class ReservationGuestController {

    private final ReservationGuestService service;

    public ReservationGuestController(ReservationGuestService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ReservationGuest> getAllReservationGuests(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{reservationId}/{guestId}")
    public ResponseEntity<ReservationGuest> getReservationGuestById(
            @PathVariable Long reservationId,
            @PathVariable Long guestId) {
        ReservationGuestKey key = new ReservationGuestKey(reservationId, guestId);
        return service.findById(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReservationGuest createReservationGuest(@RequestBody ReservationGuest reservationGuest) {
        return service.save(reservationGuest);
    }

    @PutMapping("/{reservationId}/{guestId}")
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
    public ResponseEntity<Void> deleteReservationGuest(
            @PathVariable Long reservationId,
            @PathVariable Long guestId) {
        ReservationGuestKey key = new ReservationGuestKey(reservationId, guestId);
        service.delete(key);
        return ResponseEntity.noContent().build();
    }
}
