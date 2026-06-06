package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoUserAccount;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoUserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoUserAccountService {
    private final MongoUserAccountRepository repository;

    public MongoUserAccount create(MongoUserAccount userAccount) {
        return repository.save(userAccount);
    }

    public MongoUserAccount findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<MongoUserAccount> findAll() {
        return repository.findAll();
    }

    public MongoUserAccount update(String id, MongoUserAccount userAccount) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUsername(userAccount.getUsername());
                    existing.setPasswordHash(userAccount.getPasswordHash());
                    existing.setRole(userAccount.getRole());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
