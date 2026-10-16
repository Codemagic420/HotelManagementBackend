package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CRUD tests for RoomTypeRepository (Single, Double, Suite).
 * TYPE: integration test (real repository + in-memory H2 DB, no mocks).
 * HOW: @SpringBootTest loads the app; @Transactional rolls back after every test, so created rows never leak.
 * Seed data (cleaners, rooms, ...) comes from DataInitializer, which runs at startup.
 * BREAK IT: change an expected value, or seed a type with maxOccupancy 0 (TC-RT6 fails).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("RoomTypeRepositoryTest")
class RoomTypeRepositoryTest {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // Checks: seeded room types exist.
    // Fails if: table is empty.
    @Test
    @DisplayName("TC-RT1: Find all room types")
    void testFindAll() {
        List<RoomType> types = roomTypeRepository.findAll();
        assertThat(types).isNotNull().isNotEmpty();
    }

    // Checks: CREATE - saved type gets an ID and keeps its name.
    // Fails if: expected name differs, or save() throws.
    @Test
    @DisplayName("TC-RT2: Save new room type")
    void testSaveNewType() {
        RoomType roomType = new RoomType();
        roomType.setName("Unique Penthouse");
        roomType.setMaxOccupancy(4);

        RoomType saved = roomTypeRepository.save(roomType);

        assertThat(saved).isNotNull();
        assertThat(saved.getRoomTypeId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Unique Penthouse");
    }

    // Checks: READ by ID.
    // Note: returns early (passes silently) if the table is empty.
    @Test
    @DisplayName("TC-RT3: Find room type by ID")
    void testFindById() {
        List<RoomType> types = roomTypeRepository.findAll();
        if (types.isEmpty()) return;
        Long typeId = types.get(0).getRoomTypeId();

        Optional<RoomType> found = roomTypeRepository.findById(typeId);

        assertThat(found).isPresent();
        assertThat(found.get().getRoomTypeId()).isEqualTo(typeId);
    }

    // Checks: UPDATE - max occupancy changes from 1 to 2.
    // Fails if: expected occupancy is changed.
    @Test
    @DisplayName("TC-RT4: Update room type")
    void testUpdateType() {
        RoomType roomType = new RoomType();
        roomType.setName("Studio");
        roomType.setMaxOccupancy(1);
        roomType = roomTypeRepository.save(roomType);

        roomType.setMaxOccupancy(2);
        RoomType updated = roomTypeRepository.save(roomType);

        assertThat(updated.getMaxOccupancy()).isEqualTo(2);
    }

    // Checks: DELETE - type is gone after deleteById.
    // Fails if: the deleteById line is removed.
    @Test
    @DisplayName("TC-RT5: Delete room type")
    void testDeleteType() {
        RoomType roomType = new RoomType();
        roomType.setName("Temporary Room Type");
        roomType.setMaxOccupancy(3);
        roomType = roomTypeRepository.save(roomType);
        Long typeId = roomType.getRoomTypeId();

        roomTypeRepository.deleteById(typeId);

        Optional<RoomType> deleted = roomTypeRepository.findById(typeId);
        assertThat(deleted).isEmpty();
    }

    // Checks: every type has maxOccupancy > 0 and a non-blank name.
    // Fails if: a seeded type has occupancy 0 or a blank name.
    @Test
    @DisplayName("TC-RT6: Room type has valid occupancy")
    void testValidOccupancy() {
        List<RoomType> types = roomTypeRepository.findAll();

        for (RoomType type : types) {
            assertThat(type.getMaxOccupancy()).isGreaterThan(0);
            assertThat(type.getName()).isNotBlank();
        }
    }
}
