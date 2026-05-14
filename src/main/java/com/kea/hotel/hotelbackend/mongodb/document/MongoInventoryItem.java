package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "inventoryItems")
@Data
@NoArgsConstructor
public class MongoInventoryItem {
    @Id
    private String id;
    private Long inventoryItemId;
    private String name;
    private BigDecimal unitPrice;
    private Boolean active;
}
