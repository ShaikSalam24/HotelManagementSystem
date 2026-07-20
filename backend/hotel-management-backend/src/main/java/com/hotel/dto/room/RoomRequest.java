package com.hotel.dto.room;


import java.math.BigDecimal;

import com.hotel.enums.RoomStatus;
import com.hotel.enums.RoomType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal pricePerNight;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    @NotNull(message = "Floor is required")
    @Positive(message = "Floor must be greater than zero")
    private Integer floor;

    @NotNull(message = "Status is required")
    private RoomStatus status;

    private String description;
}