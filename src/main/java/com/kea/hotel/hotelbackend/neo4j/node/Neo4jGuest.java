package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Guest")
@Data
@NoArgsConstructor
public class Neo4jGuest {
    @Id
    @GeneratedValue
    private String elementId;
    private Long guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String creditCardLast4;

    @Relationship(type = "MADE_RESERVATION", direction = Relationship.Direction.OUTGOING)
    private List<Neo4jReservation> reservations = new ArrayList<>();
}
