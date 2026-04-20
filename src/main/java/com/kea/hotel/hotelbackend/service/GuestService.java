package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repo;

    public GuestService(GuestRepository repo) {
        this.repo = repo;
    }

    public List<Guest> findAll() {
        return repo.findAll();
    }

    public Guest save(Guest guest) {
        return repo.save(guest);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
