package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    public List<Reservation> findAll() {
        return repo.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return repo.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return repo.save(reservation);
    }

    public Optional<Reservation> update(Long id, Reservation updated) {
        return repo.findById(id).map(existing -> {
            existing.setGuest(updated.getGuest());
            existing.setRoom(updated.getRoom());
            existing.setCheckInDate(updated.getCheckInDate());
            existing.setCheckOutDate(updated.getCheckOutDate());
            existing.setStatus(updated.getStatus());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
