package com.example.demo.entity.payment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_method")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // COD, VNPAY, PAYPAL
    private String name;
}
