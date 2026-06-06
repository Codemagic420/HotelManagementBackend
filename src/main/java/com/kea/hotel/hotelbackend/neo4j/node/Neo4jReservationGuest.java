package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node("ReservationGuest")
public class Neo4jReservationGuest {
    @Id
    private Long reservationGuestId;
    private Long reservationId;
    private Long guestId;
}
