package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBill;
import com.kea.hotel.hotelbackend.mongodb.repository.MongoBillRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MongoBillService {
    private final MongoBillRepository repository;

    public MongoBillService(MongoBillRepository repository) {
        this.repository = repository;
    }

    public List<MongoBill> findAll() {
        return repository.findAll();
    }

    public Optional<MongoBill> findById(String id) {
        return repository.findById(id);
    }

    public MongoBill save(MongoBill bill) {
        return repository.save(bill);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
