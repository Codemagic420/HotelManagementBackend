package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoInventoryItem;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoInventoryItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoInventoryItemService {
    private final MongoInventoryItemRepository repository;

    public MongoInventoryItemService(MongoInventoryItemRepository repository) {
        this.repository = repository;
    }

    public List<MongoInventoryItem> findAll() {
        return repository.findAll();
    }

    public Optional<MongoInventoryItem> findById(String id) {
        return repository.findById(id);
    }

    public MongoInventoryItem save(MongoInventoryItem item) {
        return repository.save(item);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
