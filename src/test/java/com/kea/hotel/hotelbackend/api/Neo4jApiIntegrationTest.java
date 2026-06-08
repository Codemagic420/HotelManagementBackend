package com.kea.hotel.hotelbackend.api;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jRoom;
import com.kea.hotel.hotelbackend.neo4j.service.*;
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
@DisplayName("Neo4j API Integration Tests")
class Neo4jApiIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    // Neo4j services
    @MockitoBean private Neo4jRoomService neo4jRoomService;
    @MockitoBean private Neo4jGuestService neo4jGuestService;
    @MockitoBean private Neo4jReservationService neo4jReservationService;
    @MockitoBean private Neo4jRoomTypeService neo4jRoomTypeService;
    @MockitoBean private Neo4jCleanerService neo4jCleanerService;
    @MockitoBean private Neo4jExtraServiceService neo4jExtraServiceService;
    @MockitoBean private Neo4jInventoryItemService neo4jInventoryItemService;
    @MockitoBean private Neo4jSeasonRateService neo4jSeasonRateService;
    @MockitoBean private Neo4jBillService neo4jBillService;
    @MockitoBean private Neo4jBillItemService neo4jBillItemService;
    @MockitoBean private Neo4jRoomCleaningTaskService neo4jRoomCleaningTaskService;
    @MockitoBean private Neo4jReservationGuestService neo4jReservationGuestService;
    @MockitoBean private Neo4jRoomCleaningAssignmentService neo4jRoomCleaningAssignmentService;

    // MongoDB services (needed by context)
    @MockitoBean private MongoRoomService mongoRoomService;
    @MockitoBean private MongoGuestService mongoGuestService;
    @MockitoBean private MongoRoomTypeService mongoRoomTypeService;
    @MockitoBean private MongoCleanerService mongoCleanerService;
    @MockitoBean private MongoExtraServiceService mongoExtraServiceService;
    @MockitoBean private MongoInventoryItemService mongoInventoryItemService;
    @MockitoBean private MongoSeasonRateService mongoSeasonRateService;
    @MockitoBean private MongoRoomCleaningTaskService mongoRoomCleaningTaskService;
    @MockitoBean private MongoReservationGuestService mongoReservationGuestService;
    @MockitoBean private MongoRoomCleaningAssignmentService mongoRoomCleaningAssignmentService;
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
        when(neo4jRoomService.findAll()).thenReturn(List.of());
        when(neo4jRoomService.findById(any())).thenReturn(Optional.empty());
        when(neo4jGuestService.findAll()).thenReturn(List.of());
        when(neo4jReservationService.findAll()).thenReturn(List.of());
        when(neo4jRoomTypeService.findAll()).thenReturn(List.of());
    }

    // ── Rooms ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/neo4j/rooms - Should return 200 when authenticated")
    void testGetNeo4jRooms_Returns200() throws Exception {
        mockMvc.perform(get("/api/neo4j/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/neo4j/rooms - Should return 401 without authentication")
    void testGetNeo4jRooms_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/neo4j/rooms"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/neo4j/rooms/{id} - Should return 404 for unknown id")
    void testGetNeo4jRoom_NotFound() throws Exception {
        when(neo4jRoomService.findById(99999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/neo4j/rooms/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("POST /api/neo4j/rooms - Should create room and return 200")
    void testCreateNeo4jRoom_Returns200() throws Exception {
        Neo4jRoom room = new Neo4jRoom();
        when(neo4jRoomService.save(any())).thenReturn(room);

        mockMvc.perform(post("/api/neo4j/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomNumber\":\"101\",\"roomStatus\":\"AVAILABLE\"}"))
                .andExpect(status().isOk());
    }

    // ── Guests ───────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/neo4j/guests - Should return 200 when authenticated")
    void testGetNeo4jGuests_Returns200() throws Exception {
        mockMvc.perform(get("/api/neo4j/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/neo4j/guests - Should return 401 without authentication")
    void testGetNeo4jGuests_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/neo4j/guests"))
                .andExpect(status().isUnauthorized());
    }

    // ── Reservations ─────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/neo4j/reservations - Should return 200 when authenticated")
    void testGetNeo4jReservations_Returns200() throws Exception {
        mockMvc.perform(get("/api/neo4j/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/neo4j/reservations - Should return 401 without authentication")
    void testGetNeo4jReservations_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/neo4j/reservations"))
                .andExpect(status().isUnauthorized());
    }

    // ── Room Types ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/neo4j/room-types - Should return 200 when authenticated")
    void testGetNeo4jRoomTypes_Returns200() throws Exception {
        mockMvc.perform(get("/api/neo4j/room-types"))
                .andExpect(status().isOk());
    }
}
