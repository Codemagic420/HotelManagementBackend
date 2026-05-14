package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.ReservationGuest;
import com.kea.hotel.hotelbackend.model.ReservationGuestKey;
import com.kea.hotel.hotelbackend.repository.ReservationGuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationGuestService {

    private final ReservationGuestRepository repo;

    public ReservationGuestService(ReservationGuestRepository repo) {
        this.repo = repo;
    }

    public List<ReservationGuest> findAll() {
        return repo.findAll();
    }

    public Optional<ReservationGuest> findById(ReservationGuestKey id) {
        return repo.findById(id);
    }

    public ReservationGuest save(ReservationGuest reservationGuest) {
        return repo.save(reservationGuest);
    }

    public Optional<ReservationGuest> update(ReservationGuestKey id, ReservationGuest updated) {
        return repo.findById(id).map(existing -> {
            existing.setIsPrimary(updated.getIsPrimary());
            return repo.save(existing);
        });
    }

    public void delete(ReservationGuestKey id) {
        repo.deleteById(id);
    }
}
