package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationGuestKey implements Serializable {
    private Long reservationId;
    private Long guestId;
}
