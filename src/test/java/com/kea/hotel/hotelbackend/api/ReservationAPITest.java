package com.kea.hotel.hotelbackend.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("Reservation API - Black-Box Tests")
class ReservationAPITest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        }

    // ========== EQUIVALENCE PARTITIONING - GET /api/reservations ==========
    
    @Test
    @DisplayName("TC-R1: Get all reservations - Success")
    void testGetReservations_Success() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Reservation IDs ==========
    
    @ParameterizedTest(name = "Invalid reservation ID: {0}")
    @CsvSource({
        "99999",
        "0",
        "-1"
    })
    @DisplayName("TC-R2: Get reservation with invalid ID - Should return 404")
    void testGetReservation_InvalidId_NotFound(long reservationId) throws Exception {
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TC-R3: Get reservation with valid ID - Success")
    void testGetReservation_ValidId_Success() throws Exception {
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").exists())
                .andExpect(jsonPath("$.checkInDate").exists());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Date Ranges ==========
    
    @ParameterizedTest(name = "Invalid date combo: checkIn={0}, checkOut={1}")
    @CsvSource({
        "2026-06-20, 2026-06-15",  // checkOut before checkIn
        "2026-01-01, 2026-01-01",  // same day
        "2020-01-01, 2026-06-15"   // past date
    })
    @DisplayName("TC-R4: Create reservation with invalid dates")
    void testCreateReservation_InvalidDates(String checkIn, String checkOut) throws Exception {
        String payload = String.format("""
                {
                  "guestId": 1,
                  "roomTypeId": 1,
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "numberOfGuests": 2
                }
                """, checkIn, checkOut);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // ========== DECISION TABLE - POST /api/reservations ==========
    
    @ParameterizedTest(name = "DT-R1: Create reservation with guests={0}, roomType={1}")
    @CsvSource({
        "1, 1",
        "2, 2",
        "4, 3"
    })
    @DisplayName("DT-R1: Create reservation with valid combinations")
    void testCreateReservation_ValidCombinations(int guests, int roomType) throws Exception {
        String payload = String.format("""
                {
                  "guestId": 1,
                  "roomTypeId": %d,
                  "checkInDate": "2026-06-15",
                  "checkOutDate": "2026-06-20",
                  "numberOfGuests": %d,
                  "specialRequests": "Late checkout"
                }
                """, roomType, guests);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
    @Test
    @DisplayName("TC-Invalid: Create reservation with missing guestId")
    void testCreateReservation_MissingGuestId() throws Exception {
        String payload = """
                {
                  "roomTypeId": 1,
                  "checkInDate": "2026-06-15",
                  "checkOutDate": "2026-06-20",
                  "numberOfGuests": 2
                }
                """;

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-Invalid: Create reservation with zero guests")
    void testCreateReservation_ZeroGuests() throws Exception {
        String payload = """
                {
                  "guestId": 1,
                  "roomTypeId": 1,
                  "checkInDate": "2026-06-15",
                  "checkOutDate": "2026-06-20",
                  "numberOfGuests": 0
                }
                """;

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    @Test
    @DisplayName("GET /api/reservations - Response contains expected fields")
    void testGetReservations_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").exists())
                .andExpect(jsonPath("$[0].checkInDate").exists())
                .andExpect(jsonPath("$[0].checkOutDate").exists());
    }

    // ========== PUT - UPDATE RESERVATION ==========
    
    @Test
    @DisplayName("TC-R5: Update reservation - Success")
    void testUpdateReservation_Success() throws Exception {
        String payload = """
                {
                  "checkInDate": "2026-06-16",
                  "checkOutDate": "2026-06-21",
                  "numberOfGuests": 3,
                  "specialRequests": "Early checkout"
                }
                """;

        mockMvc.perform(put("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    // ========== DELETE - CANCEL RESERVATION ==========
    
    @Test
    @DisplayName("TC-R6: Cancel reservation - Success")
    void testCancelReservation_Success() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isOk());
    }

    // ========== STATE TRANSITION - Check-In/Check-Out ==========
    
    @Test
    @DisplayName("TC-R7: Check-in reservation - Success")
    void testCheckInReservation_Success() throws Exception {
        mockMvc.perform(put("/api/reservations/1/check-in"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TC-R8: Check-out reservation - Success")
    void testCheckOutReservation_Success() throws Exception {
        mockMvc.perform(put("/api/reservations/1/check-out"))
                .andExpect(status().isOk());
    }

    // ========== PERFORMANCE CHECK ==========
    
    @Test
    @DisplayName("GET /api/reservations - Response time < 2 seconds")
    void testGetReservations_Performance() throws Exception {
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());
        
        long duration = System.currentTimeMillis() - startTime;
        assert duration < 2000 : "Response too slow: " + duration + "ms";
    }
}