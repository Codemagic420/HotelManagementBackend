package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "user_accounts")
public class MongoUserAccount {
    private String id;
    private String username;
    private String passwordHash;
    private String role;
}
