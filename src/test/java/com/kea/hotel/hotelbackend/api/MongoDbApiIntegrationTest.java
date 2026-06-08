package com.kea.hotel.hotelbackend.api;

import com.kea.hotel.hotelbackend.mongodb.document.MongoReservationGuest;
import com.kea.hotel.hotelbackend.mongodb.document.MongoRoom;
import com.kea.hotel.hotelbackend.mongodb.document.MongoRoomCleaningAssignment;
import com.kea.hotel.hotelbackend.mongodb.service.*;
import com.kea.hotel.hotelbackend.service.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DisplayName("MongoDB API Integration Tests")
class MongoDbApiIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @MockitoBean private MongoRoomService mongoRoomService;
    @MockitoBean private MongoGuestService mongoGuestService;
    @MockitoBean private MongoRoomTypeService mongoRoomTypeService;
    @MockitoBean private MongoReservationGuestService mongoReservationGuestService;
    @MockitoBean private MongoRoomCleaningAssignmentService mongoRoomCleaningAssignmentService;
    @MockitoBean private MongoCleanerService mongoCleanerService;
    @MockitoBean private MongoExtraServiceService mongoExtraServiceService;
    @MockitoBean private MongoInventoryItemService mongoInventoryItemService;
    @MockitoBean private MongoSeasonRateService mongoSeasonRateService;
    @MockitoBean private MongoRoomCleaningTaskService mongoRoomCleaningTaskService;
    @MockitoBean private MongoBillService mongoBillService;
    @MockitoBean private MongoBillItemService mongoBillItemService;
    @MockitoBean private MongoReservationService mongoReservationService;
    @MockitoBean private AiService aiService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        when(mongoRoomService.findAll()).thenReturn(List.of());
        when(mongoRoomService.findById(any())).thenReturn(Optional.empty());
        when(mongoGuestService.findAll()).thenReturn(List.of());
        when(mongoRoomTypeService.findAll()).thenReturn(List.of());
        when(mongoReservationGuestService.findAll()).thenReturn(List.of());
        when(mongoRoomCleaningAssignmentService.findAll()).thenReturn(List.of());
    }

    // ── Rooms ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/rooms - Should return 200 when authenticated")
    void testGetMongoRooms_Returns200() throws Exception {
        mockMvc.perform(get("/api/mongodb/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/mongodb/rooms - Should return 401 without authentication")
    void testGetMongoRooms_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/mongodb/rooms"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/rooms/{id} - Should return 404 for unknown id")
    void testGetMongoRoom_NotFound() throws Exception {
        when(mongoRoomService.findById("unknown-id")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/mongodb/rooms/unknown-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("POST /api/mongodb/rooms - Should create room and return 200")
    void testCreateMongoRoom_Returns200() throws Exception {
        MongoRoom room = new MongoRoom();
        room.setId("room-1");
        when(mongoRoomService.save(any())).thenReturn(room);

        mockMvc.perform(post("/api/mongodb/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomNumber\":\"101\",\"roomStatus\":\"AVAILABLE\"}"))
                .andExpect(status().isOk());
    }

    // ── Guests ───────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/guests - Should return 200 when authenticated")
    void testGetMongoGuests_Returns200() throws Exception {
        mockMvc.perform(get("/api/mongodb/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/mongodb/guests - Should return 401 without authentication")
    void testGetMongoGuests_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/mongodb/guests"))
                .andExpect(status().isUnauthorized());
    }

    // ── Reservation Guests (fixed path: was /api/mongo/) ─────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/reservation-guests - Should return 200 (fixed from /api/mongo/)")
    void testGetMongoReservationGuests_CorrectPath_Returns200() throws Exception {
        mockMvc.perform(get("/api/mongodb/reservation-guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongo/reservation-guests - Old broken path should return 404")
    void testGetMongoReservationGuests_OldPath_Returns404() throws Exception {
        mockMvc.perform(get("/api/mongo/reservation-guests"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("POST /api/mongodb/reservation-guests - Should accept creation on correct path")
    void testCreateMongoReservationGuest_Returns200() throws Exception {
        MongoReservationGuest rg = new MongoReservationGuest();
        when(mongoReservationGuestService.create(any())).thenReturn(rg);

        mockMvc.perform(post("/api/mongodb/reservation-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    // ── Room Cleaning Assignments (fixed path: was /api/mongo/) ──────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/room-cleaning-assignments - Should return 200 (fixed from /api/mongo/)")
    void testGetMongoRoomCleaningAssignments_CorrectPath_Returns200() throws Exception {
        mockMvc.perform(get("/api/mongodb/room-cleaning-assignments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongo/room-cleaning-assignments - Old broken path should return 404")
    void testGetMongoRoomCleaningAssignments_OldPath_Returns404() throws Exception {
        mockMvc.perform(get("/api/mongo/room-cleaning-assignments"))
                .andExpect(status().isNotFound());
    }

    // ── Room Types ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mongodb/room-types - Should return 200 when authenticated")
    void testGetMongoRoomTypes_Returns200() throws Exception {
        mockMvc.perform(get("/api/mongodb/room-types"))
                .andExpect(status().isOk());
    }
}
