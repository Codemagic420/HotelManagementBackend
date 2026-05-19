package com.kea.hotel.hotelbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("RoomController Integration Tests")
class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    private Room testRoom;
    private RoomType testRoomType;

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

    @Test
    @DisplayName("GET /api/rooms - Should return all rooms")
    void testGetAllRooms() throws Exception {
        when(roomService.findAll()).thenReturn(Arrays.asList(testRoom));

        ResultActions result = mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].roomNumber", equalTo("101")))
                .andExpect(jsonPath("$[0].roomStatus", equalTo("AVAILABLE")));

        verify(roomService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/rooms/{id} - Should return room by id")
    void testGetRoomById() throws Exception {
        when(roomService.findById(1L)).thenReturn(Optional.of(testRoom));

        mockMvc.perform(get("/api/rooms/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber", equalTo("101")))
                .andExpect(jsonPath("$.roomStatus", equalTo("AVAILABLE")));

        verify(roomService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/rooms/{id} - Should return 404 when room not found")
    void testGetRoomByIdNotFound() throws Exception {
        when(roomService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(roomService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /api/rooms - Should create new room")
    void testCreateRoom() throws Exception {
        Room newRoom = new Room();
        newRoom.setRoomId(2L);
        newRoom.setRoomNumber("102");
        newRoom.setRoomStatus("AVAILABLE");
        newRoom.setCleanStatus("CLEAN");

        when(roomService.save(any(Room.class))).thenReturn(newRoom);

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoom)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomNumber", equalTo("102")));

        verify(roomService, times(1)).save(any(Room.class));
    }

    @Test
    @DisplayName("PUT /api/rooms/{id} - Should update room")
    void testUpdateRoom() throws Exception {
        Room updatedRoom = new Room();
        updatedRoom.setRoomNumber("101");
        updatedRoom.setRoomStatus("MAINTENANCE");

        when(roomService.update(eq(1L), any(Room.class))).thenReturn(Optional.of(updatedRoom));

        mockMvc.perform(put("/api/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRoom)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomStatus", equalTo("MAINTENANCE")));

        verify(roomService, times(1)).update(eq(1L), any(Room.class));
    }

    @Test
    @DisplayName("DELETE /api/rooms/{id} - Should delete room")
    void testDeleteRoom() throws Exception {
        doNothing().when(roomService).delete(1L);

        mockMvc.perform(delete("/api/rooms/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(roomService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("GET /api/rooms - Should be accessible without authentication")
    void testPublicAccessToRooms() throws Exception {
        when(roomService.findAll()).thenReturn(Arrays.asList(testRoom));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/rooms - Response should contain created room")
    void testCreateRoomResponseStructure() throws Exception {
        Room newRoom = new Room();
        newRoom.setRoomId(3L);
        newRoom.setRoomNumber("103");
        newRoom.setRoomStatus("AVAILABLE");

        when(roomService.save(any(Room.class))).thenReturn(newRoom);

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId", notNullValue()))
                .andExpect(jsonPath("$.roomNumber", notNullValue()))
                .andExpect(jsonPath("$.roomStatus", notNullValue()));
    }
}
