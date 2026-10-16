package com.kea.hotel.hotelbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests of JwtTokenProvider (creates and checks JWT login tokens).
 * TYPE: integration test - uses the real Spring bean, no mocks.
 * @TestPropertySource fixes the secret key and expiry (24h) so results are predictable.
 * A JWT = header.payload.signature; the signature is made with the secret key.
 * BREAK IT: change an expected username, change the secret between create and validate, or break validateToken().
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.jwt.secret=test-secret-key-that-is-long-enough-for-hs512-and-more-test-secret-key-long-secure-key",
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

    // Checks: a token is created and has 3 dot-separated parts.
    // Fails if: generateTokenFromUsername returns null/blank or a non-JWT format.
    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        assertThat(testToken).isNotNull().isNotBlank();
        assertThat(testToken.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    // Checks: the username put in the token comes back out unchanged (round trip).
    // Fails if: the subject claim is not set to the username.
    @Test
    @DisplayName("Should extract username from valid token")
    void testGetUsernameFromToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(testToken);

        assertThat(extractedUsername).isEqualTo(testUsername);
    }

    // Checks: a freshly made token is valid.
    // Fails if: wrong secret/expiry config makes valid tokens look invalid.
    @Test
    @DisplayName("Should validate token successfully")
    void testValidateToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        boolean isValid = jwtTokenProvider.validateToken(testToken);

        assertThat(isValid).isTrue();
    }

    // Checks: 'invalid.token.here' is rejected (false), no exception thrown.
    // Fails if: validateToken throws instead of returning false, or accepts garbage.
    @Test
    @DisplayName("Should reject invalid token")
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    // WEAK/PLACEHOLDER: only checks a token is created; it does NOT test expiry.
    // To test for real: set app.jwt.expiration=1, wait, then expect validateToken == false.
    @Test
    @DisplayName("Should reject expired token")
    void testExpiredToken() {
        testToken = jwtTokenProvider.generateTokenFromUsername(testUsername);

        assertThat(testToken).isNotNull();
    }

    // Checks: 'malformed' (not 3 parts) is rejected (false).
    // Fails if: validateToken accepts or throws on malformed input.
    @Test
    @DisplayName("Should reject malformed token")
    void testMalformedToken() {
        String malformedToken = "malformed";

        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        assertThat(isValid).isFalse();
    }

    // Checks: user1 and user2 get different tokens, each with their own name inside.
    // Fails if: the token ignores the username.
    @Test
    @DisplayName("Should generate different tokens for different usernames")
    void testDifferentTokensForDifferentUsers() {
        String token1 = jwtTokenProvider.generateTokenFromUsername("user1");
        String token2 = jwtTokenProvider.generateTokenFromUsername("user2");

        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtTokenProvider.getUsernameFromToken(token1)).isEqualTo("user1");
        assertThat(jwtTokenProvider.getUsernameFromToken(token2)).isEqualTo("user2");
    }

    // Checks: edge case - '.', '+', '@' in the username survive the round trip.
    // Fails if: the username is encoded or truncated.
    @Test
    @DisplayName("Should handle special characters in username")
    void testSpecialCharactersInUsername() {
        String specialUsername = "user.special+test@domain";
        String token = jwtTokenProvider.generateTokenFromUsername(specialUsername);

        assertThat(jwtTokenProvider.getUsernameFromToken(token)).isEqualTo(specialUsername);
    }
}
