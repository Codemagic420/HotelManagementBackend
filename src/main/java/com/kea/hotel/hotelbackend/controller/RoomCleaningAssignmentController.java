package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.RoomCleaningAssignment;
import com.kea.hotel.hotelbackend.model.RoomCleaningAssignmentKey;
import com.kea.hotel.hotelbackend.service.RoomCleaningAssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/room-cleaning-assignments")
public class RoomCleaningAssignmentController {

    private final RoomCleaningAssignmentService service;

    public RoomCleaningAssignmentController(RoomCleaningAssignmentService service) {
        this.service = service;
    }

    @GetMapping
    public Page<RoomCleaningAssignment> getAllRoomCleaningAssignments(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{taskId}/{cleanerId}")
    public ResponseEntity<RoomCleaningAssignment> getRoomCleaningAssignmentById(
            @PathVariable Long taskId,
            @PathVariable Long cleanerId) {
        RoomCleaningAssignmentKey key = new RoomCleaningAssignmentKey(taskId, cleanerId);
        return service.findById(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RoomCleaningAssignment createRoomCleaningAssignment(@RequestBody RoomCleaningAssignment assignment) {
        return service.save(assignment);
    }

    @PutMapping("/{taskId}/{cleanerId}")
    public ResponseEntity<RoomCleaningAssignment> updateRoomCleaningAssignment(
            @PathVariable Long taskId,
            @PathVariable Long cleanerId,
            @RequestBody RoomCleaningAssignment updated) {
        RoomCleaningAssignmentKey key = new RoomCleaningAssignmentKey(taskId, cleanerId);
        return service.update(key, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{taskId}/{cleanerId}")
    public ResponseEntity<Void> deleteRoomCleaningAssignment(
            @PathVariable Long taskId,
            @PathVariable Long cleanerId) {
        RoomCleaningAssignmentKey key = new RoomCleaningAssignmentKey(taskId, cleanerId);
        service.delete(key);
        return ResponseEntity.noContent().build();
    }
}
