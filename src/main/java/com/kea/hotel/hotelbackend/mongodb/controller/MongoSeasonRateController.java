package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoSeasonRate;
import com.kea.hotel.hotelbackend.mongodb.service.MongoSeasonRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/season-rates")
public class MongoSeasonRateController {
    private final MongoSeasonRateService service;

    public MongoSeasonRateController(MongoSeasonRateService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoSeasonRate> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoSeasonRate> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoSeasonRate create(@RequestBody MongoSeasonRate rate) {
        return service.save(rate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
