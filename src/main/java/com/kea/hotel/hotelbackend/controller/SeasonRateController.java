package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.SeasonRate;
import com.kea.hotel.hotelbackend.service.SeasonRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/season-rates")
public class SeasonRateController {

    private final SeasonRateService service;

    public SeasonRateController(SeasonRateService service) {
        this.service = service;
    }

    @GetMapping
    public List<SeasonRate> getAllSeasonRates() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonRate> getSeasonRateById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SeasonRate createSeasonRate(@RequestBody SeasonRate seasonRate) {
        return service.save(seasonRate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeasonRate> updateSeasonRate(@PathVariable Long id, @RequestBody SeasonRate updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeasonRate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
