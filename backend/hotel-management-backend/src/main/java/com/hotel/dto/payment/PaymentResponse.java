package com.hotel.dto.payment;


import java.math.BigDecimal;

import com.hotel.enums.PaymentGateway;
import com.hotel.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private Long id;

    private String transactionId;

    private String bookingNumber;

    private BigDecimal amount;

    private String paymentMethod;

    private PaymentStatus paymentStatus;

    private String remarks;
    
    private PaymentGateway paymentGateway;
}