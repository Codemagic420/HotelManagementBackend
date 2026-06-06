package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jExtraService;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jExtraServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/extra-services")
public class Neo4jExtraServiceController {
    private final Neo4jExtraServiceService service;

    public Neo4jExtraServiceController(Neo4jExtraServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jExtraService> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jExtraService> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jExtraService create(@RequestBody Neo4jExtraService extraService) {
        return service.save(extraService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jExtraService> update(@PathVariable Long id, @RequestBody Neo4jExtraService extraService) {
        return service.findById(id).map(existing -> {
            extraService.setExtraServiceId(id);
            return ResponseEntity.ok(service.save(extraService));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
