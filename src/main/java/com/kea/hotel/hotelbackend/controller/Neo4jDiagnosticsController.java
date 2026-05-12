package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.neo4j.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/neo4j/diagnostics")
public class Neo4jDiagnosticsController {

    private final Neo4jCleanerRepository cleanerRepository;
    private final Neo4jGuestRepository guestRepository;
    private final Neo4jRoomRepository roomRepository;
    private final Neo4jRoomTypeRepository roomTypeRepository;
    private final Neo4jReservationRepository reservationRepository;

    public Neo4jDiagnosticsController(Neo4jCleanerRepository cleanerRepository,
                                     Neo4jGuestRepository guestRepository,
                                     Neo4jRoomRepository roomRepository,
                                     Neo4jRoomTypeRepository roomTypeRepository,
                                     Neo4jReservationRepository reservationRepository) {
        this.cleanerRepository = cleanerRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/status")
    public Map<String, Object> getNeo4jStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("neo4j_connected", true);

        try {
            status.put("cleaners_count", cleanerRepository.count());
            status.put("guests_count", guestRepository.count());
            status.put("rooms_count", roomRepository.count());
            status.put("room_types_count", roomTypeRepository.count());
            status.put("reservations_count", reservationRepository.count());
            status.put("message", "Neo4j is connected and responding");
        } catch (Exception e) {
            status.put("neo4j_connected", false);
            status.put("error", e.getMessage());
            status.put("exception_type", e.getClass().getSimpleName());
            status.put("message", "Neo4j connection failed");
        }

        return status;
    }
}
