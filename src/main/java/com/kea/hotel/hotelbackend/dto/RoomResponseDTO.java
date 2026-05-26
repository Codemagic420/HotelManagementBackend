package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private Long roomId;
    private String roomNumber;
    private String type;
    private String roomStatus;
    private String cleanStatus;
    private Boolean occupied;
    private String aiAssessmentSummary;
}
