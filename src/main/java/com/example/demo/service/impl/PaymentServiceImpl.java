package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.res.PaymentResponse;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(method -> new PaymentResponse(
                        method.getId(),
                        method.getCode(),
                        method.getName()))
                .toList();
    }
}
