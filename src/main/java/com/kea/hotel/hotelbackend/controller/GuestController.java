package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.dto.GuestCreateUpdateDTO;
import com.kea.hotel.hotelbackend.dto.GuestResponseDTO;
import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.service.GuestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<GuestResponseDTO> getAllGuests(
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
    public ResponseEntity<GuestResponseDTO> getGuestById(@PathVariable Long id) {
        return service.findById(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public GuestResponseDTO createGuest(@RequestBody GuestCreateUpdateDTO dto) {
        Guest guest = mapToEntity(dto);
        return mapToResponseDTO(service.save(guest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponseDTO> updateGuest(@PathVariable Long id, @RequestBody GuestCreateUpdateDTO dto) {
        Guest updated = mapToEntity(dto);
        return service.update(id, updated)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enrich-ai")
    public ResponseEntity<GuestResponseDTO> enrichGuestWithAI(@PathVariable Long id) {
        return service.enrichWithAI(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/ai-profile")
    public ResponseEntity<GuestResponseDTO> updateAIProfile(
            @PathVariable Long id,
            @RequestBody AIProfileRequest request) {
        return service.updateAIProfile(id, request.getProfile())
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class AIProfileRequest {
        private String profile;

        public AIProfileRequest() {}

        public AIProfileRequest(String profile) {
            this.profile = profile;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }

    private GuestResponseDTO mapToResponseDTO(Guest guest) {
        return new GuestResponseDTO(
                guest.getGuestId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getCreditCardLast4(),
                guest.getAiProfileSummary()
        );
    }

    private Guest mapToEntity(GuestCreateUpdateDTO dto) {
        Guest guest = new Guest();
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setPhone(dto.getPhone());
        guest.setCreditCardLast4(dto.getCreditCardLast4());
        return guest;
    }
}
