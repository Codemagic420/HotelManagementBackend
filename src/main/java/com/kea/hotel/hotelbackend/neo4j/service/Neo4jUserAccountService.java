package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jUserAccount;
import com.kea.hotel.hotelbackend.neo4j.repository.Neo4jUserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Neo4jUserAccountService {
    private final Neo4jUserAccountRepository repository;

    public Neo4jUserAccount create(Neo4jUserAccount userAccount) {
        return repository.save(userAccount);
    }

    public Neo4jUserAccount findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Neo4jUserAccount> findAll() {
        return repository.findAll();
    }

    public Neo4jUserAccount update(Long id, Neo4jUserAccount userAccount) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUsername(userAccount.getUsername());
                    existing.setPasswordHash(userAccount.getPasswordHash());
                    existing.setRole(userAccount.getRole());
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
