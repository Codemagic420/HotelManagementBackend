package com.kea.hotel.hotelbackend.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Black-box tests of the Room API (/api/rooms). /api/rooms/** is public in SecurityConfig.
 * TYPE: BLACK-BOX API test. MockMvc sends fake HTTP requests to the real controllers (no real server/port).
 * Security filters are NOT applied here (MockMvc is built without springSecurity()), so no login is needed.
 * Uses the real H2 DB filled by DataInitializer, so tests rely on seeded data (e.g. ID 1 exists).
 * RISK: state-changing tests (PUT/DELETE) are not rolled back and share one DB, so test order can matter.
 * Techniques: equivalence partitioning, boundary values, decision table, negative tests.
 * BREAK IT: change an expected status code, a URL, or an ID; remove validation in RoomController.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Room API - Black-Box Tests")
class RoomAPITest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    // Builds a fresh MockMvc from the Spring context before each test.
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    // ========== EQUIVALENCE PARTITIONING - GET /api/rooms ==========
    
    // Checks: GET /api/rooms gives 200 with JSON content.
    // Fails if: wrong URL (404) or the content type is not JSON.
    @Test
    @DisplayName("TC-D1: Get all rooms - Success")
    void testGetRooms_Success() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Room IDs ==========
    
    // Checks: boundary IDs 99999, 0, -1 give 404.
    // Runs 3 times. Fails if: the controller returns 200/400/500 for them.
    @ParameterizedTest(name = "Invalid room ID: {0}")
    @CsvSource({
        "99999",
        "0",
        "-1"
    })
    @DisplayName("TC-D3: Get room with invalid ID - Should return 404")
    void testGetRoom_InvalidId_NotFound(long roomId) throws Exception {
        mockMvc.perform(get("/api/rooms/" + roomId))
                .andExpect(status().isNotFound());
    }

    // ========== DECISION TABLE - POST /api/rooms ==========
    
    // Decision table: room type 1 x status AVAILABLE/OCCUPIED/CLEANING - all valid (2xx).
    // Note: every run posts roomNumber '100'; would fail if room numbers must be unique.
    // Fails if: a status value is rejected.
    @ParameterizedTest(name = "DT-R1: Create room with type={0}, status={1}")
    @CsvSource({
        "1, AVAILABLE",
        "1, OCCUPIED",
        "1, CLEANING"
    })
    @DisplayName("DT-R1: Create room with valid combinations")
    void testCreateRoom_ValidCombinations(int typeId, String status) throws Exception {
        String payload = String.format("""
                {
                  "roomNumber": "100",
                  "roomType": {"roomTypeId": %d},
                  "roomStatus": "%s",
                  "cleanStatus": "CLEAN",
                  "occupied": false
                }
                """, typeId, status);

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
    // Checks: negative test - no roomType gives 400.
    // Fails if: the API accepts a room without a type.
    @Test
    @DisplayName("TC-Invalid: Create room with missing roomType")
    void testCreateRoom_MissingRoomType() throws Exception {
        String payload = """
                {
                  "roomNumber": "100",
                  "roomStatus": "AVAILABLE",
                  "cleanStatus": "CLEAN"
                }
                """;

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: negative test - '{}' gives 400.
    // Fails if: validation (@NotNull etc.) is removed from the Room model.
    @Test
    @DisplayName("TC-Invalid: Create room with empty payload")
    void testCreateRoom_EmptyPayload() throws Exception {
        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    // Checks: response body is a JSON list.
    // Note: name says 'expected fields' but it only checks it is a list.
    @Test
    @DisplayName("GET /api/rooms - Response contains expected fields")
    void testGetRooms_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== PERFORMANCE CHECK ==========
    
    // Checks: GET /api/rooms answers in under 2000 ms.
    // Fails if: limit is lowered or the query becomes slow.
    @Test
    @DisplayName("GET /api/rooms - Response time < 2 seconds")
    void testGetRooms_Performance() throws Exception {
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk());
        
        long duration = System.currentTimeMillis() - startTime;
        assert duration < 2000 : "Response too slow: " + duration + "ms";
    }
}