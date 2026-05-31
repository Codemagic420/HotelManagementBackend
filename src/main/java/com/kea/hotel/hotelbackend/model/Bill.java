package com.kea.hotel.hotelbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private LocalDateTime openedAt = LocalDateTime.now();

    private LocalDateTime closedAt;

    @Column(nullable = false)
    private BigDecimal roomCharge = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(length = 50)
    private String billStatus = "PENDING";

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @JsonProperty("reservationId")
    public Long getReservationId() {
        return reservation != null ? reservation.getReservationId() : null;
    }
}
