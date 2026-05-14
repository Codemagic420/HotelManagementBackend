package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.InventoryItem;
import com.kea.hotel.hotelbackend.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemService {

    private final InventoryItemRepository repo;

    public InventoryItemService(InventoryItemRepository repo) {
        this.repo = repo;
    }

    public List<InventoryItem> findAll() {
        return repo.findAll();
    }

    public Optional<InventoryItem> findById(Long id) {
        return repo.findById(id);
    }

    public InventoryItem save(InventoryItem item) {
        return repo.save(item);
    }

    public Optional<InventoryItem> update(Long id, InventoryItem updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setUnitPrice(updated.getUnitPrice());
            existing.setActive(updated.getActive());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
