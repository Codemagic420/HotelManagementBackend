package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Cleaner;
import com.kea.hotel.hotelbackend.service.CleanerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/cleaners")
public class CleanerController {

    private final CleanerService service;

    public CleanerController(CleanerService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Cleaner> getAllCleaners(
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(active, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaner> getCleanerById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cleaner createCleaner(@RequestBody Cleaner cleaner) {
        return service.save(cleaner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cleaner> updateCleaner(@PathVariable Long id, @RequestBody Cleaner updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCleaner(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
