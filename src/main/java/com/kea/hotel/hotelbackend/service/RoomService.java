package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repo;

    public RoomService(RoomRepository repo) {
        this.repo = repo;
    }

    public List<Room> findAll() {
        return repo.findAll();
    }

    public Page<Room> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Room> findAll(String roomStatus, Pageable pageable) {
        return roomStatus != null ? repo.findByRoomStatus(roomStatus, pageable) : repo.findAll(pageable);
    }

    public Optional<Room> findById(Long id) {
        return repo.findById(id);
    }

    public Room save(Room room) {
        return repo.save(room);
    }

    public Optional<Room> update(Long id, Room updated) {
        return repo.findById(id).map(existing -> {
            existing.setRoomNumber(updated.getRoomNumber());
            existing.setType(updated.getType());
            existing.setOccupied(updated.getOccupied());
            return repo.save(existing);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
