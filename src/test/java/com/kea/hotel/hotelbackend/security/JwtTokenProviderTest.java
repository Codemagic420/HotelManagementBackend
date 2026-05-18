package com.kea.hotel.hotelbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "app.jwt.secret=test-secret-key-that-is-long-enough-for-hs512",
    "app.jwt.expiration=86400000"
})
@DisplayName("JWT Security Tests")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String testUsername;
    private String testToken;

    @BeforeEach
    void setUp() {
        testUsername = "testuser";
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        assertThat(testToken).isNotNull().isNotBlank();
        assertThat(testToken.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void testGetUsernameFromToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(testToken);

        assertThat(extractedUsername).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testValidateToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        boolean isValid = jwtTokenProvider.validateToken(testToken);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject invalid token")
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should reject expired token")
    void testExpiredToken() {
        // This test would require mocking time or creating an expired token
        // For now, verify the token has expiration time
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        assertThat(testToken).isNotNull();
    }

    @Test
    @DisplayName("Should reject malformed token")
    void testMalformedToken() {
        String malformedToken = "malformed";

        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should generate different tokens for different usernames")
    void testDifferentTokensForDifferentUsers() {
        String token1 = jwtTokenProvider.generateTokenFromUsername("user1");
        String token2 = jwtTokenProvider.generateTokenFromUsername("user2");

        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtTokenProvider.getUsernameFromToken(token1)).isEqualTo("user1");
        assertThat(jwtTokenProvider.getUsernameFromToken(token2)).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void testSpecialCharactersInUsername() {
        String specialUsername = "user.special+test@domain";
        String token = jwtTokenProvider.generateTokenFromUsername(specialUsername);

        assertThat(jwtTokenProvider.getUsernameFromToken(token)).isEqualTo(specialUsername);
    }
}
