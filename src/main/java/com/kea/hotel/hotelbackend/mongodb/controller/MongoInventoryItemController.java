package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoInventoryItem;
import com.kea.hotel.hotelbackend.mongodb.service.MongoInventoryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/inventory-items")
public class MongoInventoryItemController {
    private final MongoInventoryItemService service;

    public MongoInventoryItemController(MongoInventoryItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoInventoryItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoInventoryItem> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoInventoryItem create(@RequestBody MongoInventoryItem item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
