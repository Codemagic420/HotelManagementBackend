package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestResponseDTO {
    private Long guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String creditCardLast4;
    private String aiProfileSummary;
}
