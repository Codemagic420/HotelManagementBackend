package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Node("RoomCleaningTask")
@Data
@NoArgsConstructor
public class Neo4jRoomCleaningTask {
    @Id
    @GeneratedValue
    private Long id;
    private Long taskId;
    private LocalDateTime createdAt;
    private String taskStatus;
    private String note;

    @Relationship(type = "ASSIGNED_TO", direction = Relationship.Direction.INCOMING)
    private Neo4jCleaner cleaner;
}
