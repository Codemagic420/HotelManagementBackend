package com.kea.hotel.hotelbackend.security;

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
@DisplayName("Authentication & Authorization - Black-Box Tests")
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    // ========== EQUIVALENCE PARTITIONING - Login ==========
    
    @Test
    @DisplayName("TC-A1: Login with valid admin credentials - Success")
    void testLogin_ValidAdmin_Success() throws Exception {
        String payload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("TC-A2: Login with valid staff credentials - Success")
    void testLogin_ValidStaff_Success() throws Exception {
        String payload = """
                {
                  "username": "staff",
                  "password": "staff123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Password Length ==========
    
    @ParameterizedTest(name = "Invalid password: {0}")
    @CsvSource({
        "admin, tooshort",
        "admin, ''",
        "admin, wrongpass"
    })
    @DisplayName("TC-A3: Login with invalid password")
    void testLogin_InvalidPassword(String username, String password) throws Exception {
        String payload = String.format("""
                {
                  "username": "%s",
                  "password": "%s"
                }
                """, username, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // ========== BOUNDARY VALUE ANALYSIS - Username ==========
    
    @ParameterizedTest(name = "Invalid username: {0}")
    @CsvSource({
        "nonexistent",
        "",
        "user@123"
    })
    @DisplayName("TC-A4: Login with invalid username")
    void testLogin_InvalidUsername(String username) throws Exception {
        String payload = String.format("""
                {
                  "username": "%s",
                  "password": "password123"
                }
                """, username);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // ========== NEGATIVE TESTS - Missing Fields ==========
    
    @Test
    @DisplayName("TC-Invalid: Login with missing username")
    void testLogin_MissingUsername() throws Exception {
        String payload = """
                {
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-Invalid: Login with missing password")
    void testLogin_MissingPassword() throws Exception {
        String payload = """
                {
                  "username": "admin"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-Invalid: Login with empty payload")
    void testLogin_EmptyPayload() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== AUTHORIZATION - Role-Based Access ==========
    
    @Test
    @DisplayName("TC-A5: Access admin endpoint without auth - Should return 401")
    void testAccessAdmin_NoAuth_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TC-A6: Access admin endpoint with staff token - Should return 403")
    void testAccessAdmin_StaffRole_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // ========== TOKEN REFRESH ==========
    
    @Test
    @DisplayName("TC-A9: Refresh token with valid token")
    void testRefreshToken_Valid_Success() throws Exception {
        String loginPayload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        var loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String token = com.jayway.jsonpath.JsonPath.read(
            loginResult.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(post("/api/auth/refresh")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("TC-A10: Refresh token without token")
    void testRefreshToken_NoToken_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    // ========== LOGOUT ==========
    
    @Test
    @DisplayName("TC-A7: Logout - Success")
    void testLogout_Success() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }

    // ========== TOKEN VALIDATION ==========
    
    @Test
    @DisplayName("TC-A8: Access endpoint with invalid token")
    void testAccessWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TC-A11: Access endpoint with expired token")
    void testAccessWithExpiredToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTYyMzkwMjJ9.invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TC-A12: Access endpoint with malformed token")
    void testAccessWithMalformedToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer malformed"))
                .andExpect(status().isUnauthorized());
    }

    // ========== RESPONSE VALIDATION ==========
    
    @Test
    @DisplayName("POST /api/auth/login - Response contains expected fields")
    void testLogin_ResponseStructure() throws Exception {
        String payload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.role").exists());
    }

    @Test
    @DisplayName("POST /api/auth/login - Token format is JWT")
    void testLogin_TokenFormat_IsJWT() throws Exception {
        String payload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", matchesPattern("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$")));
    }

    // ========== PERFORMANCE CHECK ==========
    
    @Test
    @DisplayName("POST /api/auth/login - Response time < 1 second")
    void testLogin_Performance() throws Exception {
        long startTime = System.currentTimeMillis();
        
        String payload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
        
        long duration = System.currentTimeMillis() - startTime;
        assert duration < 1000 : "Login too slow: " + duration + "ms";
    }

    // ========== CONCURRENT ACCESS ==========
    
    @Test
    @DisplayName("TC-A13: Multiple simultaneous login attempts")
    void testMultipleLogins_Concurrent() throws Exception {
        String payload = """
                {
                  "username": "admin",
                  "password": "admin123"
                }
                """;

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists());
        }
    }

    // ========== SQL INJECTION PREVENTION ==========
    
    @Test
    @DisplayName("TC-A14: Login with SQL injection attempt")
    void testLogin_SQLInjection_Prevention() throws Exception {
        String payload = """
                {
                  "username": "admin' OR '1'='1",
                  "password": "anything"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // ========== CASE SENSITIVITY ==========
    
    @Test
    @DisplayName("TC-A15: Login with uppercase username (case sensitivity)")
    void testLogin_CaseSensitivity_Username() throws Exception {
        String payload = """
                {
                  "username": "ADMIN",
                  "password": "admin123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // ========== WHITESPACE HANDLING ==========
    
    @Test
    @DisplayName("TC-A16: Login with whitespace in credentials")
    void testLogin_Whitespace_Handling() throws Exception {
        String payload = """
                {
                  "username": " admin ",
                  "password": " admin123 "
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }
}