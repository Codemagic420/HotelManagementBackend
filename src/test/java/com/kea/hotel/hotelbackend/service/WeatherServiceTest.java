package com.kea.hotel.hotelbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Weather Service Tests")
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    @DisplayName("Should have weather service configured")
    void testWeatherServiceExists() {
        assertThat(weatherService).isNotNull();
    }
}
