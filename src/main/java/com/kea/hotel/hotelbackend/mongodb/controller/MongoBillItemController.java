package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBill;
import com.kea.hotel.hotelbackend.mongodb.service.MongoBillItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongo/bills")
@RequiredArgsConstructor
public class MongoBillItemController {
    private final MongoBillItemService service;

    @PostMapping
    public ResponseEntity<MongoBill> create(@RequestBody MongoBill bill) {
        return ResponseEntity.ok(service.create(bill));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoBill> findById(@PathVariable String id) {
        MongoBill bill = service.findById(id);
        return bill != null ? ResponseEntity.ok(bill) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<MongoBill>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoBill> update(@PathVariable String id, @RequestBody MongoBill bill) {
        MongoBill updated = service.update(id, bill);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
