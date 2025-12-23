package com.example.demo.entity.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.ShippingEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.user.UserEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Integer totalQuantity;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    private String status;
    // PENDING, PAID, SHIPPING, COMPLETED, CANCELLED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ShippingEntity shipping;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;
}
