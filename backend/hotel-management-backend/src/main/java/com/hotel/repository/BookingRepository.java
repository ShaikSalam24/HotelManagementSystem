package com.hotel.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.entity.User;
import com.hotel.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    boolean existsByBookingNumber(String bookingNumber);

    List<Booking> findByCustomer(User customer);

    List<Booking> findByRoom(Room room);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByCustomerAndBookingStatus(User customer,
                                                 BookingStatus bookingStatus);
    
    @Query("""
    	    SELECT b FROM Booking b
    	    WHERE b.room = :room
    	    AND b.bookingStatus IN (:statuses)
    	    AND b.checkInDate < :checkOutDate
    	    AND b.checkOutDate > :checkInDate
    	    """)
    	List<Booking> findOverlappingBookings(
    	        @Param("room") Room room,
    	        @Param("checkInDate") LocalDate checkInDate,
    	        @Param("checkOutDate") LocalDate checkOutDate,
    	        @Param("statuses") List<BookingStatus> statuses);
}