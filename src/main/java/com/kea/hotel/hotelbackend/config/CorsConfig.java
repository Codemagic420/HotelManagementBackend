package com.kea.hotel.hotelbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",    // React/Node dev server
                    "http://localhost:3001",    // Local frontend dev server
                    "http://127.0.0.1:3001",    // Loopback IP
                    "http://localhost:5173",    // Vite dev server
                    "http://localhost:8088",    // Local nginx frontend
                    "http://127.0.0.1:8088",    // Loopback nginx frontend
                    "http://localhost:8000",    // Alternative
                    "http://127.0.0.1:5500",    // Common static server
                    "http://localhost:5500",
                    "null"                     // file:// origins appear as "null"
                )
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
