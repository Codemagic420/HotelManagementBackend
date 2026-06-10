package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.SeasonRate;
import com.kea.hotel.hotelbackend.service.SeasonRateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/season-rates")
@SecurityRequirement(name = "bearerAuth")
public class SeasonRateController {

    private final SeasonRateService service;

    public SeasonRateController(SeasonRateService service) {
        this.service = service;
    }

    @GetMapping
    public Page<SeasonRate> getAllSeasonRates(
            @RequestParam(required = false) String season,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(season, pageable);
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
