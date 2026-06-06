package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBill;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoBillItemService {
    private final MongoBillRepository repository;

    public MongoBill create(MongoBill bill) {
        return repository.save(bill);
    }

    public MongoBill findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<MongoBill> findAll() {
        return repository.findAll();
    }

    public MongoBill update(String id, MongoBill bill) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setReservationId(bill.getReservationId());
                    existing.setTotalAmount(bill.getTotalAmount());
                    existing.setStatus(bill.getStatus());
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
