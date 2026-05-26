package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateUpdateDTO {
    private String referenceNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private Integer numGuests;
    private Long roomTypeId;
    private Long guestId;
    private Long assignedRoomId;
    private Long bookedRateId;
    private BigDecimal bookedNightlyPrice;
    private String status;
}
