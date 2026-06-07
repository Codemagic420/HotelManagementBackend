package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "bill_items")
@Data
@NoArgsConstructor
public class MongoBillItem {
    @Id
    private String id;
    private Long billItemId;
    private Long billId;
    private String itemType;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private LocalDateTime postedAt;
}
