package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roomTypes")
@Data
@NoArgsConstructor
public class MongoRoomType {
    @Id
    private String id;
    private Long roomTypeId;
    private String name;
    private Integer maxOccupancy;
}
