package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("RoomType")
@Data
@NoArgsConstructor
public class Neo4jRoomType {
    @Id
    @GeneratedValue
    private String elementId;
    private Long roomTypeId;
    private String name;
    private Integer maxOccupancy;
}
