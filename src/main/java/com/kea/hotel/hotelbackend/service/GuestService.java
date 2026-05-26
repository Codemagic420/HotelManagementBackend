package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository repo;
    private final AiEnrichmentService aiEnrichmentService;

    public GuestService(GuestRepository repo, AiEnrichmentService aiEnrichmentService) {
        this.repo = repo;
        this.aiEnrichmentService = aiEnrichmentService;
    }

    public Page<Guest> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<Guest> findAllList() {
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

    public Optional<Guest> enrichWithAI(Long id) {
        return repo.findById(id).map(guest -> {
            String summary = aiEnrichmentService.generateGuestProfileSummary(guest);
            guest.setAiProfileSummary(summary);
            guest.setAiFieldsUpdatedAt(LocalDateTime.now());
            return repo.save(guest);
        });
    }

    public Optional<Guest> updateAIProfile(Long id, String profile) {
        return repo.findById(id).map(guest -> {
            guest.setAiProfileSummary(profile);
            guest.setAiFieldsUpdatedAt(LocalDateTime.now());
            return repo.save(guest);
        });
    }
}
