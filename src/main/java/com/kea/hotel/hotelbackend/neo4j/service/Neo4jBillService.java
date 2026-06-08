package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBill;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jBillRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jBillService {
    private final Neo4jBillRepository repository;

    public Neo4jBillService(Neo4jBillRepository repository) {
        this.repository = repository;
    }

    public List<Neo4jBill> findAll() {
        return repository.findAll();
    }

    public Optional<Neo4jBill> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jBill save(Neo4jBill bill) {
        return repository.save(bill);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
