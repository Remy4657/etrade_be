package com.example.demo.entity.payment;

import java.math.BigDecimal;

import com.example.demo.entity.order.OrderEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethodEntity paymentMethod;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    private String status; // PENDING, PAID, FAILED
    private String transactionCode;
}
