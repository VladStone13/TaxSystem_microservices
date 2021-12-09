package com.taxsystem.servicepayment.dto;

import java.math.BigDecimal;

public record PaymentResponseDto(BigDecimal count, String fromName, String toName) {
}
