package com.kea.hotel.hotelbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class LlmProxyController {

    private final String llmBaseUrl;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LlmProxyController(
            @Value("${app.llm.base-url:http://localhost:8000}") String llmBaseUrl) {
        this.llmBaseUrl = llmBaseUrl;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(llmBaseUrl + "/health"))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> parsed = objectMapper.readValue(resp.body(), Map.class);
            return ResponseEntity.ok(Map.of("llm", parsed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", "LLM service unavailable", "error", e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/guest/ask")
    public ResponseEntity<Map<String, Object>> guestAsk(HttpServletRequest servletRequest) {
        try {
            byte[] rawBytes = servletRequest.getInputStream().readAllBytes();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(llmBaseUrl + "/guest/ask"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(rawBytes))
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> parsed = objectMapper.readValue(resp.body(), Map.class);
            return ResponseEntity.status(resp.statusCode()).body(parsed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "LLM service unavailable: " + e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/staff/ask")
    public ResponseEntity<Map<String, Object>> staffAsk(HttpServletRequest servletRequest) {
        try {
            byte[] rawBytes = servletRequest.getInputStream().readAllBytes();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(llmBaseUrl + "/staff/ask"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(rawBytes))
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> parsed = objectMapper.readValue(resp.body(), Map.class);
            return ResponseEntity.status(resp.statusCode()).body(parsed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "LLM service unavailable: " + e.getMessage()));
        }
    }
}
