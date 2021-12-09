package com.taxsystem.servicepayment.service;

import com.taxsystem.servicepayment.dto.PaymentResponseDto;
import com.taxsystem.servicepayment.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    List<Payment> getAll();
    Payment getPaymentById(long id) throws IllegalArgumentException;
    long createPayment(BigDecimal count, Long fromId, Long toId)
            throws IllegalArgumentException;
    void updatePayment(long id, BigDecimal count, Long fromId, Long toId)
            throws IllegalArgumentException;
    void deletePayment(long id);
    List<PaymentResponseDto> getAllResponse();
    PaymentResponseDto getPaymentResponseDto(long id) throws IllegalArgumentException;
}
