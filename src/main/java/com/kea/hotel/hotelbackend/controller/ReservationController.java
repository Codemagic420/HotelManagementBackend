package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import com.kea.hotel.hotelbackend.repository.RoomTypeRepository;
import com.kea.hotel.hotelbackend.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/mysql/reservations")
public class ReservationController {

    private final ReservationService service;
    private final GuestRepository guestRepository;
    private final RoomTypeRepository roomTypeRepository;

    public ReservationController(ReservationService service, GuestRepository guestRepository, RoomTypeRepository roomTypeRepository) {
        this.service = service;
        this.guestRepository = guestRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @GetMapping
    public Page<Reservation> getAllReservations(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody java.util.Map<String, Object> payload) {
        try {
            Object guestIdObj = payload.get("guestId");
            Object roomTypeIdObj = payload.get("roomTypeId");
            Object checkInObj = payload.get("checkInDate");
            Object checkOutObj = payload.get("checkOutDate");
            Object numGuestsObj = payload.get("numberOfGuests");

            if (guestIdObj == null) return ResponseEntity.badRequest().body("missing guestId");
            if (roomTypeIdObj == null) return ResponseEntity.badRequest().body("missing roomTypeId");
            if (checkInObj == null || checkOutObj == null) return ResponseEntity.badRequest().body("missing dates");

            Long guestId = ((Number) guestIdObj).longValue();
            Long roomTypeId = ((Number) roomTypeIdObj).longValue();

            var guestOpt = guestRepository.findById(guestId);
            if (guestOpt.isEmpty()) return ResponseEntity.badRequest().body("guest not found");

            var roomTypeOpt = roomTypeRepository.findById(roomTypeId);
            if (roomTypeOpt.isEmpty()) return ResponseEntity.badRequest().body("room type not found");

            java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInObj.toString());
            java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutObj.toString());

            // validate dates
            java.time.LocalDate today = java.time.LocalDate.now();
            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                return ResponseEntity.badRequest().body("invalid dates");
            }
            if (checkIn.isBefore(today)) {
                return ResponseEntity.badRequest().body("check-in in past");
            }

            Integer numGuests = null;
            if (numGuestsObj != null) {
                numGuests = ((Number) numGuestsObj).intValue();
                if (numGuests <= 0) return ResponseEntity.badRequest().body("invalid numberOfGuests");
            } else {
                return ResponseEntity.badRequest().body("missing numberOfGuests");
            }

            Reservation reservation = new Reservation();
            reservation.setGuest(guestOpt.get());
            reservation.setRoomType(roomTypeOpt.get());
            reservation.setCheckInDate(checkIn);
            reservation.setCheckOutDate(checkOut);
            reservation.setNumGuests(numGuests);

            return ResponseEntity.ok(service.save(reservation));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("invalid payload");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/check-in")
    public ResponseEntity<Void> checkIn(@PathVariable Long id) {
        var opt = service.checkIn(id);
        return opt.isPresent() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/check-out")
    public ResponseEntity<Void> checkOut(@PathVariable Long id) {
        var opt = service.checkOut(id);
        return opt.isPresent() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
