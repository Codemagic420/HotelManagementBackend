package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.RoomCleaningTask;
import com.kea.hotel.hotelbackend.service.RoomCleaningTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-cleaning-tasks")
public class RoomCleaningTaskController {

    private final RoomCleaningTaskService service;

    public RoomCleaningTaskController(RoomCleaningTaskService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLEANER')")
    public List<RoomCleaningTask> getAllRoomCleaningTasks() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CLEANER')")
    public ResponseEntity<RoomCleaningTask> getRoomCleaningTaskById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public RoomCleaningTask createRoomCleaningTask(@RequestBody RoomCleaningTask task) {
        return service.save(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomCleaningTask> updateRoomCleaningTask(@PathVariable Long id, @RequestBody RoomCleaningTask updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoomCleaningTask(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
