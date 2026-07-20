package com.hotel.service;


import java.util.List;

import com.hotel.dto.payment.PaymentRequest;
import com.hotel.dto.payment.PaymentResponse;
import com.hotel.enums.PaymentStatus;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    List<PaymentResponse> getPayments();

    List<PaymentResponse> getPaymentsByStatus(PaymentStatus paymentStatus);

}
