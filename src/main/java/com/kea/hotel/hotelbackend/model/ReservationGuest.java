package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_guest")
@Data
@NoArgsConstructor
public class ReservationGuest {
    @EmbeddedId
    private ReservationGuestKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guestId")
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(nullable = false)
    private Boolean isPrimary = false;
}
