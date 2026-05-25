package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MongoReservation {
    private Long reservationId;
    private String referenceNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private Integer numGuests;
    private String roomType;
    private String assignedRoomNumber;
    private BigDecimal bookedNightlyPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<MongoBill> bills = new ArrayList<>();
    private String aiNotesSummary;
    private LocalDateTime aiFieldsUpdatedAt;
}
