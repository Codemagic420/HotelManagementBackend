package com.kea.hotel.hotelbackend.api;

import com.kea.hotel.hotelbackend.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@DisplayName("MySQL Room API Integration Tests")
class RoomAPITest {

    @Autowired
    private WebApplicationContext wac;

    @MockitoBean
    private RoomService roomService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        when(roomService.findAll(any(), any())).thenReturn(Page.empty());
        when(roomService.findById(any())).thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mysql/rooms - Should return 200 when authenticated")
    void testGetRooms_WithAuth_Returns200() throws Exception {
        mockMvc.perform(get("/api/mysql/rooms"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/mysql/rooms - Should return 401 when not authenticated")
    void testGetRooms_WithoutAuth_Returns401() throws Exception {
        mockMvc.perform(get("/api/mysql/rooms"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mysql/rooms - Should return JSON content type")
    void testGetRooms_ContentType() throws Exception {
        mockMvc.perform(get("/api/mysql/rooms"))
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mysql/rooms/{id} - Should return 404 for non-existent room")
    void testGetRoom_NotFound() throws Exception {
        when(roomService.findById(eq(99999L))).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/mysql/rooms/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mysql/rooms?roomStatus=AVAILABLE - Should apply filter and return 200")
    void testGetRooms_WithFilter_Returns200() throws Exception {
        mockMvc.perform(get("/api/mysql/rooms").param("roomStatus", "AVAILABLE"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STAFF")
    @DisplayName("GET /api/mysql/rooms?page=0&size=10 - Should support pagination")
    void testGetRooms_Pagination_Returns200() throws Exception {
        mockMvc.perform(get("/api/mysql/rooms").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());
    }
}
