package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.dto.ReservationCreateUpdateDTO;
import com.kea.hotel.hotelbackend.dto.ReservationResponseDTO;
import com.kea.hotel.hotelbackend.dto.GuestResponseDTO;
import com.kea.hotel.hotelbackend.dto.RoomResponseDTO;
import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ReservationResponseDTO> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(",");
            String field = parts[0];
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return service.findAll(pageable).map(this::mapToResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return service.findById(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReservationResponseDTO createReservation(@RequestBody ReservationCreateUpdateDTO dto) {
        Reservation reservation = mapToEntity(dto);
        return mapToResponseDTO(service.save(reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationCreateUpdateDTO dto) {
        Reservation updated = mapToEntity(dto);
        return service.update(id, updated)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enrich-ai")
    public ResponseEntity<ReservationResponseDTO> enrichReservationWithAI(@PathVariable Long id) {
        return service.enrichWithAI(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/ai-notes")
    public ResponseEntity<ReservationResponseDTO> updateAINotes(
            @PathVariable Long id,
            @RequestBody AINoteRequest request) {
        return service.updateAINotes(id, request.getNotes())
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class AINoteRequest {
        private String notes;

        public AINoteRequest() {}

        public AINoteRequest(String notes) {
            this.notes = notes;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    private ReservationResponseDTO mapToResponseDTO(Reservation reservation) {
        GuestResponseDTO guestDTO = null;
        if (reservation.getGuest() != null) {
            guestDTO = new GuestResponseDTO(
                    reservation.getGuest().getGuestId(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getEmail(),
                    reservation.getGuest().getPhone(),
                    reservation.getGuest().getCreditCardLast4(),
                    reservation.getGuest().getAiProfileSummary()
            );
        }

        RoomResponseDTO roomDTO = null;
        if (reservation.getRoom() != null) {
            roomDTO = new RoomResponseDTO(
                    reservation.getRoom().getRoomId(),
                    reservation.getRoom().getRoomNumber(),
                    reservation.getRoom().getType(),
                    reservation.getRoom().getRoomStatus(),
                    reservation.getRoom().getCleanStatus(),
                    reservation.getRoom().getOccupied(),
                    reservation.getRoom().getAiAssessmentSummary()
            );
        }

        return new ReservationResponseDTO(
                reservation.getReservationId(),
                reservation.getReferenceNo(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getNights(),
                reservation.getNumGuests(),
                reservation.getBookedNightlyPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getAiNotesSummary(),
                guestDTO,
                roomDTO
        );
    }

    private Reservation mapToEntity(ReservationCreateUpdateDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setReferenceNo(dto.getReferenceNo());
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNights(dto.getNights());
        reservation.setNumGuests(dto.getNumGuests());
        reservation.setBookedNightlyPrice(dto.getBookedNightlyPrice());
        reservation.setStatus(dto.getStatus());
        return reservation;
    }
}
