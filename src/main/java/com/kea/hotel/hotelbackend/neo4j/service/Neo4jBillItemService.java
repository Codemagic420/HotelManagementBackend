package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBillItem;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jBillItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Neo4jBillItemService {
    private final Neo4jBillItemRepository repository;

    public Neo4jBillItemService(Neo4jBillItemRepository repository) {
        this.repository = repository;
    }

    public Page<Neo4jBillItem> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Neo4jBillItem> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jBillItem save(Neo4jBillItem item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
