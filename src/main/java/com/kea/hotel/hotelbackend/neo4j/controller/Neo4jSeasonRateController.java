package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jSeasonRate;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jSeasonRateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/season-rates")
@SecurityRequirement(name = "bearerAuth")
public class Neo4jSeasonRateController {
    private final Neo4jSeasonRateService service;

    public Neo4jSeasonRateController(Neo4jSeasonRateService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jSeasonRate> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jSeasonRate> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jSeasonRate create(@RequestBody Neo4jSeasonRate seasonRate) {
        return service.save(seasonRate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jSeasonRate> update(@PathVariable String id, @RequestBody Neo4jSeasonRate seasonRate) {
        return service.findById(id).map(existing -> {
            seasonRate.setElementId(existing.getElementId());
            return ResponseEntity.ok(service.save(seasonRate));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
