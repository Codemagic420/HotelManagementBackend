package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    private Long billId;
    private BigDecimal totalAmount;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    private Long reservationId;
}
