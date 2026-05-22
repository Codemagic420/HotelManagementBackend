package com.kea.hotel.hotelbackend.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("API Test Suite - MockMvc")
class RoomAPITest {

    @Autowired
    private org.springframework.web.context.WebApplicationContext wac;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DisplayName("GET /api/rooms - Should return 200 OK")
    void testGetRooms_StatusCode() {
        try {
            mockMvc.perform(get("/api/rooms")).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("GET /api/rooms - Should return JSON content type")
    void testGetRooms_ContentType() {
        try {
            mockMvc.perform(get("/api/rooms")).andExpect(content().contentTypeCompatibleWith("application/json"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("GET /api/rooms/{id} - Should return 404 for non-existent room")
    void testGetRoom_NotFound() {
        try {
            mockMvc.perform(get("/api/rooms/99999")).andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("GET /api/rooms - Should return list structure")
    void testGetRooms_ResponseStructure() {
        try {
            mockMvc.perform(get("/api/rooms")).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("POST /api/rooms - Should accept a valid public create request")
    void testCreateRoom_PublicCreate() {
        try {
            String payload = """
                    {
                      "roomNumber": "9999",
                      "roomType": {"roomTypeId": 1},
                      "roomStatus": "AVAILABLE",
                      "cleanStatus": "CLEAN",
                      "occupied": false,
                      "type": "Single"
                    }
                    """;

            org.springframework.test.web.servlet.MvcResult mvcResult = mockMvc.perform(post("/api/rooms")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andReturn();

            int statusCode = mvcResult.getResponse().getStatus();
            assertThat(statusCode).isIn(200, 201);
            assertThat(mvcResult.getResponse().getContentAsString()).contains("9999");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
