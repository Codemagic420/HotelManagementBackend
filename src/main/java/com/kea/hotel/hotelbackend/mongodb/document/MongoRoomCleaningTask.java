package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MongoRoomCleaningTask {
    private Long taskId;
    private LocalDateTime createdAt;
    private String taskStatus;
    private String note;
    private String assignedCleaner;
    private LocalDateTime assignedAt;
}
