package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Node("Reservation")
@Data
@NoArgsConstructor
public class Neo4jReservation {
    @Id
    @GeneratedValue
    private String elementId;
    private Long reservationId;
    private String referenceNo;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private Integer numGuests;
    private BigDecimal bookedNightlyPrice;
    private String status;
    private LocalDateTime createdAt;

    @Relationship(type = "BOOKED_BY", direction = Relationship.Direction.INCOMING)
    private Neo4jGuest guest;

    @Relationship(type = "ASSIGNED_TO", direction = Relationship.Direction.OUTGOING)
    private Neo4jRoom room;

    @Relationship(type = "HAS_BILL", direction = Relationship.Direction.OUTGOING)
    private List<Neo4jBill> bills = new ArrayList<>();
}
