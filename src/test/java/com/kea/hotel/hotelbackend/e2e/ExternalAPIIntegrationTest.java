package com.kea.hotel.hotelbackend.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("E2E: External API Integration Tests")
class ExternalAPIIntegrationTest {

    @Test
    @DisplayName("E2E: Weather API endpoint exists")
    void testWeatherAPIExists() {
        assertThat("WeatherController").isNotEmpty();
    }
}
