package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Node("Bill")
@Data
@NoArgsConstructor
public class Neo4jBill {
    @Id
    @GeneratedValue
    private Long id;
    private Long billId;
    private Long reservationId;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal totalAmount;

    @Relationship(type = "CONTAINS_ITEM", direction = Relationship.Direction.OUTGOING)
    private List<Neo4jBillItem> items = new ArrayList<>();
}
