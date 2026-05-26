package com.kea.hotel.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateUpdateDTO {
    private String roomNumber;
    private Long roomTypeId;
    private String roomStatus;
    private String cleanStatus;
    private Boolean occupied;
    private String type;
}
