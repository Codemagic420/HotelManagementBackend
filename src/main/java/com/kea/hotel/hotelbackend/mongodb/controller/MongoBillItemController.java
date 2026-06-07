package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBillItem;
import com.kea.hotel.hotelbackend.mongodb.service.MongoBillItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongodb/bill-items")
public class MongoBillItemController {
    private final MongoBillItemService service;

    public MongoBillItemController(MongoBillItemService service) {
        this.service = service;
    }

    @GetMapping
    public Page<MongoBillItem> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoBillItem> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-bill/{billId}")
    public List<MongoBillItem> getByBillId(@PathVariable Long billId) {
        return service.findByBillId(billId);
    }

    @PostMapping
    public MongoBillItem create(@RequestBody MongoBillItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoBillItem> update(@PathVariable String id, @RequestBody MongoBillItem item) {
        return service.findById(id).map(existing -> {
            item.setId(existing.getId());
            return ResponseEntity.ok(service.save(item));
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
