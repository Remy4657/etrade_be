package com.example.demo.entity.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.shipping.ShippingEntity;
import com.example.demo.entity.user.UserEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = false)
    private UserEntity user;

    private Integer totalQuantity;
    private BigDecimal subtotalAmount; // tiền hàng
    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    private String status;
    // PENDING, PAID, SHIPPING, COMPLETED, CANCELLED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItemEntity = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_id", nullable = false, unique = false)
    private ShippingEntity shipping;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = false)
    private PaymentEntity payment;
}
