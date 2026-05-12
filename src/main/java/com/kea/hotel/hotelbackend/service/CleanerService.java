package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Cleaner;
import com.kea.hotel.hotelbackend.repository.CleanerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CleanerService {

    private final CleanerRepository repo;

    public CleanerService(CleanerRepository repo) {
        this.repo = repo;
    }

    public List<Cleaner> findAll() {
        return repo.findAll();
    }

    public Optional<Cleaner> findById(Long id) {
        return repo.findById(id);
    }

    public Cleaner save(Cleaner cleaner) {
        return repo.save(cleaner);
    }

    public Optional<Cleaner> update(Long id, Cleaner updated) {
        return repo.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setPhone(updated.getPhone());
            existing.setActive(updated.getActive());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
