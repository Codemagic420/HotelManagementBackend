package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long reservationId;
    private String referenceNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private Integer numGuests;
    private BigDecimal bookedNightlyPrice;
    private String status;
    private LocalDateTime createdAt;
    private String aiNotesSummary;

    private GuestResponseDTO guest;
    private RoomResponseDTO room;
}
