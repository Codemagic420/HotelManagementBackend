package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repo;
    private final AiEnrichmentService aiEnrichmentService;

    public ReservationService(ReservationRepository repo, AiEnrichmentService aiEnrichmentService) {
        this.repo = repo;
        this.aiEnrichmentService = aiEnrichmentService;
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

    public Optional<Reservation> enrichWithAI(Long id) {
        return repo.findById(id).map(reservation -> {
            String summary = aiEnrichmentService.generateReservationNotesSummary(reservation);
            reservation.setAiNotesSummary(summary);
            reservation.setAiFieldsUpdatedAt(LocalDateTime.now());
            return repo.save(reservation);
        });
    }
}
