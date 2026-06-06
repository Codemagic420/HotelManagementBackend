package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBill;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jBillItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/bills")
@RequiredArgsConstructor
public class Neo4jBillItemController {
    private final Neo4jBillItemService service;

    @PostMapping
    public ResponseEntity<Neo4jBill> create(@RequestBody Neo4jBill bill) {
        return ResponseEntity.ok(service.create(bill));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jBill> findById(@PathVariable Long id) {
        Neo4jBill bill = service.findById(id);
        return bill != null ? ResponseEntity.ok(bill) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Neo4jBill>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jBill> update(@PathVariable Long id, @RequestBody Neo4jBill bill) {
        Neo4jBill updated = service.update(id, bill);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
