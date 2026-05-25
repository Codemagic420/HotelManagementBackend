package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(unique = true)
    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(nullable = false, length = 20)
    private String roomStatus;

    @Column(nullable = false, length = 20)
    private String cleanStatus;

    @Column(nullable = false)
    private Boolean occupied = false;

    @Column(length = 255)
    private String type;

    @Column(columnDefinition = "LONGTEXT")
    private String aiAssessmentSummary;

    @Column(name = "ai_fields_updated_at")
    private LocalDateTime aiFieldsUpdatedAt;
}
