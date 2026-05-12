package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.math.BigDecimal;

@Node("InventoryItem")
@Data
@NoArgsConstructor
public class Neo4jInventoryItem {
    @Id
    @GeneratedValue
    private Long id;
    private Long inventoryItemId;
    private String name;
    private BigDecimal unitPrice;
    private Boolean active;
}
