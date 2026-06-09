package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.math.BigDecimal;

@Node("ExtraService")
@Data
@NoArgsConstructor
public class Neo4jExtraService {
    @Id
    @GeneratedValue
    private String elementId;
    private Long extraServiceId;
    private String name;
    private BigDecimal unitPrice;
    private String priceUnit;
    private Boolean active;
}
