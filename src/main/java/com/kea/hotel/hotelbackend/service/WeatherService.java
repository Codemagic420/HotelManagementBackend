package com.kea.hotel.hotelbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.key:demo}")
    private String apiKey;

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeatherForLocation(String city) {
        try {
            String url = String.format(
                    "%s?q=%s&appid=%s&units=metric",
                    apiUrl, city, apiKey
            );

            logger.info("Fetching weather for city: {}", city);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response != null) {
                logger.info("Weather fetched successfully for {}", city);
            }
            return response;
        } catch (Exception e) {
            logger.error("Error fetching weather for city: {}", city, e);
            return null;
        }
    }

    public static class WeatherResponse {
        private String name;
        private Main main;
        private java.util.List<Weather> weather;

        public static class Main {
            public double temp;
            public double feels_like;
            public int humidity;
        }

        public static class Weather {
            public String description;
            public String icon;
        }

        // Getters
        public String getName() { return name; }
        public Main getMain() { return main; }
        public java.util.List<Weather> getWeather() { return weather; }

        public void setName(String name) { this.name = name; }
        public void setMain(Main main) { this.main = main; }
        public void setWeather(java.util.List<Weather> weather) { this.weather = weather; }
    }
}
