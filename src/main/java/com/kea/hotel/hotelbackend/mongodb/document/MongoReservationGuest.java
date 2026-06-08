package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "reservation_guests")
public class MongoReservationGuest {
    private String id;
    private Long reservationId;
    private Long guestId;
}
