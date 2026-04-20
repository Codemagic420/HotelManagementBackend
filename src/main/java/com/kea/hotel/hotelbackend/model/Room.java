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

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private boolean occupied;
}
