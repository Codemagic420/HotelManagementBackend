package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Node("RoomCleaningAssignment")
public class Neo4jRoomCleaningAssignment {
    @Id
    private Long assignmentId;
    private Long cleanerId;
    private Long taskId;
    private LocalDateTime assignedAt;
    private String status;
}
