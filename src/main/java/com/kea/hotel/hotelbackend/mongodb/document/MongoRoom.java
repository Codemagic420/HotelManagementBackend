package com.kea.hotel.hotelbackend.mongodb.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
@Data
@NoArgsConstructor
public class MongoRoom {
    @Id
    private String id;
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private String roomStatus;
    private String cleanStatus;
    private Boolean occupied;
    private List<MongoRoomCleaningTask> cleaningTasks = new ArrayList<>();
    private String aiAssessmentSummary;
    private LocalDateTime aiFieldsUpdatedAt;
}
