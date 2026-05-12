package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "guests")
@Data
@NoArgsConstructor
public class MongoGuest {
    @Id
    private String id;
    private Long guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String creditCardLast4;
    private List<MongoReservation> reservations = new ArrayList<>();
}
