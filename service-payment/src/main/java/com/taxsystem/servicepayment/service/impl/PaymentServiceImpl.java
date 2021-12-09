package com.taxsystem.servicepayment.service.impl;

import com.taxsystem.servicepayment.dto.PaymentResponseDto;
import com.taxsystem.servicepayment.dto.UserDto;
import com.taxsystem.servicepayment.enums.UserType;
import com.taxsystem.servicepayment.model.Payment;
import com.taxsystem.servicepayment.repository.PaymentRepository;
import com.taxsystem.servicepayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final String userUrlAdress ="http://service-identity:8080/user";

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(long id) throws IllegalArgumentException {
        final Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (optionalPayment.isPresent()) {
            return optionalPayment.get();
        }
        else {
            throw new IllegalArgumentException("Invalid tax payment id");
        }
    }

    private boolean checkForCorrectness(Long userId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Transactional
    @Override
    public long createPayment(BigDecimal count, Long fromId, Long toId)
            throws IllegalArgumentException {
        final Payment payment = new Payment(count, fromId, toId);

        if (!checkForCorrectness(fromId)
                || !checkForCorrectness(toId)) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final Payment savedPayment = paymentRepository.save(payment);
            return savedPayment.getId();
        }
    }

    @Transactional
    @Override
    public void updatePayment(long id, BigDecimal count, Long fromId, Long toId)
            throws IllegalArgumentException {
        final Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (optionalPayment.isEmpty()) {
            throw new IllegalArgumentException("Invalid tax payment id");
        }

        final Payment payment = optionalPayment.get();

        if (count != null && !count.equals(0)) payment.setCount(count);
        if (fromId != null && fromId != -1) {
            if (!checkForCorrectness(fromId)) {
                throw new IllegalArgumentException("Invalid from id");
            }

            payment.setFromId(fromId);
        }
        if (toId != null && toId != -1) {
            if (!checkForCorrectness(toId)) {
                throw new IllegalArgumentException("Invalid to id");
            }

            payment.setToId(toId);
        }

        paymentRepository.save(payment);

    }

    @Transactional
    @Override
    public void deletePayment(long id) {
        paymentRepository.deleteById(id);
    }

    private String getNameById(long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/" +id,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getBody().name();
    }

    private PaymentResponseDto paymentToResponse(Payment payment) {
        final BigDecimal count = payment.getCount();
        final String fromName = getNameById(payment.getFromId());
        final String toName = getNameById(payment.getToId());

        return new PaymentResponseDto(count, fromName, toName);
    }

    @Override
    public List<PaymentResponseDto> getAllResponse() {
        List<Payment> payments = getAll();
        List<PaymentResponseDto> paymentResponseDtos = new ArrayList<>();

        for(Payment payment: payments) {
            paymentResponseDtos.add(paymentToResponse(payment));
        }

        return paymentResponseDtos;
    }

    @Override
    public PaymentResponseDto getPaymentResponseDto(long id)
            throws IllegalArgumentException {
        try {
            Payment payment = getPaymentById(id);
            return paymentToResponse(payment);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
