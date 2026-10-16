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

/**
 * Black-box tests of the Guest API (/api/guests).
 * TYPE: BLACK-BOX API test. MockMvc sends fake HTTP requests to the real controllers (no real server/port).
 * Security filters are NOT applied here (MockMvc is built without springSecurity()), so no login is needed.
 * Uses the real H2 DB filled by DataInitializer, so tests rely on seeded data (e.g. ID 1 exists).
 * RISK: state-changing tests (PUT/DELETE) are not rolled back and share one DB, so test order can matter.
 * Techniques: equivalence partitioning, boundary values, decision table, negative tests.
 * BREAK IT: change an expected status code, an ID, or remove @Valid/@Email in Guest/GuestController.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Guest API - Black-Box Tests")
class GuestAPITest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        // Builds a fresh MockMvc from the Spring context before each test.
        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        }

    // ========== EQUIVALENCE PARTITIONING - GET /api/guests ==========
    
    // Checks: GET /api/guests gives 200, JSON, body is a list.
    // Fails if: wrong URL or the endpoint returns an object.
    @Test
    @DisplayName("TC-G1: Get all guests - Success")
    void testGetGuests_Success() throws Exception {
        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Guest IDs ==========
    
    // Checks: boundary IDs 99999, 0, -1 give 404.
    // Runs 3 times. Fails if: another status is returned.
    @ParameterizedTest(name = "Invalid guest ID: {0}")
    @CsvSource({
        "99999",
        "0",
        "-1"
    })
    @DisplayName("TC-G2: Get guest with invalid ID - Should return 404")
    void testGetGuest_InvalidId_NotFound(long guestId) throws Exception {
        mockMvc.perform(get("/api/guests/" + guestId))
                .andExpect(status().isNotFound());
    }

    // Checks: guest 1 exists, has guestId and a string firstName.
    // Fails if: guest 1 is missing (e.g. deleted by TC-G5) or firstName is not a string.
    @Test
    @DisplayName("TC-G3: Get guest with valid ID - Success")
    void testGetGuest_ValidId_Success() throws Exception {
        mockMvc.perform(get("/api/guests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestId").exists())
                .andExpect(jsonPath("$.firstName").isString());
    }

    // ========== DECISION TABLE - POST /api/guests ==========
    
    // Decision table: 3 valid email + phone combinations all give 2xx.
    // Fails if: validation rejects a valid email/phone format.
    @ParameterizedTest(name = "DT-G1: Create guest with email={0}, phone={1}")
    @CsvSource({
        "valid@email.com, +4540123456",
        "guest@hotel.dk, +4540111111",
        "test@test.com, +4599999999"
    })
    @DisplayName("DT-G1: Create guest with valid combinations")
    void testCreateGuest_ValidCombinations(String email, String phone) throws Exception {
        String payload = String.format("""
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "%s",
                  "phone": "%s",
                  "address": "123 Main St",
                  "city": "Copenhagen"
                }
                """, email, phone);

        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
    // Checks: negative test - no firstName gives 400.
    // Fails if: @NotBlank on firstName is removed.
    @Test
    @DisplayName("TC-Invalid: Create guest with missing firstName")
    void testCreateGuest_MissingFirstName() throws Exception {
        String payload = """
                {
                  "lastName": "Doe",
                  "email": "test@email.com",
                  "phone": "+4540123456"
                }
                """;

        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: 'invalid-email' (no @) gives 400.
    // Fails if: @Email validation is removed.
    @Test
    @DisplayName("TC-Invalid: Create guest with invalid email format")
    void testCreateGuest_InvalidEmail() throws Exception {
        String payload = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "invalid-email",
                  "phone": "+4540123456"
                }
                """;

        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: negative test - '{}' gives 400.
    // Fails if: required-field validation is removed.
    @Test
    @DisplayName("TC-Invalid: Create guest with empty payload")
    void testCreateGuest_EmptyPayload() throws Exception {
        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    // Checks: first guest has guestId, firstName, email.
    // Fails if: a field is renamed or the list is empty.
    @Test
    @DisplayName("GET /api/guests - Response contains expected fields")
    void testGetGuests_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].guestId").exists())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].email").exists());
    }

    // ========== PUT - UPDATE GUEST ==========
    
    // Checks: PUT /api/guests/1 with new data gives 200.
    // Fails if: guest 1 doesn't exist or the PUT mapping is removed.
    @Test
    @DisplayName("TC-G4: Update guest - Success")
    void testUpdateGuest_Success() throws Exception {
        String payload = """
                {
                  "firstName": "Jane",
                  "lastName": "Smith",
                  "email": "jane@email.com",
                  "phone": "+4540999999"
                }
                """;

        mockMvc.perform(put("/api/guests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    // Checks: DELETE /api/guests/1 gives 204 (No Content).
    // RISK: deletes guest 1 in the shared DB - can break tests that run after it.
    // Fails if: the controller returns 200 instead of 204, or guest 1 is gone.
    @Test
    @DisplayName("TC-G5: Delete guest - Success")
    void testDeleteGuest_Success() throws Exception {
        mockMvc.perform(delete("/api/guests/1"))
                .andExpect(status().isNoContent());
    }

    // ========== PERFORMANCE CHECK ==========
    
    // Checks: GET /api/guests answers in under 2000 ms.
    // Fails if: limit is lowered or the query becomes slow.
    @Test
    @DisplayName("GET /api/guests - Response time < 2 seconds")
    void testGetGuests_Performance() throws Exception {
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk());
        
        long duration = System.currentTimeMillis() - startTime;
        assert duration < 2000 : "Response too slow: " + duration + "ms";
    }
}