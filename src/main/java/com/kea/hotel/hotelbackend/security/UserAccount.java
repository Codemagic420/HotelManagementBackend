package com.kea.hotel.hotelbackend.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_account")
@Getter @Setter @NoArgsConstructor
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;
}
