package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MongoBill {
    private Long billId;
    private Long reservationId;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal totalAmount;
    private List<MongoBillItem> items = new ArrayList<>();
}
