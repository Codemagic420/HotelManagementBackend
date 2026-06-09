package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Cleaner")
@Data
@NoArgsConstructor
public class Neo4jCleaner {
    @Id
    @GeneratedValue
    private String elementId;
    private Long cleanerId;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean active;

    @Relationship(type = "ASSIGNED_TO", direction = Relationship.Direction.OUTGOING)
    private List<Neo4jRoomCleaningTask> tasks = new ArrayList<>();
}
