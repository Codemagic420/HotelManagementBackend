package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBill;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jBillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/bills")
public class Neo4jBillController {
    private final Neo4jBillService service;

    public Neo4jBillController(Neo4jBillService service) {
        this.service = service;
    }

    @GetMapping
    public List<Neo4jBill> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jBill> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Neo4jBill create(@RequestBody Neo4jBill bill) {
        return service.save(bill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jBill> update(@PathVariable Long id, @RequestBody Neo4jBill bill) {
        return service.findById(id).map(existing -> {
            bill.setBillId(id);
            return ResponseEntity.ok(service.save(bill));
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
