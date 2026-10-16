package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests of RoomService.
 * TYPE: pure UNIT test. No Spring, no DB: the repository is a Mockito @Mock, the service is @InjectMocks.
 * PATTERN: Arrange (when(...).thenReturn) -> Act (call service) -> Assert (AssertJ) + verify() calls on the mock.
 * MockitoExtension is strict: a stub that is never used fails the test (UnnecessaryStubbingException).
 * BREAK IT: change a stubbed return value or expected value, or make the service skip the repository call (verify fails).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService Unit Tests")
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room testRoom;
    private RoomType testRoomType;

    // Arrange: build a RoomType (Double, max 2) and a Room 101 (AVAILABLE, CLEAN) reused by every test.
    @BeforeEach
    void setUp() {
        testRoomType = new RoomType();
        testRoomType.setRoomTypeId(1L);
        testRoomType.setName("Double");
        testRoomType.setMaxOccupancy(2);

        testRoom = new Room();
        testRoom.setRoomId(1L);
        testRoom.setRoomNumber("101");
        testRoom.setRoomType(testRoomType);
        testRoom.setRoomStatus("AVAILABLE");
        testRoom.setCleanStatus("CLEAN");
        testRoom.setOccupied(false);
    }

    // Checks: returns both rooms and calls findAll() once.
    // Fails if: service doesn't use the repository or changes the list.
    @Test
    @DisplayName("Should retrieve all rooms")
    void testFindAll() {
        Room room2 = new Room();
        room2.setRoomId(2L);
        room2.setRoomNumber("102");

        when(roomRepository.findAll()).thenReturn(Arrays.asList(testRoom, room2));

        List<Room> result = roomService.findAll();

        assertThat(result).hasSize(2).contains(testRoom, room2);
        verify(roomRepository, times(1)).findAll();
    }

    // Checks: existing ID 1 returns the room (Optional present).
    // Fails if: service asks for a different ID.
    @Test
    @DisplayName("Should retrieve room by ID successfully")
    void testFindById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));

        Optional<Room> result = roomService.findById(1L);

        assertThat(result).isPresent().contains(testRoom);
        verify(roomRepository, times(1)).findById(1L);
    }

    // Checks: unknown ID 99 returns an empty Optional (no exception).
    // Fails if: service throws or returns null.
    @Test
    @DisplayName("Should return empty Optional when room not found")
    void testFindById_NotFound() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.findById(99L);

        assertThat(result).isEmpty();
        verify(roomRepository, times(1)).findById(99L);
    }

    // Checks: saved room keeps its number and status, and save() is called once.
    // Fails if: service doesn't call repository.save().
    @Test
    @DisplayName("Should save room successfully")
    void testSave() {
        when(roomRepository.save(testRoom)).thenReturn(testRoom);

        Room result = roomService.save(testRoom);

        assertThat(result)
                .isNotNull()
                .extracting("roomNumber", "roomStatus")
                .containsExactly("101", "AVAILABLE");
        verify(roomRepository, times(1)).save(testRoom);
    }

    // Checks: update(1, newData) finds the room, saves and returns the new status/clean status.
    // Note: mocked save() returns updatedRoom, so the result mostly reflects the stub.
    // Fails if: service doesn't call findById first.
    @Test
    @DisplayName("Should update room successfully")
    void testUpdate_Success() {
        Room updatedRoom = new Room();
        updatedRoom.setRoomId(1L);
        updatedRoom.setRoomNumber("101");
        updatedRoom.setRoomStatus("MAINTENANCE");
        updatedRoom.setCleanStatus("DIRTY");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);

        Optional<Room> result = roomService.update(1L, updatedRoom);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(room -> {
                    assertThat(room.getRoomStatus()).isEqualTo("MAINTENANCE");
                    assertThat(room.getCleanStatus()).isEqualTo("DIRTY");
                });
        verify(roomRepository, times(1)).findById(1L);
    }

    // Checks: updating an unknown ID returns empty and NEVER calls save() (verify never()).
    // Fails if: service saves anyway (would create a ghost room).
    @Test
    @DisplayName("Should handle update when room not found")
    void testUpdate_NotFound() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.update(99L, testRoom);

        assertThat(result).isEmpty();
        verify(roomRepository, never()).save(any());
    }

    // Checks: delete(1) calls repository.deleteById(1) exactly once.
    // Fails if: the service doesn't delete or uses another ID.
    @Test
    @DisplayName("Should delete room successfully")
    void testDelete() {
    // Arrange
    doNothing().when(roomRepository).deleteById(1L);

    // Act
    roomService.delete(1L);

    // Assert
    verify(roomRepository, times(1)).deleteById(1L);
    }

    // State test on the Room model: occupied false -> true -> false.
    // WEAK: no service involved; fails only if the getter/setter breaks.
    @Test
    @DisplayName("Should track room occupancy state changes")
    void testOccupancyStateChange() {
        assertThat(testRoom.getOccupied()).isFalse();

        testRoom.setOccupied(true);
        assertThat(testRoom.getOccupied()).isTrue();

        testRoom.setOccupied(false);
        assertThat(testRoom.getOccupied()).isFalse();
    }

    // WEAK: loops over valid statuses and checks the setter stores them.
    // Does not check that INVALID statuses are rejected.
    @Test
    @DisplayName("Should validate room status values")
    void testRoomStatusValidation() {
        String[] validStatuses = {"AVAILABLE", "OCCUPIED", "MAINTENANCE"};

        for (String status : validStatuses) {
            testRoom.setRoomStatus(status);
            assertThat(testRoom.getRoomStatus()).isEqualTo(status);
        }
    }

    // ========== PARAMETERIZED TESTS: Room Status Validation ==========
    // Parameterized test: one test body, run once per status value below.

    // Parameterized: same test runs once per status (AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING).
    // WEAK: save() is mocked to return the same room, so an invalid status would not be caught.
    @ParameterizedTest
    @ValueSource(strings = {"AVAILABLE", "OCCUPIED", "MAINTENANCE", "CLEANING"})
    @DisplayName("Should accept valid room status values")
    void testValidRoomStatuses(String status) {
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        testRoom.setRoomStatus(status);
        Room result = roomService.save(testRoom);

        assertThat(result.getRoomStatus()).isIn("AVAILABLE", "OCCUPIED", "MAINTENANCE", "CLEANING");
    }
}
