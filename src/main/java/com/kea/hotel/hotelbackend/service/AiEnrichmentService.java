package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.model.Room;

public interface AiEnrichmentService {
    String generateGuestProfileSummary(Guest guest);
    String generateReservationNotesSummary(Reservation reservation);
    String generateRoomAssessmentSummary(Room room);
}
