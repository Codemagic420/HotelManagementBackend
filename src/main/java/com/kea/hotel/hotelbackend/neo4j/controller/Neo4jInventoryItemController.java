package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jInventoryItem;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jInventoryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/inventory-items")
public class Neo4jInventoryItemController {
    private final Neo4jInventoryItemService service;

    public Neo4jInventoryItemController(Neo4jInventoryItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jInventoryItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jInventoryItem> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jInventoryItem create(@RequestBody Neo4jInventoryItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jInventoryItem> update(@PathVariable Long id, @RequestBody Neo4jInventoryItem item) {
        return service.findById(id).map(existing -> {
            item.setInventoryItemId(id);
            return ResponseEntity.ok(service.save(item));
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
