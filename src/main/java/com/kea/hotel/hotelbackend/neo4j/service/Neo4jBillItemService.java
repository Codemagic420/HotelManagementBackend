package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jBill;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Neo4jBillItemService {
    private final Neo4jBillRepository repository;

    public Neo4jBill create(Neo4jBill bill) {
        return repository.save(bill);
    }

    public Neo4jBill findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Neo4jBill> findAll() {
        return repository.findAll();
    }

    public Neo4jBill update(Long id, Neo4jBill bill) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setReservationId(bill.getReservationId());
                    existing.setTotalAmount(bill.getTotalAmount());
                    existing.setStatus(bill.getStatus());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
