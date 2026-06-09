package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBill;
import com.kea.hotel.hotelbackend.mongodb.service.MongoBillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/mongodb/bills")
public class MongoBillController {
    private final MongoBillService service;

    public MongoBillController(MongoBillService service) {
        this.service = service;
    }

    @GetMapping
    public List<MongoBill> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoBill> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MongoBill create(@RequestBody MongoBill bill) {
        return service.save(bill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoBill> update(@PathVariable String id, @RequestBody MongoBill bill) {
        return service.findById(id).map(existing -> {
            bill.setBillId(existing.getBillId());
            return ResponseEntity.ok(service.save(bill));
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
