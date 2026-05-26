package com.kea.hotel.hotelbackend.integration;

import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.repository.RoomRepository;
import com.kea.hotel.hotelbackend.repository.RoomTypeRepository;
import com.kea.hotel.hotelbackend.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("RoomService Integration Tests")
class RoomServiceIntegrationTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    private Room testRoom;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        // Clean up
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();

        // Create room type
        roomType = new RoomType();
        roomType.setName("Double");
        roomType.setMaxOccupancy(2);
        roomTypeRepository.save(roomType);

        // Create test room
        testRoom = new Room();
        testRoom.setRoomNumber("101");
        testRoom.setType("Double");
        testRoom.setRoomStatus("AVAILABLE");
        testRoom.setCleanStatus("CLEAN");
        testRoom.setOccupied(false);
        testRoom.setRoomType(roomType);
    }

    @Test
    @DisplayName("Should create and persist room to database")
    void testCreateRoom() {
        // ACT: Save room through service
        Room saved = roomService.save(testRoom);

        // ASSERT: Room is persisted with generated ID
        assertThat(saved).isNotNull();
        assertThat(saved.getRoomId()).isNotNull();
        assertThat(saved.getRoomNumber()).isEqualTo("101");

        // Verify in database
        Optional<Room> retrieved = roomRepository.findById(saved.getRoomId());
        assertThat(retrieved)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getRoomStatus()).isEqualTo("AVAILABLE"));
    }

    @Test
    @DisplayName("Should retrieve room by ID from database")
    void testGetRoomById() {
        // ARRANGE: Create and save room
        Room saved = roomService.save(testRoom);

        // ACT: Retrieve room by ID
        Optional<Room> result = roomService.findById(saved.getRoomId());

        // ASSERT: Room retrieved with correct data
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> {
                    assertThat(room.getRoomId()).isEqualTo(saved.getRoomId());
                    assertThat(room.getRoomNumber()).isEqualTo("101");
                    assertThat(room.getType()).isEqualTo("Double");
                    assertThat(room.getRoomStatus()).isEqualTo("AVAILABLE");
                });
    }

    @Test
    @DisplayName("Should find all rooms with pagination")
    void testFindAllWithPagination() {
        // ARRANGE: Create multiple rooms
        Room room2 = new Room();
        room2.setRoomNumber("102");
        room2.setType("Double");
        room2.setRoomStatus("AVAILABLE");
        room2.setCleanStatus("CLEAN");
        room2.setOccupied(false);
        room2.setRoomType(roomType);

        Room room3 = new Room();
        room3.setRoomNumber("103");
        room3.setType("Single");
        room3.setRoomStatus("OCCUPIED");
        room3.setCleanStatus("DIRTY");
        room3.setOccupied(true);
        room3.setRoomType(roomType);

        roomService.save(testRoom);
        roomService.save(room2);
        roomService.save(room3);

        // ACT: Find all with pagination
        Pageable pageable = PageRequest.of(0, 10);
        Page<Room> result = roomService.findAll(pageable);

        // ASSERT: All rooms retrieved
        assertThat(result.getContent())
                .hasSize(3)
                .extracting("roomNumber")
                .contains("101", "102", "103");
    }

    @Test
    @DisplayName("Should update room status")
    void testUpdateRoomStatus() {
        // ARRANGE: Create and save room
        Room saved = roomService.save(testRoom);

        // ACT: Update room status
        Room updated = new Room();
        updated.setRoomNumber("101");
        updated.setType("Double");
        updated.setRoomStatus("OCCUPIED");
        updated.setCleanStatus("DIRTY");
        updated.setOccupied(true);
        updated.setRoomType(roomType);

        Optional<Room> result = roomService.update(saved.getRoomId(), updated);

        // ASSERT: Room updated in database
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> {
                    assertThat(room.getRoomStatus()).isEqualTo("OCCUPIED");
                    assertThat(room.getCleanStatus()).isEqualTo("DIRTY");
                    assertThat(room.getOccupied()).isTrue();
                });

        // Verify in database
        Optional<Room> dbRoom = roomRepository.findById(saved.getRoomId());
        assertThat(dbRoom)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getRoomStatus()).isEqualTo("OCCUPIED"));
    }

    @Test
    @DisplayName("Should delete room from database")
    void testDeleteRoom() {
        // ARRANGE: Create and save room
        Room saved = roomService.save(testRoom);
        Long roomId = saved.getRoomId();

        // Verify room exists
        assertThat(roomRepository.findById(roomId)).isPresent();

        // ACT: Delete room
        roomService.delete(roomId);

        // ASSERT: Room is deleted from database
        assertThat(roomRepository.findById(roomId)).isEmpty();
    }

    @Test
    @DisplayName("Should enforce unique room number constraint")
    void testUniqueRoomNumberConstraint() {
        // ARRANGE: Create and save first room
        roomService.save(testRoom);

        // ACT & ASSERT: Attempt to save room with same number throws exception
        Room duplicateNumber = new Room();
        duplicateNumber.setRoomNumber("101"); // Same number
        duplicateNumber.setType("Single");
        duplicateNumber.setRoomStatus("AVAILABLE");
        duplicateNumber.setCleanStatus("CLEAN");
        duplicateNumber.setOccupied(false);
        duplicateNumber.setRoomType(roomType);

        assertThatThrownBy(() -> roomService.save(duplicateNumber))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should update room with AI assessment")
    void testAIAssessmentEnrichment() {
        // ARRANGE: Create and save room
        Room saved = roomService.save(testRoom);

        // ACT: Add AI assessment
        String assessment = "Room condition: Excellent. AC working properly. New bedding needed.";
        Optional<Room> result = roomService.updateAIAssessment(saved.getRoomId(), assessment);

        // ASSERT: AI assessment is saved
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> {
                    assertThat(room.getAiAssessmentSummary()).isEqualTo(assessment);
                    assertThat(room.getAiFieldsUpdatedAt()).isNotNull();
                });

        // Verify in database
        Optional<Room> dbRoom = roomRepository.findById(saved.getRoomId());
        assertThat(dbRoom)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getAiAssessmentSummary()).isEqualTo(assessment));
    }

    @Test
    @DisplayName("Should track room occupancy status")
    void testRoomOccupancyTracking() {
        // ARRANGE: Create room as available
        testRoom.setOccupied(false);
        Room saved = roomService.save(testRoom);

        // ACT: Mark as occupied
        Room updated = new Room();
        updated.setRoomNumber("101");
        updated.setType("Double");
        updated.setRoomStatus("OCCUPIED");
        updated.setCleanStatus("DIRTY");
        updated.setOccupied(true);
        updated.setRoomType(roomType);

        roomService.update(saved.getRoomId(), updated);

        // ASSERT: Occupancy status updated
        Optional<Room> result = roomRepository.findById(saved.getRoomId());
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> assertThat(room.getOccupied()).isTrue());
    }

    @Test
    @DisplayName("Should manage clean status transitions")
    void testCleanStatusTransitions() {
        // ARRANGE: Create dirty room
        testRoom.setCleanStatus("DIRTY");
        Room saved = roomService.save(testRoom);

        // ACT: Update to clean
        Room updated = new Room();
        updated.setRoomNumber("101");
        updated.setType("Double");
        updated.setRoomStatus("AVAILABLE");
        updated.setCleanStatus("CLEAN");
        updated.setOccupied(false);
        updated.setRoomType(roomType);

        roomService.update(saved.getRoomId(), updated);

        // ASSERT: Clean status updated in database
        Optional<Room> result = roomRepository.findById(saved.getRoomId());
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> assertThat(room.getCleanStatus()).isEqualTo("CLEAN"));
    }

    @Test
    @DisplayName("Should handle pagination with multiple pages")
    void testPaginationMultiplePages() {
        // ARRANGE: Create 15 rooms
        for (int i = 1; i <= 15; i++) {
            Room room = new Room();
            room.setRoomNumber(String.format("%03d", i));
            room.setType("Double");
            room.setRoomStatus("AVAILABLE");
            room.setCleanStatus("CLEAN");
            room.setOccupied(false);
            room.setRoomType(roomType);
            roomService.save(room);
        }

        // ACT: Get pages with size 5
        Page<Room> page1 = roomService.findAll(PageRequest.of(0, 5));
        Page<Room> page2 = roomService.findAll(PageRequest.of(1, 5));
        Page<Room> page3 = roomService.findAll(PageRequest.of(2, 5));

        // ASSERT: All pages retrieved correctly
        assertThat(page1.getContent()).hasSize(5);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page3.getContent()).hasSize(5);
        assertThat(page1.getTotalElements()).isEqualTo(15);
        assertThat(page1.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should preserve room data integrity across updates")
    void testDataIntegrity() {
        // ARRANGE: Create two rooms
        testRoom.setRoomNumber("101");
        Room room2 = new Room();
        room2.setRoomNumber("102");
        room2.setType("Single");
        room2.setRoomStatus("AVAILABLE");
        room2.setCleanStatus("CLEAN");
        room2.setOccupied(false);
        room2.setRoomType(roomType);

        Room saved1 = roomService.save(testRoom);
        Room saved2 = roomService.save(room2);

        // ACT: Update only room1
        Room updated = new Room();
        updated.setRoomNumber("101");
        updated.setType("Double");
        updated.setRoomStatus("OCCUPIED");
        updated.setCleanStatus("DIRTY");
        updated.setOccupied(true);
        updated.setRoomType(roomType);

        roomService.update(saved1.getRoomId(), updated);

        // ASSERT: Room2 is unchanged
        Optional<Room> room1Result = roomRepository.findById(saved1.getRoomId());
        Optional<Room> room2Result = roomRepository.findById(saved2.getRoomId());

        assertThat(room1Result)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getRoomStatus()).isEqualTo("OCCUPIED"));

        assertThat(room2Result)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getRoomStatus()).isEqualTo("AVAILABLE"));
    }
}
