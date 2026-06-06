package com.kea.hotel.hotelbackend.mongodb.controller;

import com.kea.hotel.hotelbackend.mongodb.document.MongoUserAccount;
import com.kea.hotel.hotelbackend.mongodb.service.MongoUserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mongo/user-accounts")
@RequiredArgsConstructor
public class MongoUserAccountController {
    private final MongoUserAccountService service;

    @PostMapping
    public ResponseEntity<MongoUserAccount> create(@RequestBody MongoUserAccount userAccount) {
        return ResponseEntity.ok(service.create(userAccount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MongoUserAccount> findById(@PathVariable String id) {
        MongoUserAccount userAccount = service.findById(id);
        return userAccount != null ? ResponseEntity.ok(userAccount) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<MongoUserAccount>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MongoUserAccount> update(@PathVariable String id, @RequestBody MongoUserAccount userAccount) {
        MongoUserAccount updated = service.update(id, userAccount);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
