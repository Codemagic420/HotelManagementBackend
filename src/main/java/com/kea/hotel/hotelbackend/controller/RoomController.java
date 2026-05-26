package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.dto.RoomCreateUpdateDTO;
import com.kea.hotel.hotelbackend.dto.RoomResponseDTO;
import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public Page<RoomResponseDTO> getAllRooms(
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
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        return service.findById(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RoomResponseDTO createRoom(@RequestBody RoomCreateUpdateDTO dto) {
        Room room = mapToEntity(dto);
        return mapToResponseDTO(service.save(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id, @RequestBody RoomCreateUpdateDTO dto) {
        Room updated = mapToEntity(dto);
        return service.update(id, updated)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enrich-ai")
    public ResponseEntity<RoomResponseDTO> enrichRoomWithAI(@PathVariable Long id) {
        return service.enrichWithAI(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/ai-assessment")
    public ResponseEntity<RoomResponseDTO> updateAIAssessment(
            @PathVariable Long id,
            @RequestBody AIAssessmentRequest request) {
        return service.updateAIAssessment(id, request.getAssessment())
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class AIAssessmentRequest {
        private String assessment;

        public AIAssessmentRequest() {}

        public AIAssessmentRequest(String assessment) {
            this.assessment = assessment;
        }

        public String getAssessment() {
            return assessment;
        }

        public void setAssessment(String assessment) {
            this.assessment = assessment;
        }
    }

    private RoomResponseDTO mapToResponseDTO(Room room) {
        return new RoomResponseDTO(
                room.getRoomId(),
                room.getRoomNumber(),
                room.getType(),
                room.getRoomStatus(),
                room.getCleanStatus(),
                room.getOccupied(),
                room.getAiAssessmentSummary()
        );
    }

    private Room mapToEntity(RoomCreateUpdateDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomStatus(dto.getRoomStatus());
        room.setCleanStatus(dto.getCleanStatus());
        room.setOccupied(dto.getOccupied());
        room.setType(dto.getType());
        return room;
    }
}
