package com.example.demo.entity;

import com.example.demo.entity.order.OrderEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shipping")
public class ShippingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private String receiverName;
    private String phone;
    private String address;

    private String shippingStatus;
}
