package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.repository.RoomTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {

    private final RoomTypeRepository repo;

    public RoomTypeService(RoomTypeRepository repo) {
        this.repo = repo;
    }

    public List<RoomType> findAll() {
        return repo.findAll();
    }

    public Page<RoomType> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<RoomType> findById(Long id) {
        return repo.findById(id);
    }

    public RoomType save(RoomType roomType) {
        return repo.save(roomType);
    }

    public Optional<RoomType> update(Long id, RoomType updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setMaxOccupancy(updated.getMaxOccupancy());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
