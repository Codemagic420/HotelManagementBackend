package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBillItem;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jBillItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/neo4j/bill-items")
public class Neo4jBillItemController {
    private final Neo4jBillItemService service;

    public Neo4jBillItemController(Neo4jBillItemService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Neo4jBillItem> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jBillItem> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jBillItem create(@RequestBody Neo4jBillItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jBillItem> update(@PathVariable Long id, @RequestBody Neo4jBillItem item) {
        return service.findById(id).map(existing -> {
            item.setId(existing.getId());
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
