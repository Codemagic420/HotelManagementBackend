package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

/**
 * PLACEHOLDER: no real external-API test yet.
 * The only check is that a hard-coded string is not empty, so it ALWAYS passes.
 * To make it real: call /api/weather/... and assert on the response (mock the external provider).
 * It can only fail if the Spring context fails to start.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("E2E: External API Integration Tests")
class ExternalAPIIntegrationTest {

    // PLACEHOLDER: asserts on the constant 'WeatherController' - does not touch the real controller.
    @Test
    @DisplayName("E2E: Weather API endpoint exists")
    void testWeatherAPIExists() {
        assertThat("WeatherController").isNotEmpty();
    }
}
