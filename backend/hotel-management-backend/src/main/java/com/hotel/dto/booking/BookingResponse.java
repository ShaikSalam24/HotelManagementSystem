package com.hotel.dto.booking;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.enums.BookingStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {

    private Long id;

    private String bookingNumber;

    private String customerName;

    private String roomNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer numberOfGuests;

    private BigDecimal totalAmount;

    private BookingStatus bookingStatus;

    private String specialRequest;
}
