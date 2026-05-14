package com.kea.hotel.hotelbackend.neo4j.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.math.BigDecimal;
import java.time.LocalDate;

@Node("SeasonRate")
@Data
@NoArgsConstructor
public class Neo4jSeasonRate {
    @Id
    @GeneratedValue
    private Long id;
    private Long rateId;
    private String roomType;
    private String season;
    private BigDecimal pricePerNight;
    private LocalDate validFrom;
    private LocalDate validTo;
}
