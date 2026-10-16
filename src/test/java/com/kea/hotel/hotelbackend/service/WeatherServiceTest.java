package com.kea.hotel.hotelbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Smoke test for WeatherService (calls an external weather API).
 * TYPE: integration - loads the Spring context and injects the real bean.
 * BREAK IT: remove @Service from WeatherService or break its constructor/config -> context fails.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Weather Service Tests")
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    // Checks: the WeatherService bean is created (not null).
    // WEAK: does not call the weather API or check any result.
    @Test
    @DisplayName("Should have weather service configured")
    void testWeatherServiceExists() {
        assertThat(weatherService).isNotNull();
    }
}
