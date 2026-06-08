package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "room_cleaning_assignments")
public class MongoRoomCleaningAssignment {
    private String id;
    private Long cleanerId;
    private Long taskId;
    private LocalDateTime assignedAt;
    private String status;
}
