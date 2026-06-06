package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node("UserAccount")
public class Neo4jUserAccount {
    @Id
    private Long id;
    private String username;
    private String passwordHash;
    private String role;
}
