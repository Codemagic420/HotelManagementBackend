package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherResponse getWeather(@PathVariable String city) {
        logger.info("Weather request for city: {}", city);
        WeatherService.WeatherResponse response = weatherService.getWeatherForLocation(city);

        if (response == null) {
            return new WeatherResponse(false, "Unable to fetch weather", null);
        }

        return new WeatherResponse(true, "Weather fetched successfully", response);
    }

    public static class WeatherResponse {
        private boolean success;
        private String message;
        private WeatherService.WeatherResponse data;

        public WeatherResponse(boolean success, String message, WeatherService.WeatherResponse data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public WeatherService.WeatherResponse getData() { return data; }

        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
        public void setData(WeatherService.WeatherResponse data) { this.data = data; }
    }
}
