package com.hotel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotel.dto.booking.BookingRequest;
import com.hotel.dto.booking.BookingResponse;
import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.entity.User;
import com.hotel.enums.BookingStatus;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.UserRepository;
import com.hotel.service.BookingService;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Builder
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    
    private String generateBookingNumber() {
        return "BK" + System.currentTimeMillis();
    }
    
	private BigDecimal calculateTotalAmount(Room room, LocalDate checkIn, LocalDate checkOut) {

		long days = ChronoUnit.DAYS.between(checkIn, checkOut);

		return room.getPricePerNight().multiply(BigDecimal.valueOf(days));
	}
	
	private BookingResponse mapToResponse(Booking booking) {

		String customerName = booking.getCustomer().getFirstName()
		        + " "
		        + booking.getCustomer().getLastName();
		
	    return BookingResponse.builder()
	            .id(booking.getId())
	            .bookingNumber(booking.getBookingNumber())
	            .customerName(customerName)
	            .roomNumber(booking.getRoom().getRoomNumber())
	            .checkInDate(booking.getCheckInDate())
	            .checkOutDate(booking.getCheckOutDate())
	            .numberOfGuests(booking.getNumberOfGuests())
	            .totalAmount(booking.getTotalAmount())
	            .bookingStatus(booking.getBookingStatus())
	            .specialRequest(booking.getSpecialRequest())
	            .build();
	}
	
	private User getCurrentUser() {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    return userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));
	}

	
    
	@Override
	public BookingResponse createBooking(BookingRequest request) {
		User customer = getCurrentUser();
		
		Room room = roomRepository.findById(request.getRoomId())
		        .orElseThrow(() ->
		                new ResourceNotFoundException("Room not found"));
		if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
		    throw new IllegalArgumentException(
		            "Check-out date must be after check-in date");
		}
		if (request.getNumberOfGuests() > room.getCapacity()) {
		    throw new IllegalArgumentException(
		            "Number of guests exceeds room capacity");
		}
		List<Booking> overlappingBookings =
		        bookingRepository.findOverlappingBookings(
		                room,
		                request.getCheckInDate(),
		                request.getCheckOutDate(),
		                List.of(
		                        BookingStatus.BOOKED,
		                        BookingStatus.CHECKED_IN));

		if (!overlappingBookings.isEmpty()) {
		    throw new IllegalArgumentException(
		            "Room is already booked for the selected dates");
		}
		BigDecimal totalAmount = calculateTotalAmount(
		        room,
		        request.getCheckInDate(),
		        request.getCheckOutDate());
		Booking booking = Booking.builder()
		        .bookingNumber(generateBookingNumber())
		        .customer(customer)
		        .room(room)
		        .checkInDate(request.getCheckInDate())
		        .checkOutDate(request.getCheckOutDate())
		        .numberOfGuests(request.getNumberOfGuests())
		        .totalAmount(totalAmount)
		        .bookingStatus(BookingStatus.BOOKED)
		        .specialRequest(request.getSpecialRequest())
		        .build();
		bookingRepository.save(booking);
		
		return mapToResponse(booking);
	}
	@Override
public BookingResponse getBookingById(Long id) {
    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> 
                    new ResourceNotFoundException("Booking not found with id: " + id));
    return mapToResponse(booking);
}

@Override
public List<BookingResponse> getAllBookings() {
    List<Booking> bookings = bookingRepository.findAll();
    return bookings.stream()
            .map(this::mapToResponse)
            .toList();
}

@Override
public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
    List<Booking> bookings = bookingRepository.findByBookingStatus(status);
    return bookings.stream()
            .map(this::mapToResponse)
            .toList();
}

@Override
public List<BookingResponse> getMyBookings() {
    User currentUser = getCurrentUser();
    List<Booking> bookings = bookingRepository.findByCustomer(currentUser);
    return bookings.stream()
            .map(this::mapToResponse)
            .toList();
}

@Override
public BookingResponse cancelBooking(Long id) {
    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> 
                    new ResourceNotFoundException("Booking not found with id: " + id));
    
    if (booking.getBookingStatus() == BookingStatus.CHECKED_OUT || 
        booking.getBookingStatus() == BookingStatus.CANCELLED) {
        throw new IllegalArgumentException(
                "Cannot cancel a booking with status: " + booking.getBookingStatus());
    }
    
    booking.setBookingStatus(BookingStatus.CANCELLED);
    bookingRepository.save(booking);
    
    return mapToResponse(booking);
}

@Override
public BookingResponse checkIn(Long id) {
    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> 
                    new ResourceNotFoundException("Booking not found with id: " + id));
    
    if (booking.getBookingStatus() != BookingStatus.BOOKED) {
        throw new IllegalArgumentException(
                "Only BOOKED bookings can be checked in. Current status: " + 
                booking.getBookingStatus());
    }
    
    booking.setBookingStatus(BookingStatus.CHECKED_IN);
    bookingRepository.save(booking);
    
    return mapToResponse(booking);
}

@Override
public BookingResponse checkOut(Long id) {
    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> 
                    new ResourceNotFoundException("Booking not found with id: " + id));
    
    if (booking.getBookingStatus() != BookingStatus.CHECKED_IN) {
        throw new IllegalArgumentException(
                "Only CHECKED_IN bookings can be checked out. Current status: " + 
                booking.getBookingStatus());
    }
    
    booking.setBookingStatus(BookingStatus.CHECKED_OUT);
    bookingRepository.save(booking);
    
    return mapToResponse(booking);
}

}
