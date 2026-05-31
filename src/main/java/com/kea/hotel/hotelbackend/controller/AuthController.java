package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.dto.LoginRequest;
import com.kea.hotel.hotelbackend.dto.LoginResponse;
import com.kea.hotel.hotelbackend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        if (request.getUsername() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(a -> a.getAuthority())
            .orElse(null);

        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer", 86400, username, role));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorization.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = tokenProvider.getUsernameFromToken(token);
        String newToken = tokenProvider.generateTokenFromUsername(username);

        return ResponseEntity.ok(new LoginResponse(newToken, "Bearer", 86400, username, null));
    }
}
