package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest")
@Data
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 4)
    private String creditCardLast4;
}
