package com.hotel.service;

import java.util.List;

import com.hotel.dto.booking.BookingRequest;
import com.hotel.dto.booking.BookingResponse;
import com.hotel.enums.BookingStatus;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse getBookingById(Long id);

    List<BookingResponse> getAllBookings();

    List<BookingResponse> getBookingsByStatus(BookingStatus status);

    List<BookingResponse> getMyBookings();

    BookingResponse cancelBooking(Long id);

    BookingResponse checkIn(Long id);

    BookingResponse checkOut(Long id);
}