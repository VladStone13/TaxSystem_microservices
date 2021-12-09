package com.taxsystem.servicepayment.repository;

import com.taxsystem.servicepayment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
