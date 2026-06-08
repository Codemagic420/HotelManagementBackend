package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBillItem;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoBillItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoBillItemService {
    private final MongoBillItemRepository repository;

    public MongoBillItemService(MongoBillItemRepository repository) {
        this.repository = repository;
    }

    public Page<MongoBillItem> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<MongoBillItem> findById(String id) {
        return repository.findById(id);
    }

    public List<MongoBillItem> findByBillId(Long billId) {
        return repository.findByBillId(billId);
    }

    public MongoBillItem save(MongoBillItem item) {
        return repository.save(item);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
