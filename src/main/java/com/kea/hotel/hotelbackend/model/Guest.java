package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "guest")
@Data
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 4)
    private String creditCardLast4;

    @Column(columnDefinition = "LONGTEXT")
    private String aiProfileSummary;

    @Column(name = "ai_fields_updated_at")
    private LocalDateTime aiFieldsUpdatedAt;
}
