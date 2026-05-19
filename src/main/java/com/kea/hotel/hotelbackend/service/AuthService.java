package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.dto.LoginRequest;
import com.kea.hotel.hotelbackend.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public LoginResponse authenticate(LoginRequest request) {
        // TODO: Implement authentication logic
        return new LoginResponse("dummy-jwt-token", "Bearer", 86400000L);
    }
}
