package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.InventoryItem;
import com.kea.hotel.hotelbackend.service.InventoryItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/inventory-items")
public class InventoryItemController {

    private final InventoryItemService service;

    public InventoryItemController(InventoryItemService service) {
        this.service = service;
    }

    @GetMapping
    public Page<InventoryItem> getAllInventoryItems(
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(active, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public InventoryItem createInventoryItem(@RequestBody InventoryItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id, @RequestBody InventoryItem updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
