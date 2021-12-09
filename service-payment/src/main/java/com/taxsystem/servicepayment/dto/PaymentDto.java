package com.taxsystem.servicepayment.dto;

import java.math.BigDecimal;

public record PaymentDto(BigDecimal count, Long fromId, Long toId) {
}
