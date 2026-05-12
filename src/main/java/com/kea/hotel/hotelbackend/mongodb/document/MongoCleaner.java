package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cleaners")
@Data
@NoArgsConstructor
public class MongoCleaner {
    @Id
    private String id;
    private Long cleanerId;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean active;
}
