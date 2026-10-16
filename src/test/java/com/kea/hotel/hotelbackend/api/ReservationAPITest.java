package com.kea.hotel.hotelbackend.api;

import java.time.LocalDate;

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

/**
 * Black-box tests of the Reservation API (/api/reservations).
 * TYPE: BLACK-BOX API test. MockMvc sends fake HTTP requests to the real controllers (no real server/port).
 * Security filters are NOT applied here (MockMvc is built without springSecurity()), so no login is needed.
 * Uses the real H2 DB filled by DataInitializer, so tests rely on seeded data (e.g. ID 1 exists).
 * RISK: state-changing tests (PUT/DELETE) are not rolled back and share one DB, so test order can matter.
 * Techniques: equivalence partitioning, boundary values (dates), decision table, state transition.
 * BREAK IT: change an expected status code, a date, or an ID; remove date validation in the service/controller.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Reservation API - Black-Box Tests")
class ReservationAPITest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        // Builds a fresh MockMvc from the Spring context before each test.
        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        }

    // ========== EQUIVALENCE PARTITIONING - GET /api/reservations ==========
    
    // Checks: GET /api/reservations gives 200, JSON, body is a list.
    // Fails if: wrong URL or the endpoint returns an object.
    @Test
    @DisplayName("TC-R1: Get all reservations - Success")
    void testGetReservations_Success() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Reservation IDs ==========
    
    // Checks: boundary IDs 99999, 0, -1 give 404.
    // Runs 3 times. Fails if: another status is returned.
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

    // Checks: reservation 1 exists and has reservationId and checkInDate.
    // Fails if: reservation 1 is missing (e.g. deleted by TC-R6) or a field is renamed.
    @Test
    @DisplayName("TC-R3: Get reservation with valid ID - Success")
    void testGetReservation_ValidId_Success() throws Exception {
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").exists())
                .andExpect(jsonPath("$.checkInDate").exists());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Date Ranges ==========
    
    // Checks: bad date pairs give 400: check-out before check-in, same day, check-in in the past.
    // Runs 3 times. Fails if: the date rules are missing.
    // Dates are day offsets from today so the cases stay valid/invalid as time passes.
    @ParameterizedTest(name = "Invalid date combo: checkIn=today+{0}, checkOut=today+{1}")
    @CsvSource({
        "35, 30",   // checkOut before checkIn
        "30, 30",   // same day
        "-30, 35"   // checkIn in the past
    })
    @DisplayName("TC-R4: Create reservation with invalid dates")
    void testCreateReservation_InvalidDates(int checkInOffset, int checkOutOffset) throws Exception {
        LocalDate checkIn = LocalDate.now().plusDays(checkInOffset);
        LocalDate checkOut = LocalDate.now().plusDays(checkOutOffset);
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
    
    // Decision table: guests x room type (1/1, 2/2, 4/3) with valid dates - all 2xx.
    // Fails if: a combination is rejected (e.g. too many guests for the room type).
    // Dates are relative to today so they never become 'past'.
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
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "numberOfGuests": %d,
                  "specialRequests": "Late checkout"
                }
                """, roomType, LocalDate.now().plusDays(30), LocalDate.now().plusDays(35), guests);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
    // Checks: negative test - no guestId gives 400.
    // Fails if: a reservation without a guest is accepted.
    @Test
    @DisplayName("TC-Invalid: Create reservation with missing guestId")
    void testCreateReservation_MissingGuestId() throws Exception {
        String payload = """
                {
                  "roomTypeId": 1,
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "numberOfGuests": 2
                }
                """.formatted(LocalDate.now().plusDays(30), LocalDate.now().plusDays(35));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: boundary - numberOfGuests = 0 gives 400 (minimum is 1).
    // Fails if: the lower bound is not validated.
    @Test
    @DisplayName("TC-Invalid: Create reservation with zero guests")
    void testCreateReservation_ZeroGuests() throws Exception {
        String payload = """
                {
                  "guestId": 1,
                  "roomTypeId": 1,
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "numberOfGuests": 0
                }
                """.formatted(LocalDate.now().plusDays(30), LocalDate.now().plusDays(35));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    // Checks: first reservation has reservationId, checkInDate, checkOutDate.
    // Fails if: a field is renamed or the list is empty.
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
    
    // Checks: PUT /api/reservations/1 with new dates/guests gives 200.
    // Fails if: reservation 1 doesn't exist or the PUT mapping is removed.
    @Test
    @DisplayName("TC-R5: Update reservation - Success")
    void testUpdateReservation_Success() throws Exception {
        String payload = """
                {
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "numberOfGuests": 3,
                  "specialRequests": "Early checkout"
                }
                """.formatted(LocalDate.now().plusDays(31), LocalDate.now().plusDays(36));

        mockMvc.perform(put("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    // ========== DELETE - CANCEL RESERVATION ==========
    
    // Checks: DELETE /api/reservations/1 (cancel) gives 200.
    // RISK: deletes reservation 1 in the shared DB - can break tests that run after it.
    // Fails if: reservation 1 no longer exists (404).
    @Test
    @DisplayName("TC-R6: Cancel reservation - Success")
    void testCancelReservation_Success() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isOk());
    }

    // ========== STATE TRANSITION - Check-In/Check-Out ==========
    
    // State transition: reservation 1 -> CHECKED_IN via PUT .../check-in (200).
    // Fails if: endpoint missing or reservation 1 is gone.
    @Test
    @DisplayName("TC-R7: Check-in reservation - Success")
    void testCheckInReservation_Success() throws Exception {
        mockMvc.perform(put("/api/reservations/1/check-in"))
                .andExpect(status().isOk());
    }

    // State transition: reservation 1 -> CHECKED_OUT via PUT .../check-out (200).
    // Fails if: endpoint missing or reservation 1 is gone.
    @Test
    @DisplayName("TC-R8: Check-out reservation - Success")
    void testCheckOutReservation_Success() throws Exception {
        mockMvc.perform(put("/api/reservations/1/check-out"))
                .andExpect(status().isOk());
    }

    // ========== PERFORMANCE CHECK ==========
    
    // Checks: GET /api/reservations answers in under 2000 ms.
    // Fails if: limit is lowered or the query becomes slow.
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