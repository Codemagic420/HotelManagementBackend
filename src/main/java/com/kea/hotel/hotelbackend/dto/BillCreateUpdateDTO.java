package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillCreateUpdateDTO {
    private Long reservationId;
    private BigDecimal totalAmount;
}
