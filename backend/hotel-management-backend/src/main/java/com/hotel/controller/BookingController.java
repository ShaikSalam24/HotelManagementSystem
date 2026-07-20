package com.hotel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hotel.dto.booking.BookingRequest;
import com.hotel.dto.booking.BookingResponse;
import com.hotel.enums.BookingStatus;
import com.hotel.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    /**
     * Customer creates a booking
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        BookingResponse response = bookingService.createBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get booking by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id));
    }

    /**
     * Admin & Receptionist - View all bookings
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings());
    }

    /**
     * Get bookings by status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(
            @PathVariable BookingStatus status) {

        return ResponseEntity.ok(
                bookingService.getBookingsByStatus(status));
    }

    /**
     * Logged-in customer booking history
     */
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {

        return ResponseEntity.ok(
                bookingService.getMyBookings());
    }

    /**
     * Customer cancels booking
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id));
    }

    /**
     * Receptionist checks in customer
     */
    @PutMapping("/{id}/check-in")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<BookingResponse> checkIn(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.checkIn(id));
    }

    /**
     * Receptionist checks out customer
     */
    @PutMapping("/{id}/check-out")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<BookingResponse> checkOut(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.checkOut(id));
    }
}