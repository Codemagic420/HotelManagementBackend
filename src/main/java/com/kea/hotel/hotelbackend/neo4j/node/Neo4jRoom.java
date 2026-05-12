package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Room")
@Data
@NoArgsConstructor
public class Neo4jRoom {
    @Id
    @GeneratedValue
    private Long id;
    private Long roomId;
    private String roomNumber;
    private String roomStatus;
    private String cleanStatus;
    private Boolean occupied;

    @Relationship(type = "IS_TYPE", direction = Relationship.Direction.OUTGOING)
    private Neo4jRoomType roomType;

    @Relationship(type = "HAS_TASK", direction = Relationship.Direction.OUTGOING)
    private List<Neo4jRoomCleaningTask> cleaningTasks = new ArrayList<>();
}
