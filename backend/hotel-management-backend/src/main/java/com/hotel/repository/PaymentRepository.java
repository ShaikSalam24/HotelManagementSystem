package com.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.entity.Booking;
import com.hotel.entity.Payment;
import com.hotel.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByBooking(Booking booking);

    boolean existsByBooking(Booking booking);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}
