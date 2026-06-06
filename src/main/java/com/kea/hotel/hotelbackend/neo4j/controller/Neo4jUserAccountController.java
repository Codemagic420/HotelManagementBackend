package com.kea.hotel.hotelbackend.neo4j.controller;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jUserAccount;
import com.kea.hotel.hotelbackend.neo4j.service.Neo4jUserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neo4j/user-accounts")
@RequiredArgsConstructor
public class Neo4jUserAccountController {
    private final Neo4jUserAccountService service;

    @PostMapping
    public ResponseEntity<Neo4jUserAccount> create(@RequestBody Neo4jUserAccount userAccount) {
        return ResponseEntity.ok(service.create(userAccount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Neo4jUserAccount> findById(@PathVariable Long id) {
        Neo4jUserAccount userAccount = service.findById(id);
        return userAccount != null ? ResponseEntity.ok(userAccount) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Neo4jUserAccount>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Neo4jUserAccount> update(@PathVariable Long id, @RequestBody Neo4jUserAccount userAccount) {
        Neo4jUserAccount updated = service.update(id, userAccount);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
