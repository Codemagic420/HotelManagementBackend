package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "extraServices")
@Data
@NoArgsConstructor
public class MongoExtraService {
    @Id
    private String id;
    private Long extraServiceId;
    private String name;
    private BigDecimal unitPrice;
    private String priceUnit;
    private Boolean active;
}
