package com.taxsystem.servicepayment.controller;

import com.taxsystem.servicepayment.dto.PaymentDto;
import com.taxsystem.servicepayment.dto.PaymentResponseDto;
import com.taxsystem.servicepayment.model.Payment;
import com.taxsystem.servicepayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAll() {
        final List<PaymentResponseDto> paymentResponseDtos = paymentService.getAllResponse();
        return ResponseEntity.ok(paymentResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable long id) {
        try {
            PaymentResponseDto taxReportResponseDto = paymentService.getPaymentResponseDto(id);

            return ResponseEntity.ok(taxReportResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PaymentDto paymentDto) {
        final BigDecimal count = paymentDto.count();
        final long fromId = paymentDto.fromId();
        final long toId = paymentDto.toId();

        try {
            final long taxReportId = paymentService
                    .createPayment(count, fromId, toId);
            final String paymentUri = "/payment/" + taxReportId;

            return ResponseEntity.created(URI.create(paymentUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id,
                                       @RequestBody PaymentDto paymentDto) {
        final BigDecimal count = paymentDto.count();
        final long fromId;
        if (paymentDto.fromId() == null) {
            fromId = -1;
        }
        else {
            fromId = paymentDto.fromId();
        }

        final long toId;
        if (paymentDto.toId() == null) {
            toId = -1;
        }
        else {
            toId = paymentDto.toId();
        }

        try {
            paymentService.updatePayment(id, count, fromId, toId);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        paymentService.deletePayment(id);

        return ResponseEntity.noContent().build();
    }
}
