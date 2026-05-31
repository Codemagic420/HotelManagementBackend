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

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Room API - Black-Box Tests")
class RoomAPITest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    // ========== EQUIVALENCE PARTITIONING - GET /api/rooms ==========
    
    @Test
    @DisplayName("TC-D1: Get all rooms - Success")
    void testGetRooms_Success() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Room IDs ==========
    
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

    @Test
    @DisplayName("TC-Invalid: Create room with empty payload")
    void testCreateRoom_EmptyPayload() throws Exception {
        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    @Test
    @DisplayName("GET /api/rooms - Response contains expected fields")
    void testGetRooms_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== PERFORMANCE CHECK ==========
    
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