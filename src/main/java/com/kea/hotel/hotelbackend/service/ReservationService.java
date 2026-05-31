package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import com.kea.hotel.hotelbackend.repository.SeasonRateRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationService {

    private final ReservationRepository repo;
    private final SeasonRateRepository seasonRateRepository;

    public ReservationService(ReservationRepository repo, SeasonRateRepository seasonRateRepository) {
        this.repo = repo;
        this.seasonRateRepository = seasonRateRepository;
    }

    public List<Reservation> findAll() {
        return repo.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Reservation> checkIn(Long id) {
        var opt = repo.findById(id);
        opt.ifPresent(r -> {
            r.setStatus("CHECKED_IN");
            repo.save(r);
        });
        return opt;
    }

    public Optional<Reservation> checkOut(Long id) {
        var opt = repo.findById(id);
        opt.ifPresent(r -> {
            r.setStatus("CHECKED_OUT");
            repo.save(r);
        });
        return opt;
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getBookedNightlyPrice() == null) {
            reservation.setBookedNightlyPrice(BigDecimal.ZERO);
        }

        if (reservation.getNights() == null) {
            if (reservation.getCheckInDate() != null && reservation.getCheckOutDate() != null) {
                long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
                reservation.setNights((int) days);
            }
        }
        // ensure bookedRate and price are set when possible
        if (reservation.getBookedRate() == null && reservation.getRoomType() != null && seasonRateRepository != null) {
            var rates = seasonRateRepository.findAll();
            for (var r : rates) {
                if (r.getRoomType() != null && r.getRoomType().getRoomTypeId().equals(reservation.getRoomType().getRoomTypeId())) {
                    reservation.setBookedRate(r);
                    reservation.setBookedNightlyPrice(r.getPricePerNight());
                    break;
                }
            }
        }

        // ensure referenceNo and status
        if (reservation.getReferenceNo() == null || reservation.getReferenceNo().isBlank()) {
            String ref = "REF" + Math.abs(java.util.UUID.randomUUID().toString().hashCode());
            if (ref.length() > 20) ref = ref.substring(0, 20);
            reservation.setReferenceNo(ref);
        }

        if (reservation.getStatus() == null) {
            reservation.setStatus("BOOKED");
        }

        try {
            return repo.save(reservation);
        } catch (DataIntegrityViolationException ex) {
            // If delete constraints or other FK issues happen elsewhere, rethrow
            throw ex;
        }
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
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            // fallback: mark as cancelled if there are dependent entities
            repo.findById(id).ifPresent(r -> {
                r.setStatus("CANCELLED");
                repo.save(r);
            });
        }
    }
}
