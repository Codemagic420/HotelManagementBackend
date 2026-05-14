package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_cleaning_assignment")
@Data
@NoArgsConstructor
public class RoomCleaningAssignment {
    @EmbeddedId
    private RoomCleaningAssignmentKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private RoomCleaningTask task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cleanerId")
    @JoinColumn(name = "cleaner_id")
    private Cleaner cleaner;

    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();
}
