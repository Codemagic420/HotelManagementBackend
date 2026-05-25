package com.kea.hotel.hotelbackend.service.impl;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.service.AiEnrichmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AiEnrichmentServiceImpl implements AiEnrichmentService {
    private static final Logger logger = Logger.getLogger(AiEnrichmentServiceImpl.class.getName());

    @Value("${ai.ollama.url:http://localhost:11434}")
    private String ollamaUrl;

    @Value("${ai.ollama.model:mistral:7b}")
    private String ollamaModel;

    @Override
    public String generateGuestProfileSummary(Guest guest) {
        String prompt = String.format(
            "Create a brief 2-3 sentence profile summary for a hotel guest: %s %s (Email: %s, Phone: %s). " +
            "Focus on what type of guest they might be and any patterns you can infer from the information provided. " +
            "Be concise and professional.",
            guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPhone()
        );
        return callOllama(prompt);
    }

    @Override
    public String generateReservationNotesSummary(Reservation reservation) {
        String guestName = reservation.getGuest() != null ?
            reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName() : "Unknown";
        String roomNumber = reservation.getAssignedRoom() != null ?
            reservation.getAssignedRoom().getRoomNumber() : "Not assigned";

        String prompt = String.format(
            "Create a brief operational summary for a hotel reservation: " +
            "Guest: %s, Room: %s, Check-in: %s, Check-out: %s, Duration: %d nights, Status: %s. " +
            "Include any important operational notes or flags (e.g., early arrival, late checkout request). " +
            "Be concise and actionable for staff.",
            guestName, roomNumber, reservation.getCheckInDate(),
            reservation.getCheckOutDate(), reservation.getNights(), reservation.getStatus()
        );
        return callOllama(prompt);
    }

    @Override
    public String generateRoomAssessmentSummary(Room room) {
        String prompt = String.format(
            "Create a brief 2-3 sentence assessment summary for a hotel room: " +
            "Room Number: %s, Status: %s, Clean Status: %s, Occupied: %s. " +
            "Focus on any maintenance or operational concerns based on the status information. " +
            "Be concise and highlight priority issues.",
            room.getRoomNumber(), room.getRoomStatus(), room.getCleanStatus(), room.getOccupied()
        );
        return callOllama(prompt);
    }

    private String callOllama(String prompt) {
        try {
            URL url = new URL(ollamaUrl + "/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);

            String jsonPayload = String.format(
                "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
                ollamaModel,
                escapeJson(prompt)
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.log(Level.WARNING, "Ollama API error. Response code: " + responseCode);
                return "AI enrichment unavailable";
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            String responseJson = response.toString();
            String generatedText = extractResponse(responseJson);
            return generatedText.isEmpty() ? "Unable to generate summary" : generatedText;

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error calling Ollama API: " + e.getMessage());
            return "AI enrichment service unavailable";
        }
    }

    private String escapeJson(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    private String extractResponse(String jsonResponse) {
        try {
            int responseIndex = jsonResponse.indexOf("\"response\":\"");
            if (responseIndex == -1) {
                return "";
            }
            int start = responseIndex + 12;
            int end = jsonResponse.indexOf("\"", start);
            if (end == -1) {
                return "";
            }
            String extracted = jsonResponse.substring(start, end);
            return extracted.replace("\\n", " ").replace("\\\\", "\\").trim();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error parsing Ollama response: " + e.getMessage());
            return "";
        }
    }
}
