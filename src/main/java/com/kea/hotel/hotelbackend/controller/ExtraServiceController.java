package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.ExtraService;
import com.kea.hotel.hotelbackend.service.ExtraServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extra-services")
public class ExtraServiceController {

    private final ExtraServiceService service;

    public ExtraServiceController(ExtraServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExtraService> getAllExtraServices() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtraService> getExtraServiceById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ExtraService createExtraService(@RequestBody ExtraService service) {
        return this.service.save(service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExtraService> updateExtraService(@PathVariable Long id, @RequestBody ExtraService updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtraService(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
