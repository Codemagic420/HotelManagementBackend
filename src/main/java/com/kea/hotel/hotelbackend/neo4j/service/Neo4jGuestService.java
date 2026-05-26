package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jGuest;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jGuestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Neo4jGuestService {
    private final Neo4jGuestRepository repository;

    public Neo4jGuestService(Neo4jGuestRepository repository) {
        this.repository = repository;
    }

    public Page<Neo4jGuest> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Neo4jGuest> findAllList() {
        return repository.findAll();
    }

    public Optional<Neo4jGuest> findById(Long id) {
        return repository.findById(id);
    }

    public Neo4jGuest save(Neo4jGuest guest) {
        return repository.save(guest);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
