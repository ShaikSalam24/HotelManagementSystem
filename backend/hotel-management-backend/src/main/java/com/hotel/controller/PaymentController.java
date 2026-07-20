package com.hotel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.payment.PaymentRequest;
import com.hotel.dto.payment.PaymentResponse;
import com.hotel.dto.response.ApiResponse;
import com.hotel.enums.PaymentStatus;
import com.hotel.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse payment = paymentService.createPayment(request);

        ApiResponse<PaymentResponse> response = new ApiResponse<>(
                true,
                "Payment created successfully",
                payment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(
            @PathVariable Long id) {

        PaymentResponse payment = paymentService.getPaymentById(id);

        ApiResponse<PaymentResponse> response = new ApiResponse<>(
                true,
                "Payment retrieved successfully",
                payment);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByTransactionId(
            @PathVariable String transactionId) {

        PaymentResponse payment = paymentService.getPaymentByTransactionId(transactionId);

        ApiResponse<PaymentResponse> response = new ApiResponse<>(
                true,
                "Payment retrieved successfully",
                payment);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {

        List<PaymentResponse> payments = paymentService.getPayments();

        ApiResponse<List<PaymentResponse>> response = new ApiResponse<>(
                true,
                "Payments retrieved successfully",
                payments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByStatus(
            @RequestParam PaymentStatus status) {

        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);

        ApiResponse<List<PaymentResponse>> response = new ApiResponse<>(
                true,
                "Payments retrieved successfully",
                payments);

        return ResponseEntity.ok(response);
    }
}
