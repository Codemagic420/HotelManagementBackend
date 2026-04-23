package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository repo;

    public GuestService(GuestRepository repo) {
        this.repo = repo;
    }

    public List<Guest> findAll() {
        return repo.findAll();
    }

    public Optional<Guest> findById(Long id) {
        return repo.findById(id);
    }

    public Guest save(Guest guest) {
        return repo.save(guest);
    }

    public Optional<Guest> update(Long id, Guest updated) {
        return repo.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setEmail(updated.getEmail());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
