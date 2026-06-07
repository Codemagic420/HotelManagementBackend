package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.SeasonRate;
import com.kea.hotel.hotelbackend.repository.SeasonRateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonRateService {

    private final SeasonRateRepository repo;

    public SeasonRateService(SeasonRateRepository repo) {
        this.repo = repo;
    }

    public List<SeasonRate> findAll() {
        return repo.findAll();
    }

    public Page<SeasonRate> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<SeasonRate> findAll(String season, Pageable pageable) {
        return season != null ? repo.findBySeason(season, pageable) : repo.findAll(pageable);
    }

    public Optional<SeasonRate> findById(Long id) {
        return repo.findById(id);
    }

    public SeasonRate save(SeasonRate seasonRate) {
        return repo.save(seasonRate);
    }

    public Optional<SeasonRate> update(Long id, SeasonRate updated) {
        return repo.findById(id).map(existing -> {
            existing.setRoomType(updated.getRoomType());
            existing.setSeason(updated.getSeason());
            existing.setPricePerNight(updated.getPricePerNight());
            existing.setValidFrom(updated.getValidFrom());
            existing.setValidTo(updated.getValidTo());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
