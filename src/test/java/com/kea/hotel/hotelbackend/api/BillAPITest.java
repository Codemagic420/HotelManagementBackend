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
 * Black-box tests of the Bill API (/api/bills).
 * TYPE: BLACK-BOX API test. MockMvc sends fake HTTP requests to the real controllers (no real server/port).
 * Security filters are NOT applied here (MockMvc is built without springSecurity()), so no login is needed.
 * Uses the real H2 DB filled by DataInitializer, so tests rely on seeded data (e.g. ID 1 exists).
 * RISK: state-changing tests (PUT/DELETE) are not rolled back and share one DB, so test order can matter.
 * Techniques: equivalence partitioning, boundary values, decision table, negative tests, state transition.
 * BREAK IT: change an expected status code, a URL, or an ID; remove validation in BillController.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Bill API - Black-Box Tests")
class BillAPITest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        // Builds a fresh MockMvc from the Spring context before each test.
        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        }

    // ========== EQUIVALENCE PARTITIONING - GET /api/bills ==========
    
    // Checks: GET /api/bills gives 200, JSON, and the body is a list.
    // Fails if: wrong URL (404) or the endpoint returns an object instead of a list.
    @Test
    @DisplayName("TC-B1: Get all bills - Success")
    void testGetBills_Success() throws Exception {
        mockMvc.perform(get("/api/bills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Bill IDs ==========
    
    // Checks: boundary IDs 99999 (not in DB), 0 and -1 (below valid range) give 404.
    // Runs 3 times. Fails if: one of those IDs is expected to give 200, or the controller returns 400/500.
    @ParameterizedTest(name = "Invalid bill ID: {0}")
    @CsvSource({
        "99999",
        "0",
        "-1"
    })
    @DisplayName("TC-B2: Get bill with invalid ID - Should return 404")
    void testGetBill_InvalidId_NotFound(long billId) throws Exception {
        mockMvc.perform(get("/api/bills/" + billId))
                .andExpect(status().isNotFound());
    }

    // Checks: bill 1 exists and has billId and totalAmount.
    // Fails if: bill 1 is missing from seed data or a field is renamed.
    @Test
    @DisplayName("TC-B3: Get bill with valid ID - Success")
    void testGetBill_ValidId_Success() throws Exception {
        mockMvc.perform(get("/api/bills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billId").exists())
                .andExpect(jsonPath("$.totalAmount").exists());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Amount Ranges ==========
    
    // Checks: boundary amounts 0.00 / 1.00 / 9999.99 (with tax) are accepted (2xx).
    // Fails if: the API starts rejecting 0.00 or large amounts.
    @ParameterizedTest(name = "BVA: Amount={0}, Tax={1}")
    @CsvSource({
        "0.00, 0.00",      // Minimum
        "1.00, 0.25",      // Small amount
        "9999.99, 2499.97" // Large amount
    })
    @DisplayName("TC-B4: Create bill with various amounts")
    void testCreateBill_VariousAmounts(String amount, String tax) throws Exception {
        String payload = String.format("""
                {
                  "reservationId": 1,
                  "roomCharge": %s,
                  "taxAmount": %s,
                  "discountAmount": 0.00
                }
                """, amount, tax);

        mockMvc.perform(post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== DECISION TABLE - POST /api/bills ==========
    
    // Decision table: status (PENDING/PAID) x discount (0/50) - all valid, so 2xx.
    // Fails if: a combination is rejected by validation.
    @ParameterizedTest(name = "DT-B1: Create bill with status={0}, discount={1}")
    @CsvSource({
        "PENDING, 0.00",
        "PENDING, 50.00",
        "PAID, 0.00"
    })
    @DisplayName("DT-B1: Create bill with valid combinations")
    void testCreateBill_ValidCombinations(String status, String discount) throws Exception {
        String payload = String.format("""
                {
                  "reservationId": 1,
                  "roomCharge": 500.00,
                  "taxAmount": 125.00,
                  "discountAmount": %s,
                  "billStatus": "%s"
                }
                """, discount, status);

        mockMvc.perform(post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
    // Checks: negative test - no reservationId gives 400.
    // Fails if: the API accepts a bill without a reservation.
    @Test
    @DisplayName("TC-Invalid: Create bill with missing reservationId")
    void testCreateBill_MissingReservationId() throws Exception {
        String payload = """
                {
                  "roomCharge": 500.00,
                  "taxAmount": 125.00
                }
                """;

        mockMvc.perform(post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: negative roomCharge (-500) gives 400.
    // Fails if: negative amounts are not validated.
    @Test
    @DisplayName("TC-Invalid: Create bill with negative amount")
    void testCreateBill_NegativeAmount() throws Exception {
        String payload = """
                {
                  "reservationId": 1,
                  "roomCharge": -500.00,
                  "taxAmount": 125.00
                }
                """;

        mockMvc.perform(post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // Checks: discount (150) larger than charge (100) gives 400.
    // Fails if: the business rule 'discount <= charge' is missing.
    @Test
    @DisplayName("TC-Invalid: Create bill with discount > amount")
    void testCreateBill_DiscountExceedsAmount() throws Exception {
        String payload = """
                {
                  "reservationId": 1,
                  "roomCharge": 100.00,
                  "taxAmount": 25.00,
                  "discountAmount": 150.00
                }
                """;

        mockMvc.perform(post("/api/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // ========== RESPONSE VALIDATION ==========
    
    // Checks: first bill has billId, reservationId, totalAmount, billStatus.
    // Fails if: a field is renamed or the list is empty.
    @Test
    @DisplayName("GET /api/bills - Response contains expected fields")
    void testGetBills_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/bills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].billId").exists())
                .andExpect(jsonPath("$[0].reservationId").exists())
                .andExpect(jsonPath("$[0].totalAmount").exists())
                .andExpect(jsonPath("$[0].billStatus").exists());
    }

    // ========== PUT - UPDATE BILL ==========
    
    // Checks: PUT /api/bills/1 with new amounts gives 200.
    // Fails if: bill 1 doesn't exist or the PUT mapping is removed.
    @Test
    @DisplayName("TC-B5: Update bill - Success")
    void testUpdateBill_Success() throws Exception {
        String payload = """
                {
                  "roomCharge": 600.00,
                  "taxAmount": 150.00,
                  "discountAmount": 25.00,
                  "billStatus": "PENDING"
                }
                """;

        mockMvc.perform(put("/api/bills/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    // ========== STATE TRANSITION - Bill Payment ==========
    
    // State transition: bill goes PENDING -> PAID via PUT /api/bills/1/pay (200).
    // Fails if: /pay endpoint is missing or bill 1 doesn't exist.
    @Test
    @DisplayName("TC-B6: Mark bill as paid - Success")
    void testPayBill_Success() throws Exception {
        String payload = """
                {
                  "paymentMethod": "CREDIT_CARD",
                  "transactionId": "TXN-12345"
                }
                """;

        mockMvc.perform(put("/api/bills/1/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    // ========== PERFORMANCE CHECK ==========
    
    // Checks: GET /api/bills answers in under 2000 ms.
    // Fails if: limit is lowered (e.g. 1 ms) or the query becomes slow.
    // Uses Java 'assert' (needs -ea, which Surefire enables).
    @Test
    @DisplayName("GET /api/bills - Response time < 2 seconds")
    void testGetBills_Performance() throws Exception {
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/bills"))
                .andExpect(status().isOk());
        
        long duration = System.currentTimeMillis() - startTime;
        assert duration < 2000 : "Response too slow: " + duration + "ms";
    }
}