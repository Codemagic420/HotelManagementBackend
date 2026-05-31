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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("RoomTypeRepositoryTest")
class RoomTypeRepositoryTest {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Test
    @DisplayName("TC-RT1: Find all room types")
    void testFindAll() {
        List<RoomType> types = roomTypeRepository.findAll();
        assertThat(types).isNotNull().isNotEmpty();
    }

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
