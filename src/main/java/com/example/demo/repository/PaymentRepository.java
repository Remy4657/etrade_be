package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.payment.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}