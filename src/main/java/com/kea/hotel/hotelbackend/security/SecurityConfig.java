package com.kea.hotel.hotelbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF for testing – enable later for forms
                .csrf(csrf -> csrf.disable())
                // configure route rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/guests/**").hasAnyRole("ADMIN", "STAFF")
                        .anyRequest().permitAll()
                )
                // use basic login for now
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
