package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.*;

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
}
