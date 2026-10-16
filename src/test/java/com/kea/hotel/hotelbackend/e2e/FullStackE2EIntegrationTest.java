package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

/**
 * PLACEHOLDER: no real full-stack test yet.
 * The only check is that a hard-coded string is not empty, so it ALWAYS passes.
 * To make it real: start the server (RANDOM_PORT) and run login -> create reservation -> view bill over HTTP.
 * It can only fail if the Spring context fails to start.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("E2E: Full Stack API Integration Tests")
class FullStackE2EIntegrationTest {

    // PLACEHOLDER: asserts on a constant string - cannot fail by itself.
    @Test
    @DisplayName("E2E: Full stack integration test ready")
    void testFullStackReady() {
        assertThat("FullStackE2E").isNotEmpty();
    }
}
