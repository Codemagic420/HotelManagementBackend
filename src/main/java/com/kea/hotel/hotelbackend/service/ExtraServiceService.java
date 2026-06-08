package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.ExtraService;
import com.kea.hotel.hotelbackend.repository.ExtraServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExtraServiceService {

    private final ExtraServiceRepository repo;

    public ExtraServiceService(ExtraServiceRepository repo) {
        this.repo = repo;
    }

    public List<ExtraService> findAll() {
        return repo.findAll();
    }

    public Page<ExtraService> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<ExtraService> findAll(Boolean active, Pageable pageable) {
        return active != null ? repo.findByActive(active, pageable) : repo.findAll(pageable);
    }

    public Optional<ExtraService> findById(Long id) {
        return repo.findById(id);
    }

    public ExtraService save(ExtraService service) {
        return repo.save(service);
    }

    public Optional<ExtraService> update(Long id, ExtraService updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setUnitPrice(updated.getUnitPrice());
            existing.setPriceUnit(updated.getPriceUnit());
            existing.setActive(updated.getActive());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
