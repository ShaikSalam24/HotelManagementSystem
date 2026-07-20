package com.hotel.service.impl;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.dto.payment.PaymentRequest;
import com.hotel.dto.payment.PaymentResponse;
import com.hotel.entity.Booking;
import com.hotel.entity.Payment;
import com.hotel.enums.PaymentGateway;
import com.hotel.enums.PaymentStatus;
import com.hotel.exception.BadRequestException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.PaymentRepository;
import com.hotel.service.PaymentService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

        @Override
        public PaymentResponse createPayment(PaymentRequest request) {

            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            if (paymentRepository.existsByBooking(booking)) {
                throw new BadRequestException("Payment already exists for this booking");
            }

            Payment payment = new Payment();

            payment.setTransactionId(generateTransactionId());
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setPaymentGateway(PaymentGateway.RAZORPAY);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setRemarks("Payment initiated");

            Payment savedPayment = paymentRepository.save(payment);

            return mapToResponse(savedPayment);
        }

        @Override
        @Transactional(readOnly = true)
        public PaymentResponse getPaymentById(Long id) {

            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            return mapToResponse(payment);
        }

        @Override
        @Transactional(readOnly = true)
        public PaymentResponse getPaymentByTransactionId(String transactionId) {

            Payment payment = paymentRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

            return mapToResponse(payment);
        }

        @Override
        @Transactional(readOnly = true)
        public List<PaymentResponse> getPayments() {

            return paymentRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PaymentResponse> getPaymentsByStatus(PaymentStatus paymentStatus) {

            return paymentRepository.findByPaymentStatus(paymentStatus)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        /**
         * Generates Internal Transaction Id
         */
        private String generateTransactionId() {

            return "TXN-" + UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        }

        /**
         * Entity -> DTO
         */
        private PaymentResponse mapToResponse(Payment payment) {

            return PaymentResponse.builder()
                    .id(payment.getId())
                    .transactionId(payment.getTransactionId())
                    .bookingNumber(payment.getBooking().getBookingNumber())
                    .amount(payment.getAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentGateway(payment.getPaymentGateway())
                    .paymentStatus(payment.getPaymentStatus())
                    .remarks(payment.getRemarks())
                    .build();
        }

}
