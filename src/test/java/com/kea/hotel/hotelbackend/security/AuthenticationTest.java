package com.kea.hotel.hotelbackend.security;

import static org.hamcrest.Matchers.matchesPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * BLACK-BOX tests of login, JWT access and role rules (POST /api/auth/login etc.).
 * TYPE: integration test with MockMvc + the REAL Spring Security filter chain (springSecurity()).
 * Users admin/admin123 and staff/staff123 are created at startup by DataInitializer.
 * Techniques: equivalence partitioning, negative tests, authorization (401 vs 403), security (SQL injection).
 * BREAK IT: change an expected status (e.g. 401 -> 200), use a wrong password for a 'valid' login,
 * or permit /api/** in SecurityConfig (401/403 tests fail).
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Authentication & Authorization - Black-Box Tests")
class AuthenticationTest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        // Builds MockMvc with springSecurity() so filters (JWT, 401/403) really run.
        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        }

    // ========== EQUIVALENCE PARTITIONING - Login ==========
    
    // Checks: valid partition - admin logs in, gets 200 and a token.
    // Fails if: password is wrong (-> 401) or response has no 'token' field.
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

    // Checks: valid partition - staff user logs in, gets 200 and a token.
    // Fails if: staff user is not seeded or password is wrong.
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

    // ========== EQUIVALENCE PARTITIONING - Invalid passwords (wrong / too short / empty) ==========
    
        // Checks: wrong, too short and empty passwords all give 401.
        // Runs 3 times (one per CsvSource row).
        // Fails if: a row contains the real password 'admin123' (login would succeed -> 200).
        @ParameterizedTest(name = "Invalid password: {0}")
        @CsvSource({
                "admin,tooshort",
                "admin,''",
                "admin,wrongpass"
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

    // ========== EQUIVALENCE PARTITIONING - Invalid usernames (unknown / empty / special chars) ==========
    
        // Checks: unknown, empty and special-character usernames give 401.
        // Fails if: a row is a real username with a matching password.
        @ParameterizedTest(name = "Invalid username: {0}")
        @CsvSource({
                "nonexistent",
                "''",
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
    
    // Checks: negative test - JSON without 'username' gives 400 (Bad Request).
    // Fails if: the controller stops validating input (would return 401/500 instead).
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

    // Checks: negative test - JSON without 'password' gives 400.
    // Fails if: the controller stops validating input.
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

    // Checks: negative test - '{}' gives 400.
    // Fails if: the controller stops validating input.
    @Test
    @DisplayName("TC-Invalid: Login with empty payload")
    void testLogin_EmptyPayload() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ========== AUTHORIZATION - Role-Based Access ==========
    
    // Checks: no token on a protected URL gives 401.
    // Fails if: /api/admin/** is made permitAll() in SecurityConfig.
    @Test
    @DisplayName("TC-A5: Access admin endpoint without auth - Should return 401")
    void testAccessAdmin_NoAuth_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    // Checks: logged in as STAFF but not allowed on admin URL gives 403.
    // Steps: login as staff -> read token -> call /api/admin/users with it.
    // 401 = not logged in; 403 = logged in but wrong role.
    // Fails if: you log in as admin (-> 200) or allow STAFF on that endpoint.
    @Test
    @DisplayName("TC-A6: Access admin endpoint with staff token - Should return 403")
    void testAccessAdmin_StaffRole_Forbidden() throws Exception {
        String payload = """
                {
                  "username": "staff",
                  "password": "staff123"
                }
                """;

        var loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        String token = com.jayway.jsonpath.JsonPath.read(
            loginResult.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // ========== TOKEN REFRESH ==========
    
    // Checks: a valid token can be refreshed and a new token comes back.
    // Fails if: the token is invalid/missing in the header.
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

    // Checks: refresh without a token gives 401.
    // Fails if: refresh endpoint stops requiring a token.
    @Test
    @DisplayName("TC-A10: Refresh token without token")
    void testRefreshToken_NoToken_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    // ========== LOGOUT ==========
    
    // Checks: logout returns 200.
    // Note: JWT is stateless, so logout only answers OK; it does not invalidate the token.
    @Test
    @DisplayName("TC-A7: Logout - Success")
    void testLogout_Success() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }

    // ========== TOKEN VALIDATION ==========
    
    // Checks: random string as Bearer token gives 401.
    // Fails if: the JWT filter accepts unsigned/garbage tokens.
    @Test
    @DisplayName("TC-A8: Access endpoint with invalid token")
    void testAccessWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    // Checks: a bad token (header.payload.'invalid') gives 401.
    // Note: the signature is fake, so it is rejected for a bad signature, not for being expired.
    // A real expiry test needs a token with a past 'exp' signed with the real secret.
    @Test
    @DisplayName("TC-A11: Access endpoint with expired token")
    void testAccessWithExpiredToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTYyMzkwMjJ9.invalid"))
                .andExpect(status().isUnauthorized());
    }

    // Checks: token that is not 3 JWT parts gives 401.
    // Fails if: the filter lets malformed tokens through.
    @Test
    @DisplayName("TC-A12: Access endpoint with malformed token")
    void testAccessWithMalformedToken() throws Exception {
        mockMvc.perform(get("/api/guests")
                .header("Authorization", "Bearer malformed"))
                .andExpect(status().isUnauthorized());
    }

    // ========== RESPONSE VALIDATION ==========
    
    // Checks: login response JSON has token, username and role.
    // Fails if: a field is renamed/removed in the login response DTO.
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

    // Checks: token matches JWT shape: three base64url parts split by dots.
    // Fails if: the app returns a non-JWT token (e.g. a UUID).
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
    
    // Checks: login finishes in under 1000 ms (non-functional requirement).
    // Uses Java 'assert', which only runs with -ea (Maven Surefire enables it).
    // Fails if: you lower the limit (e.g. 1 ms) or login becomes slow (BCrypt cost, slow DB).
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

    // ========== REPEATED LOGINS (sequential loop, not truly concurrent) ==========
    
    // Checks: logging in 5 times in a row always works (no state is broken).
    // Note: loop is sequential, NOT truly concurrent (that would need threads).
    // Fails if: a repeated login starts failing (e.g. account locking).
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
    
    // Checks: security - username "admin' OR '1'='1" is NOT a login bypass (401).
    // Safe because JPA uses parameterised queries.
    // Fails if: the app builds SQL by string concatenation.
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
    
    // Checks: 'ADMIN' is not the same user as 'admin' (401).
    // Fails if: user lookup becomes case-insensitive.
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
    
    // Checks: ' admin ' with spaces is rejected (401), i.e. input is not trimmed.
    // Fails if: the app starts trimming credentials.
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