package com.kea.hotel.hotelbackend.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Bill API - Black-Box Tests")
class BillAPITest {

    @Autowired
    private MockMvc mockMvc;

    // ========== EQUIVALENCE PARTITIONING - GET /api/bills ==========
    
    @Test
    @DisplayName("TC-B1: Get all bills - Success")
    void testGetBills_Success() throws Exception {
        mockMvc.perform(get("/api/bills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ========== BOUNDARY VALUE ANALYSIS - Bill IDs ==========
    
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

    @Test
    @DisplayName("TC-B3: Get bill with valid ID - Success")
    void testGetBill_ValidId_Success() throws Exception {
        mockMvc.perform(get("/api/bills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billId").exists())
                .andExpect(jsonPath("$.totalAmount").exists());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Amount Ranges ==========
    
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
                .andExpect(status().isIn(200, 201));
    }

    // ========== DECISION TABLE - POST /api/bills ==========
    
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
                .andExpect(status().isIn(200, 201));
    }

    // ========== NEGATIVE TESTS - Invalid Input ==========
    
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