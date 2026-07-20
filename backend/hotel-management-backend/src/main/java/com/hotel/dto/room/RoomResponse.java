package com.hotel.dto.room;


import java.math.BigDecimal;

import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {

    private Long id;

    private String roomNumber;

    private RoomType roomType;

    private BigDecimal pricePerNight;

    private Integer capacity;

    private Integer floor;

    private RoomStatus status;

    private String description;
}