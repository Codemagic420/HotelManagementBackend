package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("E2E: Full Stack API Integration Tests")
class FullStackE2EIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        // Login and get token
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract token from response (adjust based on your response format)
        authToken = loginResponse.contains("\"token\"") ?
                loginResponse.split("\"token\":\"")[1].split("\"")[0] :
                loginResponse.split("\"accessToken\":\"")[1].split("\"")[0];
    }

    @Test
    @DisplayName("E2E: User Authentication Flow")
    void testAuthenticationFlow() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("E2E: Fetch Rooms (Public Endpoint)")
    void testFetchRooms() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].roomNumber").exists())
                .andExpect(jsonPath("$[0].roomStatus").exists());
    }

    @Test
    @DisplayName("E2E: Fetch Guests (Authenticated Endpoint)")
    void testFetchGuests() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    @DisplayName("E2E: Fetch Reservations")
    void testFetchReservations() throws Exception {
        mockMvc.perform(get("/api/reservations")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].referenceNo").exists());
    }

    @Test
    @DisplayName("E2E: Get Room Types")
    void testGetRoomTypes() throws Exception {
        mockMvc.perform(get("/api/room-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].maxOccupancy").isNumber());
    }

    @Test
    @DisplayName("E2E: Unauthorized Access Denied")
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("E2E: Invalid Token Rejected")
    void testInvalidTokenRejected() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("E2E: API Error Handling")
    void testApiErrorHandling() throws Exception {
        mockMvc.perform(get("/api/guests/999999")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
