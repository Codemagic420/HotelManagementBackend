package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.repository.BillItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillItemService {

    private final BillItemRepository repo;

    public BillItemService(BillItemRepository repo) {
        this.repo = repo;
    }

    public List<BillItem> findAll() {
        return repo.findAll();
    }

    public Page<BillItem> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<BillItem> findAll(String itemType, Pageable pageable) {
        return itemType != null ? repo.findByItemType(itemType, pageable) : repo.findAll(pageable);
    }

    public Optional<BillItem> findById(Long id) {
        return repo.findById(id);
    }

    public BillItem save(BillItem billItem) {
        return repo.save(billItem);
    }

    public Optional<BillItem> update(Long id, BillItem updated) {
        return repo.findById(id).map(existing -> {
            existing.setBill(updated.getBill());
            existing.setItemType(updated.getItemType());
            existing.setDescription(updated.getDescription());
            existing.setQuantity(updated.getQuantity());
            existing.setUnitPrice(updated.getUnitPrice());
            existing.setLineTotal(updated.getLineTotal());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
