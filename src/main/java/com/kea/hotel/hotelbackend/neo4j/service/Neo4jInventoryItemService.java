package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jInventoryItem;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jInventoryItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jInventoryItemService {
    private final Neo4jInventoryItemRepository repository;

    public Neo4jInventoryItemService(Neo4jInventoryItemRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jInventoryItem> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jInventoryItem> findById(String id) {
        return repository.findById(id);
    }

    public Neo4jInventoryItem save(Neo4jInventoryItem item) {
        return repository.save(item);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
