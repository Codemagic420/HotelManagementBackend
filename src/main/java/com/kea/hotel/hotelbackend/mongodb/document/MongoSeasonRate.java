package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "seasonRates")
@Data
@NoArgsConstructor
public class MongoSeasonRate {
    @Id
    private String id;
    private Long rateId;
    private String roomType;
    private String season;
    private BigDecimal pricePerNight;
    private LocalDate validFrom;
    private LocalDate validTo;
}
